package Lexer;

import Exceptions.LexerException;
import Reader.*;

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
        Token nextToken = null;

        //StringBuilder content = new StringBuilder();
        //Character.isWhitespace()
        while (Character.isWhitespace(reader.getCurrentCharacter())) //skorzystac z metody w Char <- done
            reader.readCharacter();

        Position startPosition = reader.getCurrentPosition();

        if (reader.getCurrentCharacter() == 3)
            nextToken = new Token(TokenType.ETX); // EOF zmienić na ETX - done

        if (nextToken == null)
            nextToken = buildStringToken(startPosition);

        if (nextToken == null)
            nextToken = buildOperatorToken(startPosition);

        if (nextToken == null)
            nextToken = buildNumberToken(startPosition);

        if (nextToken == null)
            nextToken = buildIdentifierOrKeywordToken(startPosition);

        //if (nextToken == null)
        //    nextToken = buildIdentifierToken(content, startPosition);

        if (nextToken == null)
            throw new LexerException("Cannot build token at position: row " + startPosition.getRow() + " column " + startPosition.getColumn());

        return nextToken;
    }

    private Token buildStringToken(Position tokenBeginPosition) throws IOException, LexerException {

        if (reader.getCurrentCharacter() != '\"')
            return null;
        //content mozna zrobic lokalny -> done
        //rozbudowac petle o enkapsulacje
        reader.readCharacter();

        StringBuilder content = new StringBuilder();

        while (reader.getCurrentCharacter() != '\"' && reader.getCurrentCharacter() != 3) { // petla powinna lykac wszystko az do cudzyslowa albo do konca pliku/tekstu <- done
            content.append(reader.getCurrentCharacter());
            reader.readCharacter();
        }

        if (reader.getCurrentCharacter() == 3)
            throw new LexerException("Cannot build string token at position: row " + tokenBeginPosition.getRow() + " column " + tokenBeginPosition.getColumn() + "with content: " + content);

        reader.readCharacter();

        return new Token(TokenType.STRING, content.toString(), tokenBeginPosition);
    }

    private Token buildOperatorToken(Position tokenBeginPosition) throws IOException {
        char character = reader.getCurrentCharacter();

        StringBuilder content = new StringBuilder();

        if (character == '(' || character == ')' || character == '{' || character == '}'
                || character == ';' || character == '.' || character == '[' || character == ']') {
            content.append(character);
            reader.readCharacter();
        }

        else if (character == '!' || character == '=') {
            content.append(reader.getCurrentCharacter());
            reader.readCharacter();
            character = reader.getCurrentCharacter();

            if (character == '=')
                content.append(character);
        }

        TokenType tokenType = operators.get(content.toString());

        if (tokenType == null)
            return null;

        return new Token(tokenType, tokenBeginPosition);
    }

    private Token buildIdentifierOrKeywordToken(Position tokenBeginPosition) throws IOException { // polaczyc to z identyfikatorem, jesli jest w hashmapie to mamy keyword

        if (!(Character.isLetter(reader.getCurrentCharacter()) || reader.getCurrentCharacter() == '_')) //tutaj na isLetter or _
            return null;

        StringBuilder content = new StringBuilder();

        while (Character.isAlphabetic(reader.getCurrentCharacter())
                || reader.getCurrentCharacter() == '_'
                || reader.getCurrentCharacter() == '$'
                || reader.getCurrentCharacter() == '!'
                || reader.getCurrentCharacter() == '-')/*isKeyWordCharacter(reader.getCurrentCharacter()))*/ {
            content.append(reader.getCurrentCharacter());
            reader.readCharacter();
        }

        TokenType tokenType = keyWords.get(content.toString());

        if (tokenType == null)
            return new Token(TokenType.IDENTIFIER, content.toString(), tokenBeginPosition);

        return new Token(tokenType, tokenBeginPosition);
    }
