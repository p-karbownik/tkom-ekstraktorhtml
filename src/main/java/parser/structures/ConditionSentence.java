package parser.structures;

import java.util.Objects;

public class ConditionSentence {
    private Condition condition;

    public ConditionSentence(Condition condition)
    {
        this.condition = condition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConditionSentence that = (ConditionSentence) o;
        return Objects.equals(condition, that.condition);
    }
}
