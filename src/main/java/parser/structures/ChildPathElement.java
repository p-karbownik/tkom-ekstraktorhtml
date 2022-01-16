package parser.structures;

import java.util.Objects;

public class ChildPathElement extends PathElement{
    private RelativeCondition relativeCondition;

    public ChildPathElement()
    {
        relativeCondition = null;
    }

    public ChildPathElement(RelativeCondition relativeCondition)
    {
        this.relativeCondition = relativeCondition;
    }

    public RelativeCondition getRelativeCondition()
    {
        return relativeCondition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChildPathElement that = (ChildPathElement) o;
        return Objects.equals(relativeCondition, that.relativeCondition);
    }
}
