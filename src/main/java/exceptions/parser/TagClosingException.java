package exceptions.parser;

import reader.Position;

public class TagClosingException extends Exception{
    private String expectedTagName;
    private String actualTagName;
    private Position position;

    public TagClosingException(String expectedTagName, String actualTagName, Position position)
    {
        super("Expected tag name: " + expectedTagName + ", actual tag name: " + actualTagName + " founded at position: row: " + position.getRow() + ", column" + position.getColumn());
        this.expectedTagName = expectedTagName;
        this.actualTagName = actualTagName;
        this.position = position;
    }

    public String getExpectedTagName() {
        return expectedTagName;
    }

    public String getActualTagName() {
        return actualTagName;
    }

    public Position getPosition() {
        return position;
    }
}