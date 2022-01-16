package visitor.conditionsentence;

import parser.structures.*;

public interface Visitor {

    Object visit(ConditionSentence conditionSentence);

    Object visit(Condition condition);

    Object visit(Term term);

    Object visit(Factor factor);

    Object visit(FactorObject factorObject);

}
