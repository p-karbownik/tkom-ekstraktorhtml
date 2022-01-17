package exceptions.parser;

import lexer.TokenType;
import reader.Position;

import java.util.Arrays;

public class UnexpectedTokenException extends Exception{

    private TokenType foundedToken;
    private Position position;
    private TokenType[] expectedTokens;

    public UnexpectedTokenException(TokenType foundedToken, Position position, TokenType... expectedTokenTypes)
    {
        super("Unexpected token " + foundedToken.toString() + " at position: row: " + position.getRow() + ", column " + position.getColumn() + "expected: " + Arrays.toString(expectedTokenTypes));
        this.foundedToken = foundedToken;
        this.position = position;
        this.expectedTokens = expectedTokenTypes;
    }

    public TokenType getFoundedToken()
    {
        return foundedToken;
    }

    public Position getPosition()
    {
        return position;
    }

    public TokenType[] getExpectedTokens()
    {
        return expectedTokens;
    }
}
