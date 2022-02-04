package parser.structures;

import visitor.resource.Visitable;
import visitor.resource.Visitor;

import java.util.ArrayList;
import java.util.Objects;

public class ConditionsBlock implements Visitable {
    private ArrayList<ConditionSentence> conditionSentences;

    public ConditionsBlock(ArrayList<ConditionSentence> conditionSentences)
    {
        this.conditionSentences = conditionSentences;
    }

    public ArrayList<ConditionSentence> getConditionSentences()
    {
        return conditionSentences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConditionsBlock that = (ConditionsBlock) o;
        return Objects.equals(conditionSentences, that.conditionSentences);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}