package parser.structures;

import java.util.Objects;

public class RelativeCondition {
    private Condition condition;

    public RelativeCondition(Condition condition)
    {
        this.condition = condition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelativeCondition that = (RelativeCondition) o;
        return Objects.equals(condition, that.condition);
    }

}
