package Lexer;

import Exceptions.UnrecognisedTokenException;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class Lexer {
    private Reader reader;
    private HashMap<String, TokenType> keyWords;
    private HashMap<String, TokenType> operators;

    public Lexer(Reader reader) throws IOException {
        this.reader = reader;
        initialiseKeyWords();
        initialiseOperators();
        reader.readCharacter();
    }

    public Token getNextToken() throws Exception {
        Token nextToken = buildToken();
        reader.readCharacter();

        return nextToken;
    }

    private Token buildToken() throws IOException, CloneNotSupportedException, UnrecognisedTokenException {
        StringBuilder content = new StringBuilder();
        Position startPosition = (Position) reader.getCurrentPosition().clone();

        while (isWhiteSpace(reader.getCurrentCharacter()))
            reader.readCharacter();

        if(reader.getCurrentCharacter() == '[')
        {
            reader.readCharacter();
            while (isStringCharacter(reader.getCurrentCharacter()))
            {
                content.append(reader.getCurrentCharacter());
                reader.readCharacter();
            }

            if(reader.getCurrentCharacter() != ']')
                throw new UnrecognisedTokenException(startPosition);

            return new Token(TokenType.STRING, content.toString(), startPosition);
        }
        else if(reader.getCurrentCharacter() == '<')
        {
            reader.readCharacter();
            while (isStringCharacter(reader.getCurrentCharacter()))
            {
                content.append(reader.getCurrentCharacter());
                reader.readCharacter();
            }

            if(reader.getCurrentCharacter() != '>')
                throw new UnrecognisedTokenException(startPosition);

            return new Token(TokenType.NUMBER, content.toString(), startPosition);
        }
        else
        {
            if(isKeyWordCharacter(reader.getCurrentCharacter())) {
                while (isKeyWordCharacter(reader.getCurrentCharacter())) {
                    content.append(reader.getCurrentCharacter());
                    reader.readCharacter();
                }
                reader.blockNextReading();
            }
            else
            {
                char character = reader.getCurrentCharacter();

                if(character == '(' || character == ')' || character == '{' || character == '}'
                || character == ';' || character == '.')
                {
                    content.append(character);
                }
                else
                {
                    content.append(reader.getCurrentCharacter());
                    reader.readCharacter();
                    character = reader.getCurrentCharacter();

                    if(character == '=')
                    {
                        content.append(character);
                    }
                    else
                    {
                        reader.blockNextReading();
                    }
                }
            }

            Token token = buildKeyWordToken(content.toString(), startPosition);

            if(token == null)
                token = buildOperatorToken(content.toString(), startPosition);

            return token;
        }
    }

    private Token buildOperatorToken(String content, Position tokenBeginPosition) {
        TokenType tokenType = operators.get(content);
        if(tokenType == null)
            return null;

        return new Token(tokenType, tokenBeginPosition);
    }

    //TO:DO dopisać testy do tej metody

    private Token buildKeyWordToken(String content, Position tokenBeginPosition) {
        TokenType tokenType = keyWords.get(content);
        if(tokenType == null)
            return null;

        return new Token(tokenType, tokenBeginPosition);
    }

    private boolean isWhiteSpace(int character)
    {
        return character == ' ' || character == '\t'
                || character == '\n' || character == '\r';
    }

    private void initialiseKeyWords()
    {
        keyWords = new HashMap<>();

        keyWords.put("resource", TokenType.RESOURCE);
        keyWords.put("tag", TokenType.TAG);
        keyWords.put("conditions", TokenType.CONDITIONS);
        keyWords.put("if", TokenType.IF);
        keyWords.put("parent", TokenType.PARENT);
        keyWords.put("child", TokenType.CHILD);
        keyWords.put("has", TokenType.HAS);
        keyWords.put("class", TokenType.CLASS);
        keyWords.put("attribute", TokenType.ATTRIBUTE);

        keyWords.put("or", TokenType.OR);
        keyWords.put("and", TokenType.AND);
        keyWords.put("export", TokenType.EXPORT);
        keyWords.put("to", TokenType.TO);
        keyWords.put("set", TokenType.SET);
        keyWords.put("fields", TokenType.FIELDS);
        keyWords.put("text", TokenType.TEXT);
        keyWords.put("img", TokenType.IMG);
        keyWords.put("every", TokenType.EVERY);
        keyWords.put("from", TokenType.FROM);
        keyWords.put("this", TokenType.THIS);
        keyWords.put("amount", TokenType.AMOUNT);

        keyWords.put("no", TokenType.NO);
        keyWords.put("field", TokenType.FIELD);
    }

    private void initialiseOperators()
    {
        operators = new HashMap<>();

        operators.put("!=", TokenType.NOT_EQUAL);
        operators.put("==", TokenType.EQUAL);
        operators.put("=", TokenType.ASSIGN_OPERATOR);
        operators.put(".", TokenType.DOT);
        operators.put(")", TokenType.RIGHT_ROUND_BRACKET);
        operators.put("{", TokenType.LEFT_BRACE);
        operators.put("}", TokenType.RIGHT_BRACE);
        operators.put(";", TokenType.SEMI_COLON);
        operators.put("(", TokenType.LEFT_ROUND_BRACKET);
    }

    private boolean isDigit(int character)
    {
        return '0' <= character && character <= '9';
    }

    private boolean isOperatorCharacter(int character) {
        return character == '!' || character == '=' || character == ')' || character == '{' || character == ';'
            || character == '}' || character == '(' || character == '.';
    }

    private boolean isStringCharacter(int character)
    {
        return ('A' <= character && character <= 'Z') ||
                ('a' <= character && character <= 'z')
                || character == ' ' || character == '$' || character == '_'
                || character == '.' || character == '-' || isDigit(character);
    }

    private boolean isKeyWordCharacter(int character)
    {
        return ('a' <= character && character <= 'z');
    }

    private boolean isNumber(String content)
    {
        for(char ch : content.toCharArray())
        {
            if(!isDigit(ch))
                return false;
        }
        return true;
    }
}