package Exceptions;

import Lexer.Position;

public class WrongTokenException extends LexerException
{
    public WrongTokenException(Position p)
    {
        super(p);
    }

    @Override
    public String toString() {
        return "Wrong token at: " + position.toString();
    }
}
