package parser.structures;

import java.util.Objects;

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

        return (o instanceof AttributeSubject) && Objects.equals(identifier, ((AttributeSubject) o).identifier);
    }
}