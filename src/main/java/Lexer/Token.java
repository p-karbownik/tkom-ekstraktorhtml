package Lexer;

public class Token {

    private final TokenType type;
    private final String content;

    public Token(TokenType type)
    {
        this.type = type;
        content = "";
    }

    public Token(TokenType type, String content)
    {
        this.type = type;
        this.content = content;
    }

    public TokenType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

}
