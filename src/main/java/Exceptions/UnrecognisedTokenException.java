package Exceptions;

import Lexer.Position;

public class UnrecognisedTokenException extends LexerException
{
    //musi zawierac informacje o pozycji

    public UnrecognisedTokenException(Position p)
    {
        super(p);
    }

    @Override
    public String toString() {
        return "Unrecognised Token at: " + position.toString();
    }
}
