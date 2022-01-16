package parser.structures;

import visitor.conditionsentence.Visitable;
import visitor.conditionsentence.Visitor;

import java.util.ArrayList;
import java.util.Objects;

public class Term implements Visitable
{
    private ArrayList<Factor> factors;

    public Term(ArrayList<Factor> factors)
    {
        this.factors = factors;
    }

    public ArrayList<Factor> getFactors()
    {
        return factors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Term term = (Term) o;
        return Objects.equals(factors, term.factors);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
