package Structures;

public class SimpleCondition extends Condition
{
    private PathInCondition path;
    private Term term;

    public SimpleCondition(PathInCondition path, Term term)
    {
        this.path = path;
        this.term = term;
    }
}
