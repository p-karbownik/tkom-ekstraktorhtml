package Parser.Structures;

import java.util.Objects;

public class TagSentence
{
    private final String tagName;

    public TagSentence(String tagName)
    {
        this.tagName = tagName;
    }

    public String getTagName()
    {
        return tagName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagSentence that = (TagSentence) o;
        return Objects.equals(tagName, that.tagName);
    }
}
