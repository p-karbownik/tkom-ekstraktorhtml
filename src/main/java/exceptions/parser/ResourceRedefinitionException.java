package exceptions.parser;

public class ResourceRedefinitionException extends Exception{
    private String resourceIdentifier;

    public ResourceRedefinitionException(String resourceIdentifier)
    {
        this.resourceIdentifier = resourceIdentifier;
    }

    public String getResourceIdentifier()
    {
        return resourceIdentifier;
    }
}
