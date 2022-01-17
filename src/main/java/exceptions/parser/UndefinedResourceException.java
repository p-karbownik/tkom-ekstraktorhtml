package exceptions.parser;

import reader.Position;

public class UndefinedResourceException extends Exception{
    private String resourceIdentifier;
    private Position position;

    public UndefinedResourceException(String resourceIdentifier)
    {
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
