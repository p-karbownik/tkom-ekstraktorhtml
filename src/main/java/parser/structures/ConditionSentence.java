package parser.structures;

import visitor.conditionsentence.Visitable;
import visitor.conditionsentence.Visitor;

import java.util.Objects;

public class ConditionSentence implements Visitable {
    private Condition condition;

    public ConditionSentence(Condition condition)
    {
        this.condition = condition;
    }

    public Condition getCondition()
    {
        return condition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConditionSentence that = (ConditionSentence) o;
        return Objects.equals(condition, that.condition);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
