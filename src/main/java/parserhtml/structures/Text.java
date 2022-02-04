package parserhtml.structures;

import visitor.html.Visitable;
import visitor.html.Visitor;

import java.util.Objects;

public class Text implements Element, Visitable {
    private String content;
    private Element parent;
    public Text(String content)
    {
        this.content = content;
    }

    public void addChild(Element e) {}

    public void setParent(Element e)
    {
        parent = e;
    }

    public String getContent()
    {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Text text = (Text) o;
        return Objects.equals(content, text.content);
    }

    @Override
    public Element getParent() {
        return parent;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
