package exceptions.parser;

import reader.Position;

public class FieldRedefinitionException extends Exception{
    private final String fieldDefinitionIdentifier;
    private final Position position;

    public FieldRedefinitionException(String fieldDefinitionIdentifier, Position position)
    {
        super("Redefinition of field " + fieldDefinitionIdentifier + " at position: row: " + position.getRow() +", column" + position.getColumn());
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
