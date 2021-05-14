package Lexer;

import Exceptions.UnrecognisedTokenException;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

//TO:DO wydzielenie czesci wspolnych lexerow do klasy zewnetrznej

public class HtmlLexer {
    private final static int startRow = 1;
    private final static int startColumn = 0;
    private final static int tabLength = 8;

    private final BufferedReader inputReader;
    private int currentCharacter;
    private HashMap<String, TokenType> keyWords;
    private final Position inputReaderPosition;

    public HtmlLexer(BufferedReader inputReader) throws IOException {
        this.inputReader = inputReader;
        initialiseKeyWords();
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

        if(isKeyWordCharacter(currentCharacter))
        {
            while (isKeyWordCharacter(currentCharacter))
            {
                content.append((char) currentCharacter);
                readCharacter();
            }

            return buildKeyWordToken(content.toString(), startPosition);

        }
        //TO DO: oprogramowanie reszty
        return null;
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

        keyWords.put("<", TokenType.TAG_OPENER);
        keyWords.put("\"", TokenType.QUOTE);
        keyWords.put("<!", TokenType.DOCTYPE);
        keyWords.put("=", TokenType.ASSIGN_OPERATOR);
        keyWords.put("/>", TokenType.EMPTY_CLOSING_TAG);
        keyWords.put(">", TokenType.TAG_CLOSING_MARK);
        keyWords.put("</", TokenType.CLOSING_TAG);
    }

    private boolean isKeyWordCharacter(int character) {
        return character == '!' || character == '=' || character == '<' || character == '\"' || character == '>'
                || character == '/';
    }

    private boolean isStringCharacter(int character)
    {
        return ('A' <= character && character <= 'Z') ||
                ('a' <= character && character <= 'z')
                || character == ' ' || character == '$' || character == '_'
                || character == '.' || character == '-';
    }

}
