package parser.structures;

import visitor.conditionsentence.Visitable;
import visitor.conditionsentence.Visitor;

import java.util.Objects;

public class Factor implements Visitable {
    private Path path;
    private FactorObject factorObject;
    private Condition condition;

    public Factor(Path path, FactorObject factorObject)
    {
        this.path = path;
        this.factorObject = factorObject;
        this.condition = null;
    }

    public Factor(Condition condition)
    {
        this.path = null;
        this.factorObject = null;
        this.condition = condition;
    }

    public Path getPath() {
        return path;
    }

    public FactorObject getFactorObject() {
        return factorObject;
    }

    public Condition getCondition() {
        return condition;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Factor factor = (Factor) o;
        return Objects.equals(path, factor.path) && Objects.equals(factorObject, factor.factorObject) && Objects.equals(condition, factor.condition);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
