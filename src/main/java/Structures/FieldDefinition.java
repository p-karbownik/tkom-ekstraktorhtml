package Structures;

public class FieldDefinition {
    private String fieldName;
    private PathToResource pathToResource;
    private FieldContent fieldContent;

    public FieldDefinition(String fieldName, PathToResource pathToResource, FieldContent resourceType)
    {
        this.fieldName = fieldName;
        this.pathToResource = pathToResource;
        this.fieldContent = resourceType;
    }
}
