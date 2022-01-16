package parser.structures;

import java.util.Objects;

public class TagPathElement extends PathElement{
    private String identifier;
    private int number;

    public TagPathElement(String identifier)
    {
        this.identifier = identifier;
        number = 0;
    }
    public TagPathElement(String identifier, int number)
    {
        this.identifier = identifier;
        this.number = number;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public int getNumber()
    {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagPathElement that = (TagPathElement) o;
        return number == that.number && Objects.equals(identifier, that.identifier);
    }
}
