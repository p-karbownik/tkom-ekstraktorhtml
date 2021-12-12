package Parser.Structures;

import java.util.ArrayList;
import java.util.Objects;

public class Term
{
    private ArrayList<Factor> factors;

    public Term(ArrayList<Factor> factors)
    {
        this.factors = factors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Term term = (Term) o;
        return Objects.equals(factors, term.factors);
    }
}