/*
    private Token buildIdentifierToken(StringBuilder content, Position position) throws IOException, LexerException {
        char character = reader.getCurrentCharacter();

        if (content.toString().isEmpty()) {
            if (('A' <= character && character <= 'Z') || ('a' <= character && character <= 'z') ||
                    character == '_' || character == '$' || character == '!') {
                content.append(character);
                reader.readCharacter();
                character = reader.getCurrentCharacter();
            } else
                throw new LexerException("Cannot build identifier token at position: row " + position.getRow() + " column " + position.getColumn() + "with content: " + content.toString());
        }

        while (('A' <= character && character <= 'Z') || ('a' <= character && character <= 'z') ||
                character == '_' || character == '$' || character == '!' || character == '-' || ('0' <= character && character <= '9')) {
            content.append(character);
            reader.readCharacter();
            character = reader.getCurrentCharacter();
        }

        if (!(character == '(' || character == ')' || character == '{' || character == '}'
                || character == ';' || character == '.' || character == '[' || character == ']' || character == '!' || character == '=' || character == 3) && !isWhiteSpace(character))
            throw new LexerException("Cannot build identifier token at position: row " + position.getRow() + " column " + position.getColumn() + "with content: " + content.toString());

        return new Token(TokenType.IDENTIFIER, content.toString(), position);
    }
*/
    private Number buildNumberToken(Position position) throws IOException{
        char character = reader.getCurrentCharacter();

        if(!Character.isDigit(character))
            return null;

        int value = 0;

        if(character != '0')
        {
            while (Character.isDigit(character))
            {
                value *= 10;
                value += character -'0';
                reader.readCharacter();
                character = reader.getCurrentCharacter();
            }
        }

        return new Number(value, position);
        /*
        while (!isWhiteSpace(character) && !(character == '(' || character == ')' || character == '{' || character == '}'
                || character == ';' || character == '.' || character == '[' || character == ']' || character == '!' || character == '=' || character == 0xffff)) {
            content.append(character);
            reader.readCharacter();
            character = reader.getCurrentCharacter();
        }

        //int value;

        try {
            value = Integer.parseInt(content.toString());
        } catch (NumberFormatException e) {
            throw new LexerException("Cannot build number token at position: row " + position.getRow() + " column " + position.getColumn() + "with content: " + content.toString());
        }

        return new Number(value, position);*/
    }
/*
    private boolean isWhiteSpace(char character) {
        return character == ' ' || character == '\t'
                || character == '\n' || character == '\r';
    }
*/
    private void initialiseKeyWords() {
        keyWords = new HashMap<>();

        keyWords.put("resource", TokenType.RESOURCE);
        keyWords.put("tag", TokenType.TAG);
        keyWords.put("conditions", TokenType.CONDITIONS);
        keyWords.put("if", TokenType.IF);
        keyWords.put("parent", TokenType.PARENT);
        keyWords.put("child", TokenType.CHILD);
        keyWords.put("ancestor", TokenType.ANCESTOR);
        keyWords.put("descendant", TokenType.DESCENDANT);
        keyWords.put("has", TokenType.HAS);
        keyWords.put("self", TokenType.SELF);
        keyWords.put("class", TokenType.CLASS);
        keyWords.put("attribute", TokenType.ATTRIBUTE);

        keyWords.put("or", TokenType.OR);
        keyWords.put("and", TokenType.AND);
        keyWords.put("export", TokenType.EXPORT);
        keyWords.put("to", TokenType.TO);
        keyWords.put("set", TokenType.SET);
        keyWords.put("fields", TokenType.FIELDS);
        keyWords.put("asImg", TokenType.ASIMG);
        keyWords.put("every", TokenType.EVERY);
        keyWords.put("from", TokenType.FROM);
        keyWords.put("amount", TokenType.AMOUNT);

        keyWords.put("not", TokenType.NOT);
        keyWords.put("field", TokenType.FIELD);
    }

    private void initialiseOperators() {
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
        operators.put("[", TokenType.LEFT_SQUARE_BRACKET);
        operators.put("]", TokenType.RIGHT_SQUARE_BRACKET);
    }
/*
    private boolean isStringCharacter(int character) {
        return ('A' <= character && character <= 'Z') ||
                ('a' <= character && character <= 'z')
                || character == ' ' || character == '$' || character == '_'
                || character == '.' || character == '-'
                || ('0' <= character && character <= '9');
    }

    private boolean isKeyWordCharacter(int character) {
        return ('a' <= character && character <= 'z' || character == 'I');
    }
*/
}