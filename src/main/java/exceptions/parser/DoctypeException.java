package exceptions.parser;

import reader.Position;

public class DoctypeException extends Exception{
    private Position position;

    public DoctypeException(Position position)
    {
        super("Exception during doctype parsing at position: row: " + position.getRow() +", column" + position.getColumn());
        this.position = position;
    }

    public Position getPosition()
    {
        return position;
    }
}
