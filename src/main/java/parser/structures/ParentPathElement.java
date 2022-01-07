package parser.structures;

public class ParentPathElement extends PathElement{

    @Override
    public boolean equals(Object o)
    {
        if(o == null)
            return false;
        return o instanceof ParentPathElement;
    }

}
