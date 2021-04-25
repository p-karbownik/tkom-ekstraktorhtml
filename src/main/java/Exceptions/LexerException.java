package Exceptions;

import Lexer.Position;

public class LexerException extends Exception
{
    protected final Position position;

    public LexerException(Position p)
    {
        position = p;
    }

}
