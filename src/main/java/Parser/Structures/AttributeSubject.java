package Parser.Structures;

public class AttributeSubject extends Subject{
    @Override
    public boolean equals(Object o)
    {
        if(o == null)
            return false;

        return (o instanceof AttributeSubject) && (identifier.compareTo(((AttributeSubject) o ).identifier) == 0);
    }
}