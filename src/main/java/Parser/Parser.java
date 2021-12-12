package Parser;

import Lexer.Lexer;
import Lexer.*;
import Lexer.Number;
import Lexer.TokenType;
import Parser.Structures.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
    private final Lexer lexer;
    private Token currentToken;

    private HashMap<String, Resource> parsedResources;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public void parse() throws Exception
    {
        readToken();

        parsedResources = new HashMap<>();

        while (currentToken.getType() != TokenType.ETX)
        {
            Resource resource = parseResource();

            if(resource != null)
            {
                if(parsedResources.containsKey(resource.getName()))
                    throw new Exception();
                else
                    parsedResources.put(resource.getName(), resource);
            }

            readToken();
        }
    }

    public HashMap<String, Resource> getParsedResources()
    {
        return parsedResources;
    }

    public Resource parseResource() throws Exception {
        if (lexer.getCurrentToken().getType() != TokenType.RESOURCE)
            return null;

        readToken(TokenType.IDENTIFIER);

        String identifier = currentToken.getContent();

        readToken();
        DefinitionBlock definitionBlock = parseDefinitionBlock();

        return new Resource(identifier, definitionBlock);
    }

    private DefinitionBlock parseDefinitionBlock() throws Exception {
        if (currentToken.getType() != TokenType.LEFT_BRACE)
            return null;

        readToken();

        TagSentence tagSentence = parseTagSentence();

        readToken();

        ConditionsBlock conditionsBlock = null;

        if (currentToken.getType() == TokenType.CONDITIONS) {
            readToken();
            conditionsBlock = parseConditionsBlock();
        }

        ClassLine classLine = parseClassLine();

        parseSetFields();

        FieldDefinitionBlock fieldsDefinitionBlock = parseFieldDefinitionBlock();
        AmountSentence amountSentence = parseAmountSentence();
        mustBe(TokenType.RIGHT_BRACE);

        return new DefinitionBlock(tagSentence, conditionsBlock, classLine, fieldsDefinitionBlock, amountSentence);
    }

    private TagSentence parseTagSentence() throws Exception {
        mustBe(TokenType.TAG);

        readToken(TokenType.ASSIGN_OPERATOR);
        readToken(TokenType.IDENTIFIER);

        String tagName = currentToken.getContent();

        readToken(TokenType.SEMI_COLON);

        return new TagSentence(tagName);
    }

    private ConditionsBlock parseConditionsBlock() throws Exception {
        if (currentToken.getType() != TokenType.LEFT_BRACE)
            return null;

        readToken();

        ArrayList<ConditionSentence> conditionSentences = new ArrayList<>();

        ConditionSentence conditionSentence = null;

        do {
            conditionSentence = parseConditionSentence();

            if (conditionSentence != null)
                conditionSentences.add(conditionSentence);

        } while (conditionSentence != null);

        if (currentToken.getType() != TokenType.RIGHT_BRACE)
            throw new Exception("");

        readToken();

        return new ConditionsBlock(conditionSentences);
    }

    private ConditionSentence parseConditionSentence() throws Exception {

        if (currentToken.getType() != TokenType.IF)
            return null;
        readToken();
        Condition condition = parseCondition();

        mustBe(TokenType.SEMI_COLON);
        readToken();

        return new ConditionSentence(condition);
    }

    private Condition parseCondition() throws Exception {
        ArrayList<Term> terms = new ArrayList<>();
        Term term = parseTerm();
        terms.add(term);

        while (currentToken.getType() == TokenType.OR) {
            readToken();
            term = parseTerm();
            terms.add(term);
        }

        return new Condition(terms);
    }

    private Term parseTerm() throws Exception {
        ArrayList<Factor> factors = new ArrayList<>();
        Factor factor = parseFactor();

        factors.add(factor);

        while (currentToken.getType() == TokenType.AND) {
            readToken();
            factor = parseFactor();
            factors.add(factor);
        }

        return new Term(factors);
    }

    private Factor parseFactor() throws Exception {
        Path path = parsePath();

        if (path == null) {
            mustBe(TokenType.LEFT_ROUND_BRACKET);
            readToken();

            Condition condition = parseCondition();

            mustBe(TokenType.RIGHT_ROUND_BRACKET);
            readToken();

            return new Factor(condition);
        } else {
            if(currentToken.getType() != TokenType.HAS)
                throw new Exception();

            readToken();
            FactorObject factorObject = parseFactorObject();

            return new Factor(path, factorObject);
        }

    }

    private FactorObject parseFactorObject() throws Exception {
        boolean isNegated = false;

        if (currentToken.getType() == TokenType.NOT) {
            isNegated = true;
            readToken();
        }

        if (currentToken.getType() == TokenType.TAG) {
            readToken(TokenType.IDENTIFIER);
            Subject subject = new TagSubject(currentToken.getContent());
            readToken();

            return new FactorObject(isNegated, subject);
        } else {
            Subject subject = parseSubject();

            if(currentToken.getType() != TokenType.IDENTIFIER)
                throw new Exception();

            String subjectIdentifier = currentToken.getContent();
            subject.setIdentifier(subjectIdentifier);

            readToken();

            ComparisonOperator comparisonOperator = parseComparisonOperator();

            if (comparisonOperator != null) {
                ValueSet valueSet = parseValueSet();

                return new FactorObject(isNegated, subject, comparisonOperator, valueSet);
            }

            return new FactorObject(isNegated, subject);
        }
    }

    private Subject parseSubject() throws Exception {
        if (currentToken.getType() == TokenType.CLASS) {
            readToken();
            return new ClassSubject();
        } else if (currentToken.getType() == TokenType.ATTRIBUTE) {
            readToken();
            return new AttributeSubject();
        } else
            return null;
    }

    private ValueSet parseValueSet() throws Exception {
        ArrayList<String> values = new ArrayList<>();

        mustBe(TokenType.LEFT_BRACE, TokenType.STRING);

        if (currentToken.getType() == TokenType.STRING) {
            values.add(currentToken.getContent());
        } else {
            readToken(TokenType.STRING);

            while (currentToken.getType() == TokenType.STRING) {
                values.add(currentToken.getContent());
                readToken();

                if (currentToken.getType() == TokenType.COMMA)
                    readToken(TokenType.STRING);
            }

            mustBe(TokenType.RIGHT_BRACE);
            readToken();
        }

        return new ValueSet(values);
    }

    private ComparisonOperator parseComparisonOperator() throws Exception {
        if (currentToken.getType() != TokenType.EQUAL && currentToken.getType() != TokenType.NOT_EQUAL)
            return null;

        TokenType tokenType = currentToken.getType();

        readToken();

        return new ComparisonOperator(tokenType);
    }

    private Path parsePath() throws Exception {
        if (currentToken.getType() != TokenType.SELF &&
                currentToken.getType() != TokenType.ANCESTOR &&
                currentToken.getType() != TokenType.PARENT &&
                currentToken.getType() != TokenType.CHILD &&
                currentToken.getType() != TokenType.DESCENDANT)
            return null;

        if (currentToken.getType() == TokenType.CHILD) {
            ArrayList<PathElement> elements = new ArrayList<>();

            readToken();

            RelativeCondition relativeCondition = parseRelativeCondition();
            elements.add(new ChildPathElement(relativeCondition));

            while (currentToken.getType() == TokenType.DOT) {
                readToken(TokenType.CHILD);
                readToken();
                relativeCondition = parseRelativeCondition();
                elements.add(new ChildPathElement(relativeCondition));
            }

            return new Path(elements);
        }

        if (currentToken.getType() == TokenType.PARENT) {
            ArrayList<PathElement> elements = new ArrayList<>();

            elements.add(new ParentPathElement());

            readToken();

            while (currentToken.getType() == TokenType.DOT) {
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

    private RelativeCondition parseRelativeCondition() throws Exception {
        if (currentToken.getType() != TokenType.LEFT_ROUND_BRACKET)
            return null;

        readToken();

        Condition condition = parseCondition();

        mustBe(TokenType.RIGHT_ROUND_BRACKET);
        readToken();

        return new RelativeCondition(condition);
    }

    private void parseSetFields() throws Exception {
        mustBe(TokenType.SET);
        readToken(TokenType.FIELDS);
        readToken();
    }

    private ClassLine parseClassLine() throws Exception {

        if (currentToken.getType() != TokenType.EXPORT)
            return null;

        readToken(TokenType.TO);
        readToken(TokenType.CLASS);
        readToken(TokenType.ASSIGN_OPERATOR);
        readToken(TokenType.IDENTIFIER);

        String classIdentifier = currentToken.getContent();

        readToken(TokenType.SEMI_COLON);

        readToken();

        return new ClassLine(classIdentifier);
    }

    private FieldDefinitionBlock parseFieldDefinitionBlock() throws Exception {
        if (currentToken.getType() != TokenType.LEFT_BRACE)
            return null;

        readToken();

        ArrayList<FieldDefinition> fieldDefinitionArrayList = new ArrayList<>();

        FieldDefinition fieldDefinition = parseFieldDefinition();

        if (fieldDefinition == null)
            throw new Exception();

        fieldDefinitionArrayList.add(fieldDefinition);

        do {
            fieldDefinition = parseFieldDefinition();

            if (fieldDefinition != null)
                fieldDefinitionArrayList.add(fieldDefinition);

        } while (fieldDefinition != null);

        mustBe(TokenType.RIGHT_BRACE);
        readToken();

        return new FieldDefinitionBlock(fieldDefinitionArrayList);
    }

    private FieldDefinition parseFieldDefinition() throws Exception {
        if (currentToken.getType() != TokenType.FIELD)
            return null;

        readToken(TokenType.IDENTIFIER);

        String fieldIdentifier = currentToken.getContent();

        readToken(TokenType.ASSIGN_OPERATOR);
        readToken();

        PathToResource pathToResource = parsePathToResource();

        if (pathToResource == null)
            throw new Exception("");

        if (currentToken.getType() == TokenType.SEMI_COLON) {
            readToken();

            return new FieldDefinition(fieldIdentifier, pathToResource);
        } else if (currentToken.getType() == TokenType.DOT) {
            readToken(TokenType.ATTRIBUTE, TokenType.RESOURCE);

            if (currentToken.getType() == TokenType.ATTRIBUTE) {
                readToken(TokenType.LEFT_SQUARE_BRACKET);
                readToken(TokenType.IDENTIFIER);

                String attributeIdentifier = currentToken.getContent();

                readToken(TokenType.RIGHT_SQUARE_BRACKET);

                readToken();

                if (currentToken.getType() == TokenType.SEMI_COLON) {
                    readToken();

                    return new FieldDefinition(fieldIdentifier, pathToResource, attributeIdentifier, false);
                } else if (currentToken.getType() == TokenType.DOT) {
                    readToken(TokenType.ASIMG);
                    readToken(TokenType.SEMI_COLON);
                    readToken();
                    return new FieldDefinition(fieldIdentifier, pathToResource, attributeIdentifier, false,true);
                }
            }
            else if(currentToken.getType() == TokenType.RESOURCE)
            {
                readToken(TokenType.LEFT_SQUARE_BRACKET);
                readToken(TokenType.IDENTIFIER);
                String resourceIdentifier = currentToken.getContent();

                if(!parsedResources.containsKey(resourceIdentifier))
                    throw new Exception();

                readToken(TokenType.RIGHT_SQUARE_BRACKET);
                readToken(TokenType.SEMI_COLON);
                readToken();

                return new FieldDefinition(fieldIdentifier, pathToResource, resourceIdentifier, true);
            }
        }

        return null;
    }

    private PathToResource parsePathToResource() throws Exception {
        if (currentToken.getType() != TokenType.FROM)
            return null;

        readToken(TokenType.LEFT_ROUND_BRACKET);

        readToken();

        ArrayList<PathElement> pathElements = new ArrayList<>();

        if (currentToken.getType() == TokenType.SELF) {
            pathElements.add(new SelfPathElement());
            readToken();
        } else {
            TagPathElement tagPathElement = parseTagPathElement();

            if (tagPathElement == null)
                throw new Exception("");

            pathElements.add(tagPathElement);

            while (currentToken.getType() == TokenType.DOT) {
                readToken();
                tagPathElement = parseTagPathElement();

                if (tagPathElement == null)
                    throw new Exception("");

                pathElements.add(tagPathElement);
            }
        }

        mustBe(TokenType.RIGHT_ROUND_BRACKET);
        readToken();
        return new PathToResource(pathElements);
    }

    private TagPathElement parseTagPathElement() throws Exception {
        if (currentToken.getType() != TokenType.TAG)
            return null;

        readToken(TokenType.LEFT_SQUARE_BRACKET);
        readToken(TokenType.IDENTIFIER);

        String identifierContent = currentToken.getContent();

        readToken(TokenType.RIGHT_SQUARE_BRACKET);

        readToken();

        int number = -1;

        if (currentToken.getType() == TokenType.LEFT_SQUARE_BRACKET) {
            readToken(TokenType.NUMBER);

            number = ((Number) currentToken).getValue();

            readToken(TokenType.RIGHT_SQUARE_BRACKET);
            readToken();
        }

        return new TagPathElement(identifierContent, number);
    }

    private AmountSentence parseAmountSentence() throws Exception {
        if (currentToken.getType() != TokenType.AMOUNT)
            return null;

        readToken(TokenType.ASSIGN_OPERATOR);
        readToken(TokenType.NUMBER, TokenType.EVERY);

        boolean isEvery = false;
        int value = 0;

        if (currentToken.getType() == TokenType.EVERY)
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

    private RangeSentence parseRangeSentence() throws Exception {
        if(currentToken.getType() != TokenType.RANGE)
            return null;

        readToken(TokenType.ASSIGN_OPERATOR);
        readToken(TokenType.LEFT_ROUND_BRACKET);
        readToken(TokenType.NUMBER);

        int from = ((Number) currentToken).getValue();

        readToken(TokenType.COMMA);
        readToken(TokenType.NUMBER);
        int to = ((Number) currentToken).getValue();
        readToken(TokenType.RIGHT_ROUND_BRACKET);
        readToken(TokenType.SEMI_COLON);

        return new RangeSentence(from, to);
    }

    private void readToken() throws Exception {
        currentToken = lexer.getNextToken();
    }

    private void readToken(TokenType... tokenType) throws Exception {
        currentToken = lexer.getNextToken();

        boolean throwException = true;

        for (TokenType t : tokenType)
            if (currentToken.getType() == t) {
                throwException = false;
                break;
            }

        if (throwException)
            throw new Exception("");
    }

    private void mustBe(TokenType... expectedTokens) throws Exception {

        boolean throwException = true;

        for (TokenType t : expectedTokens)
            if (currentToken.getType() == t) {
                throwException = false;
                break;
            }

        if (throwException)
            throw new Exception("");
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

    TagPathElement parseTagPathElementForTest() throws Exception{
        readToken();
        return parseTagPathElement();
    }

    RangeSentence parseRangeSentenceForTest() throws Exception{
        readToken();
        return parseRangeSentence();
    }

    PathToResource parsePathToResourceForTest() throws Exception{
        readToken();
        return parsePathToResource();
    }

    FieldDefinition parseFieldDefinitionForTest() throws Exception{
        readToken();
        return parseFieldDefinition();
    }

    ComparisonOperator parseComparisonOperatorForTest() throws Exception{
        readToken();
        return parseComparisonOperator();
    }

    Subject parseSubjectForTest() throws Exception{
        readToken();
        return parseSubject();
    }

    ValueSet parseValueSetForTest() throws Exception{
        readToken();
        return parseValueSet();
    }

    Path parsePathForTest() throws Exception
    {
        readToken();
        return parsePath();
    }

    ConditionSentence parseConditionSentenceForTest() throws Exception
    {
        readToken();
        return parseConditionSentence();
    }

}