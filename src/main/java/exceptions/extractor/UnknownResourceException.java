package exceptions.extractor;

public class UnknownResourceException extends Exception{
    private String resourceName;

    public UnknownResourceException(String resourceName)
    {
        super("Unknown resource with name: " + resourceName);
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }
}
