package exceptions.parser;

import reader.Position;

public class ResourceRedefinitionException extends Exception{
    private String resourceIdentifier;
    private Position position;
    public ResourceRedefinitionException(String resourceIdentifier, Position position)
    {
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
