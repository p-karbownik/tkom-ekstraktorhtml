package parserhtml.structures;

import visitor.html.Visitable;
import visitor.html.Visitor;

import java.util.ArrayList;
import java.util.Objects;

public class Root implements Element, Visitable {
    private ArrayList<Element> children;

    public Root()
    {
        children = new ArrayList<>();
    }

    public void addChild(Element e)
    {
        children.add(e);
    }

    public ArrayList<Element> getChildren() {
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Root root = (Root) o;
        return Objects.equals(children, root.children);
    }

    @Override
    public Object accept(Visitor visitor) throws Exception {
        return visitor.visit(this);
    }

    public Element getParent()
    {
        return null;
    }

    @Override
    public void setParent(Element e) {
    }
}
