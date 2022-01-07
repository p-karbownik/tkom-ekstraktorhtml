package parser.structures;

public class ClassLine
{
    private String classIdentifier;

    public ClassLine(String classIdentifier)
    {
        this.classIdentifier = classIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassLine classLine = (ClassLine) o;
        return classIdentifier.equals(classLine.classIdentifier);
    }

}
