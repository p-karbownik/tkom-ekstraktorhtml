package Parser;

import Lexer.Lexer;
import Lexer.Token;
import Lexer.TokenType;
import Structures.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final Lexer lexer;
    private Token currentToken;

    public Parser(Lexer lexer)
    {
        this.lexer = lexer;
    }

    public Resource parse() throws Exception {
        Resource resource = null;
        TagSentence tagSentence = null;

        readToken(TokenType.RESOURCE);
        readToken(TokenType.LEFT_BRACE);

        tagSentence = parseTagSentence();

        ArrayList<FieldDefinition> fieldDefinitions = parseFieldDefinitionBlock();

        readToken(TokenType.RIGHT_BRACE);

        resource = new Resource(tagSentence, fieldDefinitions);
        return resource;
    }

    private TagSentence parseTagSentence() throws Exception {
        readToken(TokenType.TAG);
        readToken(TokenType.ASSIGN_OPERATOR);
        readToken(TokenType.IDENT);

        TagSentence tagSentence = new TagSentence(currentToken.getContent());

        readToken(TokenType.SEMI_COLON);

        return tagSentence;
    }

    private ArrayList<Condition> parseConditions() throws Exception {
        readToken(TokenType.LEFT_BRACE);
        ArrayList<Condition> conditions = new ArrayList<>();

        readToken(TokenType.IF, TokenType.RIGHT_BRACE);

        while (currentToken.getType() == TokenType.IF)
        {
            conditions.add(parseConditionSentence());
            readToken(TokenType.IF, TokenType.RIGHT_BRACE);
        }

        readToken(TokenType.RIGHT_BRACE);

        return conditions;
    }

    private Condition parseConditionSentence() throws Exception {
        readToken(TokenType.LEFT_ROUND_BRACKET, TokenType.THIS, TokenType.PARENT, TokenType.CHILD);

        Condition condition;

        if(currentToken.getType() == TokenType.LEFT_ROUND_BRACKET)
        {
            ComplexCondition complexCondition = new ComplexCondition();

            while (currentToken.getType() != TokenType.SEMI_COLON) {
                complexCondition.addCondition(parseCondition());

                readToken(TokenType.RIGHT_ROUND_BRACKET);

                readToken(TokenType.OR, TokenType.AND, TokenType.SEMI_COLON);

                if(currentToken.getType() != TokenType.SEMI_COLON)
                {
                    complexCondition.addOperators(currentToken.getType());
                    readToken(TokenType.LEFT_BRACE);
                }
            }

            condition = complexCondition;
        }
        else
        {
            condition = parseCondition();
        }

        if(currentToken.getType() != TokenType.SEMI_COLON)
            readToken(TokenType.SEMI_COLON);

        return condition;
    }

    private SimpleCondition parseCondition() throws Exception {
        PathInCondition pathInCondition = parsePath();

        if(currentToken.getType() != TokenType.HAS)
            readToken(TokenType.HAS);

        Term term = parseTerm();

        return new SimpleCondition(pathInCondition, term);
    }

    private Term parseTerm() throws Exception {
        readToken(TokenType.TAG, TokenType.ATTRIBUTE, TokenType.CLASS, TokenType.NO);

        boolean isNegated = false;
        TokenType comparisonOperator;

        if(currentToken.getType() == TokenType.NO)
        {
            readToken(TokenType.TAG, TokenType.ATTRIBUTE, TokenType.CLASS);
            isNegated = true;
        }

        TokenType subject = currentToken.getType();
        readToken(TokenType.EQUAL, TokenType.NOT_EQUAL);
        comparisonOperator = currentToken.getType();

        List<String> valueSet = parseValueSet();

        return new Term(subject, isNegated, comparisonOperator, valueSet);
    }

    private ArrayList<String> parseValueSet() throws Exception {
        ArrayList<String> values = new ArrayList<>();

        readToken(TokenType.LEFT_BRACE, TokenType.STRING);

        if(currentToken.getType() == TokenType.STRING)
        {
            values.add(currentToken.getContent());
        }
        else
        {
            readToken(TokenType.STRING);

            while (currentToken.getType() == TokenType.STRING)
            {
                    values.add(currentToken.getContent());
            }
            readToken(TokenType.RIGHT_BRACE);
        }
        return values;
    }

    private PathInCondition parsePath() throws Exception {
        PathInCondition path = new PathInCondition();

        readToken(TokenType.THIS, TokenType.PARENT, TokenType.CHILD);

        if(currentToken.getType() == TokenType.THIS)
        {
            path.addNode(TokenType.THIS);
        }
        else if(currentToken.getType() == TokenType.PARENT)
        {
            while (currentToken.getType() == TokenType.PARENT)
            {
                path.addNode(TokenType.PARENT);
                readToken(TokenType.DOT, TokenType.HAS);

                if(currentToken.getType() == TokenType.HAS)
                    break;

                readToken(TokenType.PARENT);
            }
        }
        else if(currentToken.getType() == TokenType.CHILD)
        {
            while (currentToken.getType() == TokenType.CHILD)
            {
                path.addNode(TokenType.CHILD);
                readToken(TokenType.DOT, TokenType.HAS);

                if(currentToken.getType() == TokenType.HAS)
                    break;

                readToken(TokenType.CHILD);
            }
        }

        return path;
    }

    private ClassName parseClassLine() throws Exception {
        ClassName className = null;

        readToken(TokenType.EXPORT);
        readToken(TokenType.TO);
        readToken(TokenType.CLASS);
        readToken(TokenType.ASSIGN_OPERATOR);
        readToken(TokenType.IDENT);

        className = new ClassName(currentToken.getContent());

        readToken(TokenType.SEMI_COLON);

        return className;
    }

    private FieldDefinition parseFieldDefinition() throws Exception {
        FieldDefinition fieldDefinition = null;
        String fieldName;

        readToken(TokenType.IDENT);

        fieldName = currentToken.getContent();

        Pair parsedTypeAndPathToResource = parseTypeAndPathToResource();

        readToken(TokenType.SEMI_COLON);

        fieldDefinition = new FieldDefinition(fieldName, parsedTypeAndPathToResource.first, parsedTypeAndPathToResource.second);

        return fieldDefinition;
    }

    private ArrayList<FieldDefinition> parseFieldDefinitionBlock() throws Exception {
        readToken(TokenType.SET);
        readToken(TokenType.FIELDS);
        readToken(TokenType.LEFT_BRACE);

        readToken(TokenType.FIELD);

        ArrayList<FieldDefinition> fieldDefinitionsList = new ArrayList<>();

        while (currentToken.getType() == TokenType.FIELD)
        {
            fieldDefinitionsList.add(parseFieldDefinition());
            readToken(TokenType.FIELD, TokenType.RIGHT_BRACE);
        }

        return fieldDefinitionsList;

    }

    private class Pair {
        public PathToResource first;
        public FieldContent second;

        Pair(PathToResource first, FieldContent second)
        {
            this.first = first;
            this.second = second;
        }
    }

    private Pair parseTypeAndPathToResource() throws Exception {
        PathToResource path = new PathToResource();

        readToken(TokenType.FROM);
        readToken(TokenType.THIS, TokenType.TAG);

        switch (currentToken.getType()) {
            case THIS:
                path.addNodeToPath("this", 0);

                
                break;

            case TAG:
                while (currentToken.getType() == TokenType.TAG) {
                    readToken(TokenType.IDENT);
                    String tagName = currentToken.getContent();
                    
                    readToken(TokenType.NUMBER);
                    String tagNumber = currentToken.getContent();
                    
                    path.addNodeToPath(tagName, Integer.parseInt(tagNumber));
                    
                    readToken(TokenType.DOT);
                    readToken(TokenType.TAG, TokenType.TEXT, TokenType.ATTRIBUTE);
                }
                break;
        }

        FieldContent fieldContent = parseFieldContent();

        return new Pair(path, fieldContent);
    }

    private FieldContent parseFieldContent() throws Exception {
        switch (currentToken.getType())
        {
            case ATTRIBUTE:
                readToken(TokenType.IDENT);
                String attributeName = currentToken.getContent();
                readToken(TokenType.DOT);
                readToken(TokenType.IMG, TokenType.TEXT);

                if (currentToken.getType() == TokenType.IMG)
                    return new FieldContent(attributeName, TokenType.IMG);
                else if(currentToken.getType() == TokenType.TEXT)
                    return new FieldContent(attributeName, TokenType.TEXT);
                else
                    throw new Exception("exception");

            case TEXT:
                return new FieldContent(TokenType.TEXT);

            default:
                throw new Exception("exception");
        }

    }

    private AmountSentence parseAmountSentence() throws Exception {
        AmountSentence amountSentence = null;

        readToken(TokenType.AMOUNT);
        readToken(TokenType.ASSIGN_OPERATOR);
        readToken(TokenType.NUMBER, TokenType.EVERY);

        switch (currentToken.getType())
        {
            case NUMBER:
                amountSentence = new AmountSentence(Integer.parseInt(currentToken.getContent()));
                break;

            case EVERY:
                amountSentence = new AmountSentence(true);
                break;
        }

        readToken(TokenType.SEMI_COLON);

        return amountSentence;
    }

    private void readToken(TokenType... tokenType) throws Exception {
        currentToken = lexer.getNextToken();

        boolean throwException = true;

        for(TokenType t : tokenType)
            if (currentToken.getType() == t) {
                throwException = false;
                break;
            }

        if(throwException)
            throw new Exception("");
    }
}