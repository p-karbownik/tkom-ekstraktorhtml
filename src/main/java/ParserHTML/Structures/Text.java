package ParserHTML.Structures;

import java.util.Objects;

public class Text implements Element{
    private String content;

    public Text(String content)
    {
        this.content = content;
    }

    public void addChild(Element e)
    {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Text text = (Text) o;
        return Objects.equals(content, text.content);
    }
}
