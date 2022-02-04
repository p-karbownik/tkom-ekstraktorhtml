package exceptions.parser;

import lexer.Number;
public class RangeException extends Exception{
    private Number from;
    private Number to;

    public RangeException(Number from, Number to)
    {
        super("Range values are incorrect");
        this.from = from;
        this.to = to;
    }

    public Number getTo() {
        return to;
    }

    public Number getFrom()
    {
        return from;
    }
}
