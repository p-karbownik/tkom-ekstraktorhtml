package parserhtml.structures;

import java.util.Objects;

public class Attribute {
    private String name;
    private String value;

    public Attribute(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return Objects.equals(name, attribute.name) && Objects.equals(value, attribute.value);
    }
}
