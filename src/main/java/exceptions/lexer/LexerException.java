package exceptions.lexer;

import reader.Position;

public class LexerException extends Exception
{
    private Position position;
    private String content;
    public LexerException(String s, Position position)
    {
        super(s);
        this.position = position;
    }

    public LexerException(String s, Position position, String content)
    {
        super(s);
        this.position = position;
        this.content = content;
    }

    public Position getPosition()
    {
        return position;
    }

    public String getContent()
    {
        return content;
    }
}
