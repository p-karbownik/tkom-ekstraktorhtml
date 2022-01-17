package exceptions.parser;

import reader.Position;

public class UndefinedResourceException extends Exception{
    private String resourceIdentifier;
    private Position position;

    public UndefinedResourceException(String resourceIdentifier, Position position)
    {
        super("Undefined resource " + resourceIdentifier + " at position: row: " + position.getRow() + ", column " + position.getColumn());
        this.resourceIdentifier = resourceIdentifier;
    }

    public String getResourceIdentifier()
    {
        return resourceIdentifier;
    }

    public Position getPosition()
    {
        return position;
    }
}
