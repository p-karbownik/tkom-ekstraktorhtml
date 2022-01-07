package parser.structures;

import java.util.Objects;

public class TagSubject extends Subject{
    private String identifier;

    public TagSubject(String identifier){
        this.identifier = identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagSubject that = (TagSubject) o;
        return Objects.equals(identifier, that.identifier);
    }
}
