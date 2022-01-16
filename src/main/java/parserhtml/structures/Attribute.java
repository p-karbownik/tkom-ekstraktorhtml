package parserhtml.structures;

import visitor.html.Visitable;
import visitor.html.Visitor;

import java.net.MalformedURLException;
import java.util.Objects;

public class Attribute implements Visitable {
    private String name;
    private String value;

    public Attribute(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public String getValue()
    {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return Objects.equals(name, attribute.name) && Objects.equals(value, attribute.value);
    }

    @Override
    public Object accept(Visitor visitor) throws Exception {
        return visitor.visit(this);
    }
}
