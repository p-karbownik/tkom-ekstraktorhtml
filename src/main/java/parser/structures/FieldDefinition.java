package parser.structures;

import visitor.resource.Visitable;
import visitor.resource.Visitor;

import java.util.Objects;

public class FieldDefinition implements Visitable {
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

    public String getFieldIdentifier() {
        return fieldIdentifier;
    }

    public boolean isExtractAsImage()
    {
        return extractAsImage;
    }

    public PathToResource getPathToResource() {
        return pathToResource;
    }

    public String getAttributeOrResourceIdentifier() {
        return attributeOrResourceIdentifier;
    }

    public boolean isResource() {
        return isResource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDefinition that = (FieldDefinition) o;
        return extractAsImage == that.extractAsImage && isResource == that.isResource && Objects.equals(fieldIdentifier, that.fieldIdentifier) && Objects.equals(pathToResource, that.pathToResource) && Objects.equals(attributeOrResourceIdentifier, that.attributeOrResourceIdentifier);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
