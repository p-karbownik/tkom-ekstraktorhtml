package parser;

import lexer.Lexer;
import lexer.*;
import lexer.Number;
import lexer.TokenType;
import parser.structures.*;
import exceptions.lexer.LexerException;
import exceptions.parser.*;
import reader.Position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
    private final Lexer lexer;
    private Token currentToken;

    private HashMap<String, Resource> parsedResources;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public void parse() throws UnexpectedTokenException, IOException, LexerException, ResourceRedefinitionException, UndefinedResourceException, RangeException, FieldRedefinitionException {
        readToken();

        parsedResources = new HashMap<>();
        Resource resource;

        while ((resource = parseResource()) != null) // dopoki sa resourcy
        {
            if (parsedResources.containsKey(resource.getName()))
                throw new ResourceRedefinitionException(resource.getName(), currentToken.getPosition());
            else
                parsedResources.put(resource.getName(), resource); // sprawdzic czy nie ma metody, ktora sprawdza czy dany klucz juz sie znajduje

            readToken();
        }
    }

    public HashMap<String, Resource> getParsedResources() {
        return parsedResources;
    }

    public Resource parseResource() throws UnexpectedTokenException, IOException, LexerException, UndefinedResourceException, RangeException, FieldRedefinitionException {
        if (!isTypeOf(TokenType.RESOURCE))
            return null;

        readToken(TokenType.IDENTIFIER);

        String identifier = currentToken.getContent();

        readToken();
        DefinitionBlock definitionBlock = parseDefinitionBlock();

        if (definitionBlock == null)
            throw new UnexpectedTokenException(currentToken.getType(), currentToken.getPosition(), TokenType.LEFT_BRACE); // przypadek gdy

        return new Resource(identifier, definitionBlock);
    }

    private DefinitionBlock parseDefinitionBlock() throws UnexpectedTokenException, IOException, LexerException, UndefinedResourceException, RangeException, FieldRedefinitionException {
        if (!isTypeOf(TokenType.LEFT_BRACE))
            return null;

        readToken();

        TagSentence tagSentence = parseTagSentence();

        if (tagSentence == null)
            throw new UnexpectedTokenException(currentToken.getType(), currentToken.getPosition(), TokenType.TAG);

        readToken();

        ConditionsBlock conditionsBlock = null;

        if (isTypeOf(TokenType.CONDITIONS)) {
            readToken();
            conditionsBlock = parseConditionsBlock();

            //sprawdzenie czy conditionsBlock istnieje
            if (conditionsBlock == null)
                throw new UnexpectedTokenException(currentToken.getType(), currentToken.getPosition(), TokenType.LEFT_BRACE);
        }

        ClassLine classLine = parseClassLine();

        if (classLine == null)
            throw new UnexpectedTokenException(currentToken.getType(), currentToken.getPosition(), TokenType.EXPORT);

        parseSetFields();

        FieldDefinitionBlock fieldsDefinitionBlock = parseFieldDefinitionBlock();

        if (fieldsDefinitionBlock == null)
            throw new UnexpectedTokenException(currentToken.getType(), currentToken.getPosition(), TokenType.LEFT_BRACE);

        QuantitativeConstraintSentence quantitativeConstraintSentence = parseAmountSentence();

        if (quantitativeConstraintSentence == null) {
            quantitativeConstraintSentence = parseRangeSentence();

            if (quantitativeConstraintSentence == null)
                throw new UnexpectedTokenException(currentToken.getType(), currentToken.getPosition(), TokenType.AMOUNT, TokenType.RANGE);
        }

        mustBe(TokenType.RIGHT_BRACE);

        return new DefinitionBlock(tagSentence, conditionsBlock, classLine, fieldsDefinitionBlock, quantitativeConstraintSentence);
    }

    private TagSentence parseTagSentence() throws UnexpectedTokenException, IOException, LexerException {
        if (!isTypeOf(TokenType.TAG))
            return null;

        readToken(TokenType.ASSIGN_OPERATOR);
        readToken(TokenType.IDENTIFIER);

        String tagName = currentToken.getContent();

        readToken(TokenType.SEMI_COLON);

        return new TagSentence(tagName);
    }

    private ConditionsBlock parseConditionsBlock() throws UnexpectedTokenException, IOException, LexerException {
        if (!isTypeOf(TokenType.LEFT_BRACE))
            return null;

        readToken();

        ArrayList<ConditionSentence> conditionSentences = new ArrayList<>();

        ConditionSentence conditionSentence = null;

        do {
            conditionSentence = parseConditionSentence();

            if (conditionSentence != null)
                conditionSentences.add(conditionSentence);

        } while (conditionSentence != null);

        mustBe(TokenType.RIGHT_BRACE);

        readToken();

        return new ConditionsBlock(conditionSentences);
    }

    private ConditionSentence parseConditionSentence() throws UnexpectedTokenException, IOException, LexerException {
        if (!isTypeOf(TokenType.IF))
            return null;

        readToken();
        Condition condition = parseCondition();

        mustBe(TokenType.SEMI_COLON);
        readToken();

        return new ConditionSentence(condition);
    }

    private Condition parseCondition() throws IOException, LexerException, UnexpectedTokenException {
        ArrayList<Term> terms = new ArrayList<>();
        Term term = parseTerm();
        terms.add(term);

        while (isTypeOf(TokenType.OR)) {
            readToken();
            term = parseTerm();
            terms.add(term);
        }

        return new Condition(terms);
    }

    private Term parseTerm() throws UnexpectedTokenException, IOException, LexerException {
        ArrayList<Factor> factors = new ArrayList<>();
        Factor factor = parseFactor();

        factors.add(factor);

        while (isTypeOf(TokenType.AND)) {
            readToken();
            factor = parseFactor();
            factors.add(factor);
        }

        return new Term(factors);
    }

    private Factor parseFactor() throws UnexpectedTokenException, IOException, LexerException {
        Path path = parsePath();

        if (path == null) {
            mustBe(TokenType.LEFT_ROUND_BRACKET);
            readToken();

            Condition condition = parseCondition();

            mustBe(TokenType.RIGHT_ROUND_BRACKET);
            readToken();

            return new Factor(condition);
        } else {
            mustBe(TokenType.HAS);

            readToken();
            FactorObject factorObject = parseFactorObject();

            return new Factor(path, factorObject);
        }

    }

    private FactorObject parseFactorObject() throws UnexpectedTokenException, IOException, LexerException {
        boolean isNegated = false;

        if (isTypeOf(TokenType.NOT)) {
            isNegated = true;
            readToken();
        }

        if (isTypeOf(TokenType.TAG)) {
            readToken(TokenType.IDENTIFIER);
            Subject subject = new TagSubject(currentToken.getContent());
            readToken();

            return new FactorObject(isNegated, subject);
        } else {
            Subject subject = parseSubject();

            if (subject == null)
                throw new UnexpectedTokenException(currentToken.getType(), currentToken.getPosition(), TokenType.ATTRIBUTE);

            mustBe(TokenType.IDENTIFIER, TokenType.CLASS);

            String subjectIdentifier;

            if(isTypeOf(TokenType.IDENTIFIER))
                subjectIdentifier = currentToken.getContent();
            else
                subjectIdentifier = "class";

            subject.setIdentifier(subjectIdentifier);

            readToken();

            ComparisonObject comparisonObject = parseComparisonObject();

            if (comparisonObject != null) {
                return new FactorObject(isNegated, subject, comparisonObject);
            }

            return new FactorObject(isNegated, subject);
        }
    }

    private ComparisonObject parseComparisonObject() throws IOException, LexerException, UnexpectedTokenException {
        if (!isTypeOf(TokenType.IN) && !isTypeOf(TokenType.NOT_EQUAL) && !isTypeOf(TokenType.EQUAL))
            return null;

        if(isTypeOf(TokenType.IN))
        {
            readToken();

            ValueSet valueSet = parseValueSet();

            return new ComparisonObject(valueSet);
        }

        ComparisonOperator comparisonOperator = parseComparisonOperator();

        mustBe(TokenType.STRING);

        String value = currentToken.getContent();
        readToken();

        return new ComparisonObject(comparisonOperator, value);
    }

    private Subject parseSubject() throws IOException, LexerException {
        if (isTypeOf(TokenType.ATTRIBUTE)) {
            readToken();
            return new AttributeSubject();
        } else
            return null;
    }

    private ValueSet parseValueSet() throws UnexpectedTokenException, IOException, LexerException {
        ArrayList<String> values = new ArrayList<>();

        mustBe(TokenType.LEFT_BRACE);

        readToken(TokenType.STRING);

        while (isTypeOf(TokenType.STRING)) {
            values.add(currentToken.getContent());
            readToken();

            if (isTypeOf(TokenType.COMMA))
                readToken(TokenType.STRING);
        }

        mustBe(TokenType.RIGHT_BRACE);
        readToken();

        return new ValueSet(values);
    }

    private ComparisonOperator parseComparisonOperator() throws IOException, LexerException {
        if (!isTypeOf(TokenType.EQUAL) && !isTypeOf(TokenType.NOT_EQUAL))
            return null;

        TokenType tokenType = currentToken.getType();

        readToken();

        return new ComparisonOperator(tokenType);
    }

    private Path parsePath() throws UnexpectedTokenException, IOException, LexerException {
        if (!isTypeOf(TokenType.SELF) &&
                !isTypeOf(TokenType.ANCESTOR) &&
                !isTypeOf(TokenType.PARENT) &&
                !isTypeOf(TokenType.CHILD) &&
                !isTypeOf(TokenType.DESCENDANT))
            return null;

        if (isTypeOf(TokenType.CHILD)) {
            ArrayList<PathElement> elements = new ArrayList<>();

            readToken();

            RelativeCondition relativeCondition = parseRelativeCondition();
            elements.add(new ChildPathElement(relativeCondition));

            while (isTypeOf(TokenType.DOT)) {
                readToken(TokenType.CHILD);
                readToken();
                relativeCondition = parseRelativeCondition();
                elements.add(new ChildPathElement(relativeCondition));
            }

            return new Path(elements);
        }

        if (isTypeOf(TokenType.PARENT)) {
            ArrayList<PathElement> elements = new ArrayList<>();

            elements.add(new ParentPathElement());

            readToken();

            while (isTypeOf(TokenType.DOT)) {
                readToken(TokenType.PARENT);
                elements.add(new ParentPathElement());
                readToken();
            }

            return new Path(elements);
        }

        TokenType tokenType = currentToken.getType();

        readToken();

        return new Path(tokenType);
    }

    private RelativeCondition parseRelativeCondition() throws IOException, LexerException, UnexpectedTokenException {
        if (!isTypeOf(TokenType.LEFT_ROUND_BRACKET))
            return null;

        readToken();

        Condition condition = parseCondition();

        mustBe(TokenType.RIGHT_ROUND_BRACKET);
        readToken();

        return new RelativeCondition(condition);
    }

    private void parseSetFields() throws UnexpectedTokenException, IOException, LexerException {
        mustBe(TokenType.SET);
        readToken(TokenType.FIELDS);
        readToken();
    }

    private ClassLine parseClassLine() throws UnexpectedTokenException, IOException, LexerException {
        if (!isTypeOf(TokenType.EXPORT))
            return null;

        readToken(TokenType.TO);
        readToken(TokenType.CLASS);
        readToken(TokenType.ASSIGN_OPERATOR);
        readToken(TokenType.STRING);

        String classIdentifier = currentToken.getContent();

        readToken(TokenType.SEMI_COLON);

        readToken();

        return new ClassLine(classIdentifier);
    }

    private FieldDefinitionBlock parseFieldDefinitionBlock() throws UnexpectedTokenException, IOException, LexerException, UndefinedResourceException, RangeException, FieldRedefinitionException {
        if (!isTypeOf(TokenType.LEFT_BRACE))
            return null;

        readToken();

        HashMap<String, FieldDefinition> fieldDefinitions = new HashMap<>();

        FieldDefinition fieldDefinition = parseFieldDefinition();

        if (fieldDefinition == null)
            throw new UnexpectedTokenException(currentToken.getType(), currentToken.getPosition(), TokenType.FIELD);

        fieldDefinitions.put(fieldDefinition.getFieldIdentifier(), fieldDefinition);

        do {
            Position position = currentToken.getPosition();
            fieldDefinition = parseFieldDefinition();

            if (fieldDefinition != null) {
                if(fieldDefinitions.containsKey(fieldDefinition.getFieldIdentifier()))
                    throw new FieldRedefinitionException(fieldDefinition.getFieldIdentifier(), position);

                fieldDefinitions.put(fieldDefinition.getFieldIdentifier(), fieldDefinition);
            }

        } while (fieldDefinition != null);

        mustBe(TokenType.RIGHT_BRACE);
        readToken();

        return new FieldDefinitionBlock(new ArrayList<>(fieldDefinitions.values()));
    }

    private FieldDefinition parseFieldDefinition() throws UnexpectedTokenException, IOException, LexerException, UndefinedResourceException, RangeException {
        if (!isTypeOf(TokenType.FIELD))
            return null;

        readToken(TokenType.IDENTIFIER);

        String fieldIdentifier = currentToken.getContent();

        readToken(TokenType.ASSIGN_OPERATOR);
        readToken();

        PathToResource pathToResource = parsePathToResource();

        if (pathToResource == null)
            throw new UnexpectedTokenException(currentToken.getType(), currentToken.getPosition(), TokenType.FROM);

        if (isTypeOf(TokenType.SEMI_COLON)) {
            readToken();

            return new FieldDefinition(fieldIdentifier, pathToResource);
        } else if (isTypeOf(TokenType.DOT)) {
            readToken(TokenType.ATTRIBUTE, TokenType.RESOURCE);

            if (isTypeOf(TokenType.ATTRIBUTE)) {
                readToken(TokenType.LEFT_SQUARE_BRACKET);
                readToken(TokenType.IDENTIFIER, TokenType.CLASS);

                String attributeIdentifier = (isTypeOf(TokenType.IDENTIFIER)) ? currentToken.getContent() : "class";

                readToken(TokenType.RIGHT_SQUARE_BRACKET);

                readToken();

                if (isTypeOf(TokenType.SEMI_COLON)) {
                    readToken();

                    return new FieldDefinition(fieldIdentifier, pathToResource, attributeIdentifier, false);
                } else if (isTypeOf(TokenType.DOT)) {
                    readToken(TokenType.ASIMG);
                    readToken(TokenType.SEMI_COLON);
                    readToken();
                    return new FieldDefinition(fieldIdentifier, pathToResource, attributeIdentifier, false, true);
                }
            } else if (isTypeOf(TokenType.RESOURCE)) {
                readToken(TokenType.LEFT_SQUARE_BRACKET);
                readToken(TokenType.IDENTIFIER);
                String resourceIdentifier = currentToken.getContent();

                if (!parsedResources.containsKey(resourceIdentifier))
                    throw new UndefinedResourceException(resourceIdentifier, currentToken.getPosition());

                readToken(TokenType.RIGHT_SQUARE_BRACKET);
                readToken(TokenType.SEMI_COLON);
                readToken();

                return new FieldDefinition(fieldIdentifier, pathToResource, resourceIdentifier, true);
            }
        }

        return null;
    }

    private PathToResource parsePathToResource() throws UnexpectedTokenException, IOException, LexerException, RangeException {
        if (!isTypeOf(TokenType.FROM))
            return null;

        readToken(TokenType.LEFT_ROUND_BRACKET);

        readToken();

        ArrayList<PathElement> pathElements = new ArrayList<>();

        if (isTypeOf(TokenType.SELF)) {
            pathElements.add(new SelfPathElement());
            readToken();
        } else {
            TagPathElement tagPathElement = parseTagPathElement();

            if (tagPathElement == null)
                throw new UnexpectedTokenException(currentToken.getType(), currentToken.getPosition(), TokenType.TAG);

            pathElements.add(tagPathElement);

            while (isTypeOf(TokenType.DOT)) {
                readToken();
                tagPathElement = parseTagPathElement();

                if (tagPathElement == null)
                    throw new UnexpectedTokenException(currentToken.getType(), currentToken.getPosition(), TokenType.TAG);

                pathElements.add(tagPathElement);
            }
        }

        mustBe(TokenType.RIGHT_ROUND_BRACKET);
        readToken();

        return new PathToResource(pathElements);
    }

    private TagPathElement parseTagPathElement() throws UnexpectedTokenException, IOException, LexerException{
        if (!isTypeOf(TokenType.TAG))
            return null;

        readToken(TokenType.LEFT_SQUARE_BRACKET);
        readToken(TokenType.IDENTIFIER);

        String identifierContent = currentToken.getContent();

        readToken(TokenType.RIGHT_SQUARE_BRACKET);

        readToken();

        int number = 0;

        if (isTypeOf(TokenType.LEFT_SQUARE_BRACKET)) {
            readToken(TokenType.NUMBER);

            number = ((Number) currentToken).getValue();

            readToken(TokenType.RIGHT_SQUARE_BRACKET);
            readToken();
        }

        return new TagPathElement(identifierContent, number);
    }

    private AmountSentence parseAmountSentence() throws UnexpectedTokenException, IOException, LexerException {
        if (!isTypeOf(TokenType.AMOUNT))
            return null;

        readToken(TokenType.ASSIGN_OPERATOR);
        readToken(TokenType.NUMBER, TokenType.EVERY);

        boolean isEvery = false;
        int value = 0;

        if (isTypeOf(TokenType.EVERY))
            isEvery = true;
        else
            value = ((Number) currentToken).getValue();

        readToken(TokenType.SEMI_COLON);
        readToken();

        if (isEvery)
            return new AmountSentence(true);
        else
            return new AmountSentence(value);

    }

    private RangeSentence parseRangeSentence() throws UnexpectedTokenException, IOException, LexerException, RangeException {
        if (!isTypeOf(TokenType.RANGE))
            return null;

        readToken(TokenType.ASSIGN_OPERATOR);
        readToken(TokenType.LEFT_ROUND_BRACKET);
        readToken(TokenType.NUMBER);

        Number from = (Number) currentToken;

        readToken(TokenType.COMMA);
        readToken(TokenType.NUMBER);

        Number to = (Number) currentToken;

        readToken(TokenType.RIGHT_ROUND_BRACKET);
        readToken(TokenType.SEMI_COLON);

        readToken();
        if(from.getValue() > to.getValue())
            throw new RangeException(from, to);

        return new RangeSentence(from.getValue(), to.getValue());
    }

    private void readToken() throws IOException, LexerException {
        currentToken = lexer.getNextToken();
    }

    private void readToken(TokenType... tokenType) throws UnexpectedTokenException, IOException, LexerException {
        currentToken = lexer.getNextToken();
        mustBe(tokenType);
    }

    private void mustBe(TokenType... expectedTokens) throws UnexpectedTokenException {
        boolean throwException = true;

        for (TokenType t : expectedTokens)
            if (isTypeOf(t)) {
                throwException = false;
                break;
            }

        if (throwException)
            throw new UnexpectedTokenException(currentToken.getType(), currentToken.getPosition(), expectedTokens);
    }

    private boolean isTypeOf(TokenType expectedTokenType) {
        return currentToken.getType() == expectedTokenType;
    }

    TagSentence parseTagSentenceForTest() throws Exception {
        readToken();
        return parseTagSentence();
    }

    void parseSetFieldsForTest() throws Exception {
        readToken();
        parseSetFields();
    }

    ClassLine parseClassLineForTest() throws Exception {
        readToken();
        return parseClassLine();
    }

    AmountSentence parseAmountSentenceForTest() throws Exception {
        readToken();
        return parseAmountSentence();
    }

    TagPathElement parseTagPathElementForTest() throws Exception {
        readToken();
        return parseTagPathElement();
    }

    RangeSentence parseRangeSentenceForTest() throws Exception {
        readToken();
        return parseRangeSentence();
    }

    PathToResource parsePathToResourceForTest() throws Exception {
        readToken();
        return parsePathToResource();
    }

    FieldDefinition parseFieldDefinitionForTest() throws Exception {
        readToken();
        return parseFieldDefinition();
    }

    ComparisonOperator parseComparisonOperatorForTest() throws Exception {
        readToken();
        return parseComparisonOperator();
    }

    Subject parseSubjectForTest() throws Exception {
        readToken();
        return parseSubject();
    }

    ValueSet parseValueSetForTest() throws Exception {
        readToken();
        return parseValueSet();
    }

    Path parsePathForTest() throws Exception {
        readToken();
        return parsePath();
    }

    ConditionSentence parseConditionSentenceForTest() throws Exception {
        readToken();
        return parseConditionSentence();
    }

    ComparisonObject parseComparisonObjectForTest() throws Exception {
        readToken();
        return parseComparisonObject();
    }

}