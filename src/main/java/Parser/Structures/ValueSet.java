package Parser.Structures;

import java.util.ArrayList;
import java.util.Objects;

public class ValueSet {
    ArrayList<String> values;

    public ValueSet(ArrayList<String> values)
    {
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValueSet valueSet = (ValueSet) o;
        return Objects.equals(values, valueSet.values);
    }
}
