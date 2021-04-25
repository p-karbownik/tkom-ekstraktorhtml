package Lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class Lexer {
    private final static int startRow = 0;
    private final static int startColumn = 1;

    private final BufferedReader inputReader;
    private int currentCharacter;
    private HashMap<String, TokenType> keyWords;
    private final Position inputReaderPosition;

    public Lexer(BufferedReader inputReader) throws IOException {
        this.inputReader = inputReader;
        initialiseKeyWords();
        inputReaderPosition = new Position(startRow,startColumn);
        readCharacter();
    }

    public Token getNextToken() throws Exception {
        Token nextToken = null;
        boolean readNextCharacter = true;
        while (isWhiteSpace(currentCharacter))
            readCharacter();

        if(currentCharacter == -1)
            throw new Exception();

        if(currentCharacter == '[')
        {
            readCharacter();
            nextToken = buildStringOrNumberToken();
        }
        else if(currentCharacter == ']')
            throw new Exception(); // <--- ten znak nie powinien się pojawić w innym kontekście, niż opakowanie stringa
        else if(currentCharacter == '{')
            nextToken = new Token(TokenType.LEFT_BRACE, inputReaderPosition);
        else if(currentCharacter == '}')
            nextToken = new Token(TokenType.RIGHT_BRACE, inputReaderPosition);
        else if (currentCharacter == '.')
            nextToken = new Token(TokenType.DOT, inputReaderPosition);
        else if(currentCharacter == '=')
        {
            Position tokenBeginPosition = (Position) inputReaderPosition.clone();
            readCharacter();

            if(currentCharacter == '=')
                nextToken = new Token(TokenType.EQUAL, tokenBeginPosition);

            else {
                nextToken = new Token(TokenType.ASSIGN_OPERATOR, tokenBeginPosition);
                readNextCharacter = false;
            }
        }
        else if(currentCharacter == '!')
        {
            Position tokenBeginPosition = (Position) inputReaderPosition.clone();
            readCharacter();
            readNextCharacter = false;
            if(currentCharacter == '=')
                nextToken = new Token(TokenType.NOT_EQUAL, tokenBeginPosition);
            else
                throw new Exception();
        }
        else if(currentCharacter == ';')
            nextToken = new Token(TokenType.SEMI_COLON, inputReaderPosition);
        else if(currentCharacter == '(')
            nextToken = new Token(TokenType.LEFT_ROUND_BRACKET, inputReaderPosition);
        else if (currentCharacter == ')')
            nextToken = new Token(TokenType.RIGHT_ROUND_BRACKET, inputReaderPosition);
        //skoro nie jest znakiem specjalnym, to moze być słowem kluczowym
        if(nextToken == null)
        {
            nextToken = buildKeyWordToken();
            readNextCharacter = false;
        }

        if(readNextCharacter)
            readCharacter();

        return nextToken;
    }

    private void readCharacter() throws IOException {
        if(currentCharacter == '\n')
        {
            inputReaderPosition.setNextRow();
            inputReaderPosition.setColumn(startColumn);
        }
        else
            inputReaderPosition.setNextColumn();

        currentCharacter = inputReader.read();
    }

    private Token buildKeyWordToken() throws Exception {
        StringBuilder keyWord = new StringBuilder();
        Position tokenBeginPosition = (Position) inputReaderPosition.clone();

        while (!isWhiteSpace(currentCharacter) && currentCharacter != -1 && !isOperatorCharacter(currentCharacter))
        {
            keyWord.append((char)currentCharacter);
            readCharacter();
        }
        //zlapalismy slowo
        //teraz czas sprawdzic, czy ono rzeczywiscie istnieje

        if(keyWords.containsKey(keyWord.toString()))
        {
            return new Token(keyWords.get(keyWord.toString()), tokenBeginPosition);
        }

        //wyrzuc wyjatek

        throw new Exception();
    }

    private Token buildStringOrNumberToken() throws Exception {
        StringBuilder content = new StringBuilder();
        Position tokenBeginPosition = (Position) inputReaderPosition.clone();
        boolean isString = false;
        boolean isThatFirstCharacter = true;

        while (isStringCharacter(currentCharacter))
        {
            if(!isDigit(currentCharacter))
                isString = true;

            if(isThatFirstCharacter)
            {
                isThatFirstCharacter = false;
                if (currentCharacter == '0')
                    isString = true;
            }

            content.append((char) currentCharacter);
            readCharacter();
        }

        if(!isWhiteSpace(currentCharacter) && currentCharacter != ']')
            throw new Exception();

        if(isString)
            return new Token(TokenType.STRING, content.toString(), tokenBeginPosition);
        else
            return new Token(TokenType.NUMBER, content.toString(), tokenBeginPosition);

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

    private boolean isDigit(int character)
    {
        return '0' <= character && character <= '9';
    }

    private boolean isStringCharacter(int character)
    {
        return ('A' <= character && character <= 'Z') ||
                ('a' <= character && character <= 'z')
                || character == ' ' || character == '$' || character == '_'
                || character == '.' || character == '-' || isDigit(character);
    }

    private boolean isOperatorCharacter(int character)
    {
        return character == '=' || character == '!' || character == '.' || character == '[' || character == ']' || character == ';'
                || character == '(' || character == ')';
    }
}