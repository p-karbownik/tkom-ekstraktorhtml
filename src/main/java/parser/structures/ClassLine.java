package parser.structures;

import visitor.resource.Visitable;
import visitor.resource.Visitor;

public class ClassLine implements Visitable
{
    private String classIdentifier;

    public ClassLine(String classIdentifier)
    {
        this.classIdentifier = classIdentifier;
    }

    public String getClassIdentifier()
    {
        return classIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassLine classLine = (ClassLine) o;
        return classIdentifier.equals(classLine.classIdentifier);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
