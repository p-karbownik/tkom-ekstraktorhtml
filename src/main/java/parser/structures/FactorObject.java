package parser.structures;

import visitor.conditionsentence.Visitable;
import visitor.conditionsentence.Visitor;

import java.util.Objects;

public class FactorObject implements Visitable {
    private boolean isNegated;
    private Subject subject;
    private ComparisonObject comparisonObject;

    public FactorObject(boolean isNegated, Subject subject)
    {
        this.isNegated = isNegated;
        this.subject = subject;
        comparisonObject = null;
    }

    public FactorObject(boolean isNegated, Subject subject, ComparisonObject comparisonObject)
    {
        this.isNegated = isNegated;
        this.subject = subject;
        this.comparisonObject = comparisonObject;
    }

    public Subject getSubject()
    {
        return subject;
    }

    public boolean isNegated()
    {
        return isNegated;
    }

    public ComparisonObject getComparisonObject()
    {
        return comparisonObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FactorObject that = (FactorObject) o;
        return isNegated == that.isNegated && Objects.equals(subject, that.subject) && Objects.equals(comparisonObject, that.comparisonObject);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
