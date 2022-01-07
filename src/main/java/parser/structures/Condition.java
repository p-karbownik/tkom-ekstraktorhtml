package parser.structures;

import java.util.ArrayList;
import java.util.Objects;

public class Condition {
    private ArrayList<Term> terms;

    public Condition(ArrayList<Term> terms)
    {
        this.terms = terms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Condition condition = (Condition) o;
        return Objects.equals(terms, condition.terms);
    }
}
