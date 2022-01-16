package visitor.conditionsentence;

public interface Visitable {

    Object accept(Visitor visitor);

}