package parser.structures;

public class DescendantPathElement extends PathElement{
    @Override
    public boolean equals(Object o)
    {
        if(o == null)
            return false;
        return o instanceof DescendantPathElement;
    }
}
