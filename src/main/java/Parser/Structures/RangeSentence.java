package Parser.Structures;

public class RangeSentence {
    private int from;
    private int to;

    public RangeSentence(int from, int to)
    {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RangeSentence that = (RangeSentence) o;
        return from == that.from && to == that.to;
    }

}
