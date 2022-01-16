package parser.structures;

import visitor.resource.Visitor;

public class RangeSentence extends QuantitativeConstraintSentence{
    private int from;
    private int to;

    public RangeSentence(int from, int to)
    {
        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RangeSentence that = (RangeSentence) o;
        return from == that.from && to == that.to;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
