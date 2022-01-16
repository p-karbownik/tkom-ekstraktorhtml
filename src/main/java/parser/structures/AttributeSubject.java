package parser.structures;

public class AttributeSubject extends Subject{

    public AttributeSubject()
    {
        super(null);
    }

    @Override
    public boolean equals(Object o)
    {
        if(o == null)
            return false;

        return (o instanceof AttributeSubject) && (identifier.compareTo(((AttributeSubject) o ).identifier) == 0);
    }
}