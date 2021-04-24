package Lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class Lexer {

    private BufferedReader inputReader;
    private int currentCharacter;
    private HashMap<String, TokenType> keyWords;

    public Lexer(BufferedReader inputReader) throws IOException {
        this.inputReader = inputReader;
        initialiseKeyWords();
        readCharacter();
    }

    public Token getNextToken() throws Exception {
        Token nextToken = null;

        while (isWhiteSpace(currentCharacter))
            readCharacter();

        //sprawdzanie czy jest znakiem specjalnym
        if(currentCharacter == '[')
        {
            readCharacter();

            if(isDigit(currentCharacter))
                nextToken = buildNumberToken();
            else
                nextToken = buildStringToken();
        }
        else if(currentCharacter == ']')
            throw new Exception();
        else if(currentCharacter == '{')
            nextToken = new Token(TokenType.LEFT_BRACE);
        else if(currentCharacter == '}')
            nextToken = new Token(TokenType.RIGHT_BRACE);

        //skoro nie jest znakiem specjalnym, to moze być słowem kluczowym
        if(nextToken == null)
            nextToken = buildKeyWordToken();

        readCharacter();

        return nextToken;
    }

    private void readCharacter() throws IOException {
        currentCharacter = inputReader.read();
    }

    private Token buildKeyWordToken() throws Exception {
        StringBuilder keyWord = new StringBuilder();

        while (!isWhiteSpace(currentCharacter))
        {
            keyWord.append(currentCharacter);
            readCharacter();
        }

        //zlapalismy slowo
        //teraz czas sprawdzic, czy ono rzeczywiscie istnieje

        if(keyWords.containsKey(keyWord.toString()))
        {
            return new Token(keyWords.get(keyWord.toString()));
        }

        //wyrzuc wyjatek

        throw new Exception();
    }

    private Token buildNumberToken() throws Exception {
        StringBuilder number = new StringBuilder();

        while (isDigit(currentCharacter))
        {
            number.append(currentCharacter);
            readCharacter();
        }

        if(!isWhiteSpace(currentCharacter) && currentCharacter != ']')
            throw new Exception();

        return new Token(TokenType.NUMBER, number.toString());
    }

    private Token buildStringToken() throws Exception {
        StringBuilder string = new StringBuilder();
        while (isStringCharacter(currentCharacter))
        {
            string.append((char) currentCharacter);
            readCharacter();
        }

        if(currentCharacter != ']')
            throw new Exception();

        return new Token(TokenType.STRING, string.toString());
    }

    private boolean isWhiteSpace(int character)
    {
        return character == ' ' || character == '\t'
                || character == '\n' || character == '\r';
    }

    private void initialiseKeyWords()
    {
        keyWords = new HashMap<String, TokenType>();

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

        keyWords.put("==", TokenType.EQUAL);
        keyWords.put("!=", TokenType.NOT_EQUAL);
        keyWords.put("=", TokenType.ASSIGN_OPERATOR);
        keyWords.put(".", TokenType.DOT);
        keyWords.put(";", TokenType.SEMI_COLON);
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
                || character == '.';
    }
}
