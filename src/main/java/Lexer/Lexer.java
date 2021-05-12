package Lexer;

import Exceptions.UnrecognisedTokenException;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class Lexer {
    private final static int startRow = 1;
    private final static int startColumn = 0;
    private final static int tabLength = 8;

    private final BufferedReader inputReader;
    private int currentCharacter;
    private HashMap<String, TokenType> keyWords;
    private HashMap<String, TokenType> operators;
    private final Position inputReaderPosition;

    public Lexer(BufferedReader inputReader) throws IOException {
        this.inputReader = inputReader;
        initialiseKeyWords();
        initialiseOperators();
        inputReaderPosition = new Position(startRow,startColumn);
        readCharacter();
    }

    public Token getNextToken() throws Exception {
        Token nextToken = buildToken();
        readCharacter();

        return nextToken;
    }

    private Token buildToken() throws IOException, CloneNotSupportedException, UnrecognisedTokenException {
        StringBuilder content = new StringBuilder();
        Position startPosition = (Position) inputReaderPosition.clone();

        while (isWhiteSpace(currentCharacter))
            readCharacter();

        if(currentCharacter == '[')
        {
            readCharacter();
            while (isStringCharacter(currentCharacter))
            {
                content.append((char) currentCharacter);
                readCharacter();
            }
            System.out.println(content);
            if(currentCharacter != ']')
                throw new UnrecognisedTokenException(startPosition);

            if(isNumber(content.toString()))
                return new Token(TokenType.NUMBER, content.toString(), startPosition);
            else
                return new Token(TokenType.STRING, content.toString(), startPosition);
        }
        else
        {
            if(isStringCharacter(currentCharacter)) {
                while (isStringCharacter(currentCharacter)) {
                    content.append((char) currentCharacter);
                    readCharacter();
                }
            }
            else
            {
                while (isOperatorCharacter(currentCharacter))
                {
                    content.append((char) currentCharacter);
                    readCharacter();
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
        System.out.println(content);
        if(tokenType == null)
            return null;

        return new Token(tokenType, tokenBeginPosition);
    }

    //TO:DO dopisać testy do tej metody
    private void readCharacter() throws IOException {
        if(currentCharacter == '\n') //rozbudowac o pozostale przypadki
        {
            inputReaderPosition.setNextRow();
            inputReaderPosition.setColumn(startColumn);
        }
        else if(currentCharacter == '\t')
        {
            inputReaderPosition.increaseColumn(tabLength);
        }
        else if( currentCharacter == '\r')
        {
            inputReaderPosition.setColumn(startColumn);
        }
        else
            inputReaderPosition.setNextColumn();

        currentCharacter = inputReader.read();
    }

    private Token buildKeyWordToken(String content, Position tokenBeginPosition) {
        TokenType tokenType = keyWords.get(content);
        System.out.println(content);
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
            || character == '}' || character == '(';
    }

    private boolean isStringCharacter(int character)
    {
        return ('A' <= character && character <= 'Z') ||
                ('a' <= character && character <= 'z')
                || character == ' ' || character == '$' || character == '_'
                || character == '.' || character == '-' || isDigit(character);
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