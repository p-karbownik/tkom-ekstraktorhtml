package Lexer;

import Reader.Position;

public class Number extends Token{
    private int value;

    public Number(int value, Position position)
    {
        super(TokenType.NUMBER, Integer.toString(value), position);
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
