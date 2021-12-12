package ParserHTML.Structures;

import java.util.ArrayList;
import java.util.Objects;

public class Attribute {
    private String name;
    private ArrayList<String> values;

    public Attribute(String name, ArrayList<String> values)
    {
        this.name = name;
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return Objects.equals(name, attribute.name) && Objects.equals(values, attribute.values);
    }
}
