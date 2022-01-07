package parser.structures;


public class SelfPathElement extends PathElement{
    @Override
    public boolean equals(Object o)
    {
        if(o == null)
            return false;
        return o instanceof SelfPathElement;
    }
}
