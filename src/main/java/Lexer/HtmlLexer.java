package Lexer;

import java.io.IOException;
import java.util.HashMap;

import exceptions.LexerException;
import Reader.*;

public class HtmlLexer {
    private final Reader reader;
    private HashMap<String, TokenType> keyWords;

    public HtmlLexer(Reader reader) throws IOException {
        this.reader = reader;
        initialiseKeyWords();
        reader.readCharacter();
    }

    public Token getNextToken() throws IOException, LexerException{
        Token nextToken = null;
        StringBuilder content = new StringBuilder();
        Position startPosition = reader.getCurrentPosition();

        while (isWhiteSpace(reader.getCurrentCharacter()))
            reader.readCharacter();

        if(reader.getCurrentCharacter() == 3)
            nextToken = new Token(TokenType.ETX);

        if(nextToken == null)
            nextToken = buildKeyWordToken(content, startPosition);

        if(nextToken == null)
        {
            nextToken = buildHTMLtextToken(content, startPosition);
        }

        if(nextToken == null)
            throw new LexerException("Cannot build token at position: row " + startPosition.getRow() + " column " + startPosition.getColumn() + "with content: " + content.toString());
        return nextToken;
    }

    private Token buildHTMLtextToken(StringBuilder content, Position position) throws IOException {
        char character = reader.getCurrentCharacter();

        while(character != 3 && !isWhiteSpace(character) && !isKeyWordCharacter(character)) //przemyslec podejscie do namespacow
        {
            content.append(character);
            reader.readCharacter();
            character = reader.getCurrentCharacter();
        }

        return new Token(TokenType.HTML_TEXT, content.toString(), position);
    }

    private Token buildKeyWordToken(StringBuilder content, Position tokenBeginPosition) throws IOException {

        if(!isKeyWordCharacter(reader.getCurrentCharacter()))
            return null;

        char character = reader.getCurrentCharacter();
        content.append(character);

        if(character == '<')
        {
            reader.readCharacter();
            character = reader.getCurrentCharacter();

            if(character == '!' || character == '/') // rozlaczyc ten warunek na oddzielne ify, wtedy nie trzeba hashMapy na to
            {
                content.append(character);
                reader.readCharacter();
                character = reader.getCurrentCharacter();

                if(character == '-')
                {
                    content.append(character);
                    reader.readCharacter();
                    character = reader.getCurrentCharacter();

                    if(character == '-')
                    {
                        content.append(character);
                        reader.readCharacter();
                    }
                    else
                        return new Token(TokenType.HTML_TEXT, content.toString(), tokenBeginPosition);
                }
            }
            return new Token(keyWords.get(content.toString()), content.toString(), tokenBeginPosition);
        }

        else if(character == '\"' || character == '=' || character == '>') //
        {
            reader.readCharacter();
            return new Token(keyWords.get(content.toString()), content.toString(), tokenBeginPosition);
        }

        else if(character == '/')
        {
            reader.readCharacter();
            character = reader.getCurrentCharacter();

            if(character == '>')
            {
                content.append(character);
                reader.readCharacter();
                return new Token(keyWords.get(content.toString()), content.toString(), tokenBeginPosition);
            }

            return new Token(TokenType.HTML_TEXT, content.toString(), tokenBeginPosition);
        }

        else if(character == '-')
        {
            reader.readCharacter();
            character = reader.getCurrentCharacter();

            if(character == '-')
            {
                content.append(character);
                reader.readCharacter();
                character = reader.getCurrentCharacter();

                if(character == '>')
                {
                    content.append(character);
                    reader.readCharacter();

                    return new Token(keyWords.get(content.toString()), content.toString(), tokenBeginPosition);
                }
                else
                    return new Token(TokenType.HTML_TEXT, content.toString(), tokenBeginPosition);
            }

            return new Token(TokenType.HTML_TEXT, content.toString(), tokenBeginPosition);
        }
        else
            return null;
    }

    private boolean isWhiteSpace(char character)
    {
        return character == ' ' || character == '\t'
                || character == '\n' || character == '\r';
    }

    private void initialiseKeyWords() {
        keyWords = new HashMap<>();

        keyWords.put("<", TokenType.TAG_OPENER);
        keyWords.put("\"", TokenType.QUOTE);
        keyWords.put("'", TokenType.SINGLE_QUOTE);
        keyWords.put("<!", TokenType.DOCTYPE);
        keyWords.put("=", TokenType.ASSIGN_OPERATOR);
        keyWords.put("/>", TokenType.EMPTY_CLOSING_TAG);
        keyWords.put(">", TokenType.TAG_CLOSING_MARK);
        keyWords.put("</", TokenType.CLOSING_TAG);
        keyWords.put("<!--", TokenType.COMMENT_TAG_OPENER);
        keyWords.put("-->", TokenType.COMMENT_TAG_CLOSING);
    }

    private boolean isKeyWordCharacter(char character) {
        return character == '!' || character == '=' || character == '\'' || character == '<' || character == '\"' || character == '>'
                || character == '/' || character == '-';
    }

}
