package parser.structures;

import visitor.conditionsentence.Visitable;
import visitor.conditionsentence.Visitor;

import java.util.ArrayList;
import java.util.Objects;

public class Condition implements Visitable {
    private ArrayList<Term> terms;

    public Condition(ArrayList<Term> terms)
    {
        this.terms = terms;
    }

    public ArrayList<Term> getTerms()
    {
        return terms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Condition condition = (Condition) o;
        return Objects.equals(terms, condition.terms);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
