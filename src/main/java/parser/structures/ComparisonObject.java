package parser.structures;

import java.util.Objects;

public class ComparisonObject {
    private ComparisonOperator comparisonOperator;
    private String value;
    private ValueSet valueSet;

    public ComparisonObject(ValueSet valueSet) {
        this.valueSet = valueSet;
        value = null;
        comparisonOperator = null;
    }

    public ComparisonObject(ComparisonOperator comparisonOperator, String value) {
        this.comparisonOperator = comparisonOperator;
        this.value = value;
        valueSet = null;
    }

    public ComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }

    public String getValue() {
        return value;
    }

    public ValueSet getValueSet() {
        return valueSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComparisonObject that = (ComparisonObject) o;
        return Objects.equals(comparisonOperator, that.comparisonOperator) && Objects.equals(value, that.value) && Objects.equals(valueSet, that.valueSet);
    }


}
