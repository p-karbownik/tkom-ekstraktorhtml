package exceptions.parser;

import reader.Position;

public class FieldRedefinitionException extends Exception{
    private final String fieldDefinitionIdentifier;
    private final Position position;

    public FieldRedefinitionException(String fieldDefinitionIdentifier, Position position)
    {
        this.fieldDefinitionIdentifier = fieldDefinitionIdentifier;
        this.position = position;
    }

    public String getFieldDefinitionIdentifier()
    {
        return fieldDefinitionIdentifier;
    }

    public Position getPosition()
    {
        return position;
    }
}
