package Parser.Structures;

public class ClassSubject extends Subject{
    @Override
    public boolean equals(Object o)
    {
        if(o == null)
            return false;

        return (o instanceof ClassSubject) && (identifier.compareTo(((ClassSubject) o ).identifier) == 0);
    }
}