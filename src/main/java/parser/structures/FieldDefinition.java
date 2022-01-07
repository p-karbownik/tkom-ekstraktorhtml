package parser.structures;

import java.util.Objects;

public class FieldDefinition {
    private String fieldIdentifier;
    private PathToResource pathToResource;
    private String attributeOrResourceIdentifier;
    private boolean extractAsImage;
    private boolean isResource;

    public FieldDefinition(String fieldIdentifier, PathToResource pathToResource)
    {
        this.fieldIdentifier = fieldIdentifier;
        this.pathToResource = pathToResource;
        attributeOrResourceIdentifier = null;
        extractAsImage = false;
        isResource = false;
    }

    public FieldDefinition(String fieldIdentifier, PathToResource pathToResource, String attributeOrResourceIdentifier, boolean isResource)
    {
        this.fieldIdentifier = fieldIdentifier;
        this.pathToResource = pathToResource;
        this.attributeOrResourceIdentifier = attributeOrResourceIdentifier;
        extractAsImage = false;
        this.isResource = isResource;
    }

    public FieldDefinition(String fieldIdentifier, PathToResource pathToResource, String attributeOrResourceIdentifier, boolean isResource, boolean extractAsImage)
    {
        this.fieldIdentifier = fieldIdentifier;
        this.pathToResource = pathToResource;
        this.attributeOrResourceIdentifier = attributeOrResourceIdentifier;
        this.extractAsImage = extractAsImage;
        this.isResource = isResource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDefinition that = (FieldDefinition) o;
        return extractAsImage == that.extractAsImage && isResource == that.isResource && Objects.equals(fieldIdentifier, that.fieldIdentifier) && Objects.equals(pathToResource, that.pathToResource) && Objects.equals(attributeOrResourceIdentifier, that.attributeOrResourceIdentifier);
    }

}