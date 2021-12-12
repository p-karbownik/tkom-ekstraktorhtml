package Parser.Structures;

public class AncestorPathElement extends PathElement{

    @Override
    public boolean equals(Object o)
    {
        if(o == null)
            return false;
        return o instanceof AncestorPathElement;
    }
}
