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

    public Resource parseResource() throws Exception {
        Resource resource = null;
        TagSentence tagSentence = null;

        readToken(TokenType.RESOURCE);
        readToken(TokenType.IDENTIFIER);

        Token identifier = currentToken;

        DefinitionBlock definitionBlock = parseDefinitionBlock();
        resource = new Resource(currentToken.getContent(), definitionBlock);

        return resource;
    }

    private DefinitionBlock parseDefinitionBlock() throws Exception
    {
        TagSentence tagSentence = parseTagSentence();

        readToken(TokenType.CONDITIONS, TokenType.CLASS);

        ConditionsBlock conditionsBlock = null;

        if(currentToken.getType() == TokenType.CONDITIONS)
            conditionsBlock = parseConditionsBlock();

        ClassLine classLine = parseClassLine();

        parseSetFields();

        FieldsDefinitionBlock fieldsDefinitionBlock = parseFieldsDefinitionBlock();
        AmountSentence amountSentence = parseAmountSentence();

        return DefinitionBlock(tagSentence, conditionsBlock, classLine, fieldsDefinitionBlock, amountSentence);
    }

    private TagSentence parseTagSentence() throws Exception {
        readToken(TokenType.TAG);
        readToken(TokenType.ASSIGN_OPERATOR);
        readToken(TokenType.IDENTIFIER);

        TagSentence tagSentence = new TagSentence(currentToken.getContent());

        readToken(TokenType.SEMI_COLON);

        return tagSentence;
    }

    private ConditionsBlock parseConditionsBlock() throws Exception
    {
        readToken(TokenType.LEFT_BRACE);

        ArrayList<ConditionSentence> conditionSentences = new ArrayList<>();

        ConditionSentence conditionSentence = null;

        do{
            conditionSentence = parseConditionSentence();

            if(conditionSentence != null)
                conditionSentences.add(conditionSentence);

        } while (conditionSentence != null);

        readToken(TokenType.RIGHT_BRACE);

        return new ConditionsBlock(/*conditionSentences*/);
    }

    private ConditionSentence parseConditionSentence() throws Exception {
        readToken(TokenType.IF);

        ArrayList<Condition> conditions = new ArrayList<>();

        Condition condition = null;

        condition = parseCondition();

        /*
            if(condition == null)
                throwException
         */
        do {
            conditions.add(condition);
        } while (condition != null);

        readToken(TokenType.SEMI_COLON);

        return new ConditionSentence(/* condition*/);
    }

    private Condition parseCondition() throws Exception {
        ArrayList<Term> terms = new ArrayList<>();
        Term term = parseTerm();

        //readToken(...);

        terms.add(term);

        while (currentToken.getType() == TokenType.OR)
        {
            term = parseTerm();
            terms.add(term);
            //readToken(...);
        }

        return new Condition(/* terms */);
    }

    private Term parseTerm() throws Exception {
        ArrayList<Factor> factors = new ArrayList<>();
        Factor factor = parseFactor();

        //readToken(...);

        factors.add(factor);

        while (currentToken.getType() == TokenType.AND)
        {
            factor = parseFactor();
            factors.add(factor);
            //readToken(...);
        }

        return new Term(/* factors */);
    }

    private Factor parseFactor() throws Exception
    {
        Path path = parsePath();

        if(path == null)
        {
            readToken(TokenType.LEFT_ROUND_BRACKET);

            Condition condition = parseCondition();

            readToken(TokenType.RIGHT_ROUND_BRACKET);

            return new Factor(/* condition */);
        }

        else {
            readToken(TokenType.HAS);
            FactorObject factorObject = parseFactorObject();

            return new Factor(/* factorObject */);
        }
    }

    private FactorObject parseFactorObject()
    {

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
                    readToken(/* COMMA, RIGHT_BRACE */ );

                    /*
                    if(currentToken.getType == COMMA)
                        readToken(TokenType.STRING);
                    */
            }

            readToken(TokenType.RIGHT_BRACE);
        }
        return values;
    }

    private Path parsePath() throws Exception {
        Path path = new Path();

        readToken(TokenType.SELF, TokenType.PARENT, TokenType.CHILD);

        if(currentToken.getType() == TokenType.SELF)
        {
            //path.addNode(TokenType.SELF);
        }
        else if(currentToken.getType() == TokenType.PARENT)
        {
            while (currentToken.getType() == TokenType.PARENT)
            {
                //path.addNode(TokenType.PARENT);
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
                //path.addNode(TokenType.CHILD);
                readToken(TokenType.DOT, TokenType.HAS);

                if(currentToken.getType() == TokenType.HAS)
                    break;

                readToken(TokenType.CHILD);
            }
        }

        return path;
    }

    private ClassLine parseClassLine() throws Exception {
        ClassLine className = null;

        readToken(TokenType.EXPORT);
        readToken(TokenType.TO);
        readToken(TokenType.CLASS);
        readToken(TokenType.ASSIGN_OPERATOR);
        readToken(TokenType.IDENTIFIER);

        className = new ClassLine();//(currentToken.getContent());

        readToken(TokenType.SEMI_COLON);

        return className;
    }

    private FieldDefinition parseFieldDefinition() throws Exception {
        FieldDefinition fieldDefinition = null;
        String fieldName;

        readToken(TokenType.IDENTIFIER);

        fieldName = currentToken.getContent();

        Pair parsedTypeAndPathToResource = parseTypeAndPathToResource();

        readToken(TokenType.SEMI_COLON);

        fieldDefinition = new FieldDefinition(fieldName, parsedTypeAndPathToResource.first, parsedTypeAndPathToResource.second);

        return fieldDefinition;
    }

    private ArrayList<FieldDefinition> parseFieldsDefinitionBlock() throws Exception {
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
        readToken(TokenType.SELF, TokenType.TAG);

        switch (currentToken.getType()) {
            case SELF:
                path.addNodeToPath("this", 0);

                
                break;

            case TAG:
                while (currentToken.getType() == TokenType.TAG) {
                    readToken(TokenType.IDENTIFIER);
                    String tagName = currentToken.getContent();
                    
                    readToken(TokenType.NUMBER);
                    String tagNumber = currentToken.getContent();
                    
                    path.addNodeToPath(tagName, Integer.parseInt(tagNumber));
                    
                    readToken(TokenType.DOT);
                    //readToken(TokenType.TAG, TokenType.TEXT, TokenType.ATTRIBUTE);
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
                readToken(TokenType.IDENTIFIER);
                String attributeName = currentToken.getContent();
                readToken(TokenType.DOT);
                //readToken(TokenType.ASIMG, TokenType.TEXT);

                if (currentToken.getType() == TokenType.ASIMG)
                    return new FieldContent(attributeName, TokenType.ASIMG);
               // else if(currentToken.getType() == TokenType.TEXT)
                   // return new FieldContent(attributeName, TokenType.TEXT);
                else
                    throw new Exception("exception");

           // case TEXT:
              //  return new FieldContent(TokenType.TEXT);

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