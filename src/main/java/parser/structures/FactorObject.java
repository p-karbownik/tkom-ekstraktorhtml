package parser.structures;

import java.util.Objects;

public class FactorObject {
    private boolean isNegated;
    private Subject subject;
    private ComparisonOperator comparisonOperator;
    private ValueSet valueSet;

    public FactorObject(boolean isNegated, Subject subject)
    {
        this.isNegated = isNegated;
        this.subject = subject;
        comparisonOperator = null;
        valueSet = null;
    }

    public FactorObject(boolean isNegated, Subject subject, ComparisonOperator comparisonOperator, ValueSet valueSet)
    {
        this.isNegated = isNegated;
        this.subject = subject;
        this.comparisonOperator = comparisonOperator;
        this.valueSet = valueSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FactorObject that = (FactorObject) o;
        return isNegated == that.isNegated && Objects.equals(subject, that.subject) && Objects.equals(comparisonOperator, that.comparisonOperator) && Objects.equals(valueSet, that.valueSet);
    }
}
