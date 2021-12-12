package ParserHTML.Structures;

import java.util.ArrayList;
import java.util.Objects;

public class Tag implements Element {
    private String name;
    private ArrayList<Element> children;
    private ArrayList<Attribute> attributes;
    private boolean isClosed;

    public Tag(String name, ArrayList<Attribute> attributes)
    {
        this.name = name;
        this.attributes = attributes;
        this.children = new ArrayList<>();
        isClosed = false;
    }

    public Tag(String name)
    {
        this.name = name;
        attributes = null;
        children = new ArrayList<>();
        isClosed = false;
    }

    public void addChild(Element e)
    {
        children.add(e);
    }

    public void close()
    {
        isClosed = true;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return isClosed == tag.isClosed && Objects.equals(name, tag.name) && Objects.equals(children, tag.children) && Objects.equals(attributes, tag.attributes);
    }
}
