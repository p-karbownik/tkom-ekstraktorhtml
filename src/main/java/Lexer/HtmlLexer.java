package Lexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import exceptions.LexerException;
import Reader.*;

public class HtmlLexer {
    private final Reader reader;
    //private HashMap<String, TokenType> keyWords;
    private StringBuilder content;
    private Position position;
    private boolean attributeValueReadingMode = false;
    private boolean insideTagMode = false;
    private boolean textMode = false;

    public HtmlLexer(Reader reader) throws IOException {
        this.reader = reader;
        //initialiseKeyWords();
        reader.readCharacter();
    }

    public Token getNextToken() throws IOException, LexerException{
        Token nextToken = null;
        content = new StringBuilder();
        position = reader.getCurrentPosition();

        while (isWhiteSpace(reader.getCurrentCharacter()))
            reader.readCharacter();

        if(reader.getCurrentCharacter() == 3)
            nextToken = new Token(TokenType.ETX);

        if(attributeValueReadingMode)
        {
            nextToken = getHTMLTextToken('\"', '\'');
            attributeValueReadingMode = false;
        }
        if(nextToken == null) {
            nextToken = buildKeyWordToken();
        }
        if(nextToken == null)
        {
            if(insideTagMode)
                nextToken = buildHTMLtextTokenInsideTokenMode();
            else
                nextToken = buildHTMLtextToken();
        }

        if(nextToken == null)
            throw new LexerException("Cannot build token at position: row " + position.getRow() + " column " + position.getColumn() + "with content: " + content.toString());
        return nextToken;
    }

    public Token getHTMLTextToken(Character... endCharacters) throws IOException {
        char character = reader.getCurrentCharacter();

        StringBuilder content = new StringBuilder();

        List<Character> theCharacters = Arrays.asList(endCharacters);

        while (character != 3 && !theCharacters.contains(character))
        {
            content.append(character);
            reader.readCharacter();
            character = reader.getCurrentCharacter();
        }

        return new Token(TokenType.HTML_TEXT, content.toString());
    }

    private Token buildHTMLtextTokenInsideTokenMode() throws IOException {
        char character = reader.getCurrentCharacter();

        while(character != 3 && !isWhiteSpace(character) && !isKeyWordCharacter(character)) //przemyslec podejscie do namespacow
        {
            content.append(character);
            reader.readCharacter();
            character = reader.getCurrentCharacter();
        }

        return new Token(TokenType.HTML_TEXT, content.toString(), position);
    }

    private Token buildHTMLtextToken() throws IOException {
        char character = reader.getCurrentCharacter();

        while(character != 3 && !isKeyWordCharacter(character)) //przemyslec podejscie do namespacow
        {
            content.append(character);
            reader.readCharacter();
            character = reader.getCurrentCharacter();
        }

        return new Token(TokenType.HTML_TEXT, content.toString(), position);
    }
    private Token buildKeyWordToken() throws IOException {
        char character = reader.getCurrentCharacter();
        content.append(character);
        reader.readCharacter();

        if(character == '<')
        {
            character = reader.getCurrentCharacter();

            if(character == '!')
            {
                //content.append(character);
                reader.readCharacter();
                character = reader.getCurrentCharacter();

                if(character == '-')
                {
                    //content.append(character);
                    reader.readCharacter();
                    character = reader.getCurrentCharacter();

                    if(character == '-')
                    {
                        //content.append(character);
                        reader.readCharacter();

                        character = reader.getCurrentCharacter();

                        do {
                            if(character == '-')
                            {
                                reader.readCharacter();
                                character = reader.getCurrentCharacter();

                                if(character == '-')
                                {
                                    reader.readCharacter();
                                    character = reader.getCurrentCharacter();

                                    reader.readCharacter();
                                    if(character == '>')
                                    {
                                        return new Token(TokenType.COMMENT_TAG_OPENER, content.toString(), position);
                                    }
                                    else {
                                        character = reader.getCurrentCharacter();
                                    }
                                }
                                else
                                {
                                    reader.readCharacter();
                                    character = reader.getCurrentCharacter();
                                }

                            }
                            else {
                                reader.readCharacter();
                                character = reader.getCurrentCharacter();
                            }
                        } while (character != 3);
                    }
                    else
                        return new Token(TokenType.HTML_TEXT, content.toString(), position);
                }
                else
                    return new Token(TokenType.DOCTYPE, content.toString(), position);
            }
            else if(character == '/')
            {
                content.append(character);
                reader.readCharacter();

                return new Token(TokenType.CLOSING_TAG, content.toString(), position);
            }
            /*if(character == '!' || character == '/') // rozlaczyc ten warunek na oddzielne ify, wtedy nie trzeba hashMapy na to
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
                    {
                        reader.readCharacter();
                        return new Token(TokenType.HTML_TEXT, content.toString(), position);
                    }
                }
            }*/
            //return new Token(keyWords.get(content.toString()), content.toString(), position);
            return new Token(TokenType.TAG_OPENER, content.toString(), position);
        }
        else if(insideTagMode && character == '\'')
        {

            return new Token(TokenType.SINGLE_QUOTE, content.toString(), position);
        }
        else if(insideTagMode && character == '=')
        {
            return new Token(TokenType.ASSIGN_OPERATOR, content.toString(), position);
        }
        else if(insideTagMode && character == '\"')
        {
            return new Token(TokenType.QUOTE, content.toString(), position);
        }
        else if(character == '>')
        {
            return new Token(TokenType.TAG_CLOSING_MARK, content.toString(), position);
        }
        /*else if(character == '\"' || character == '=' || character == '>') //
        {
            reader.readCharacter();
            return new Token(keyWords.get(content.toString()), content.toString(), position);
        }*/

        else if(character == '/')
        {
            character = reader.getCurrentCharacter();

            if(character == '>')
            {
                content.append(character);
                reader.readCharacter();
                return new Token(TokenType.EMPTY_CLOSING_TAG, content.toString(), position);
            }

            return new Token(TokenType.HTML_TEXT, content.toString(), position);
        }

        /*else if(character == '-')
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

                    return new Token(keyWords.get(content.toString()), content.toString(), position);
                }
                else
                    return new Token(TokenType.HTML_TEXT, content.toString(), position);
            }

            return new Token(TokenType.HTML_TEXT, content.toString(), position);
        }*/
        else
            return null;
    }

    private boolean isWhiteSpace(char character)
    {
        return character == ' ' || character == '\t'
                || character == '\n' || character == '\r';
    }

    /*private void initialiseKeyWords() {
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
    }*/

    private boolean isKeyWordCharacter(char character) {
        return character == '=' || character == '<' || character == '>' || character == '\\'
                || character == '/';
    }

    public void setAttributeValueReadingMode(boolean mode)
    {
        attributeValueReadingMode = mode;
    }

    public void setInsideTagMode(boolean mode)
    {
        insideTagMode = mode;
    }

    public void setTextMode(boolean mode)
    {
        textMode = mode;
    }
}
