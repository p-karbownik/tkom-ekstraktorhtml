package Parser.Structures;

import java.util.Objects;

public class Subject {
    protected String identifier = "";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(identifier, subject.identifier);
    }

    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }
}
