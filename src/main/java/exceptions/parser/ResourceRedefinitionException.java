package exceptions.parser;

import reader.Position;

public class ResourceRedefinitionException extends Exception{
    private String resourceIdentifier;
    private Position position;
    public ResourceRedefinitionException(String resourceIdentifier, Position position)
    {
        super("Redefinition of the resource " + resourceIdentifier +" at position: row: " + position.getRow() +", column" + position.getColumn());
        this.resourceIdentifier = resourceIdentifier;
        this.position = position;
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
