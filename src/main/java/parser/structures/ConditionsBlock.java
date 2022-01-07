package parser.structures;

import java.util.ArrayList;
import java.util.Objects;

public class ConditionsBlock {
    ArrayList<ConditionSentence> conditionSentences;

    public ConditionsBlock(ArrayList<ConditionSentence> conditionSentences)
    {
        this.conditionSentences = conditionSentences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConditionsBlock that = (ConditionsBlock) o;
        return Objects.equals(conditionSentences, that.conditionSentences);
    }
}