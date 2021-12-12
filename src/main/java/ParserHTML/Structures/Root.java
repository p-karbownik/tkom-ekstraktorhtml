package ParserHTML.Structures;

import java.util.ArrayList;
import java.util.Objects;

public class Root implements Element{
    private ArrayList<Element> children;

    public Root()
    {
        children = new ArrayList<>();
    }

    public void addChild(Element e)
    {
        children.add(e);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Root root = (Root) o;
        return Objects.equals(children, root.children);
    }

}
