package exceptions.parser;

public class UndefinedResourceException extends Exception{
    private String resourceIdentifier;

    public UndefinedResourceException(String resourceIdentifier)
    {
        this.resourceIdentifier = resourceIdentifier;
    }

    public String getResourceIdentifier()
    {
        return resourceIdentifier;
    }
}
