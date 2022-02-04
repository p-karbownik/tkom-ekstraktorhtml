package visitor.resource;

public interface Visitable {
    Object accept(Visitor visitor);
}
