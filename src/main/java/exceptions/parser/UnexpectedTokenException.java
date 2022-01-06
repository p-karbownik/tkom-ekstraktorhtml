package exceptions.parser;

import Lexer.TokenType;
import Reader.Position;

public class UnexpectedTokenException extends Exception{

    private TokenType foundedToken;
    private Position position;
    private TokenType[] expectedTokens;

    public UnexpectedTokenException(TokenType foundedToken, Position position, TokenType... expectedTokenTypes)
    {
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
