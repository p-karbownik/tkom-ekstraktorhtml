package exceptions.parser;

import reader.Position;

public class DoctypeException extends Exception{
    private Position position;

    public DoctypeException(Position position)
    {
        this.position = position;
    }

    public Position getPosition()
    {
        return position;
    }
}
