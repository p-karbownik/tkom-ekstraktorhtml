package parserhtml.structures;

import visitor.html.Visitable;
import visitor.html.Visitor;

import java.util.ArrayList;
import java.util.Objects;

public class Tag implements Element, Visitable {
    private String name;
    private ArrayList<Element> children;
    private ArrayList<Attribute> attributes;
    private boolean isClosed;
    private Element parent;

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

    public void setParent(Element e)
    {
        parent = e;
    }

    public Element getParent()
    {
        return parent;
    }

    public ArrayList<Element> getChildren() {
        return children;
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

    public ArrayList<Attribute> getAttributes()
    {
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return isClosed == tag.isClosed && Objects.equals(name, tag.name) && Objects.equals(children, tag.children) && Objects.equals(attributes, tag.attributes);
    }

    @Override
    public Object accept(Visitor visitor) throws Exception {
        return visitor.visit(this);
    }
}
