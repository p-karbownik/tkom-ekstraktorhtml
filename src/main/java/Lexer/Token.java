package Lexer;

public class Token {

    private final TokenType type;
    private final String content;
    private final Position position;

    public Token(TokenType type)
    {
        this.type = type;
        content = "";
        position = null;
    }

    public Token(TokenType type, String content)
    {
        this.type = type;
        this.content = content;
        position = null;
    }

    public Token(TokenType type, Position position)
    {
        this.type = type;
        content = "";
        this.position = position;
    }

    public Token(TokenType type, String content, Position position)
    {
        this.type = type;
        this.content = content;
        this.position = position;
    }

    public TokenType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public Position getPosition()
    {
        return position;
    }
    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof Token ))
            return false;
        Token token = (Token) object;

        return type == token.type && content.compareTo(token.content) == 0;
    }


}
