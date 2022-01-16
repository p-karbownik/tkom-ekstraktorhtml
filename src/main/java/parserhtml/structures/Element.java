package parserhtml.structures;

public interface Element {
    void addChild(Element e);

    Element getParent();

    void setParent(Element e);
}
