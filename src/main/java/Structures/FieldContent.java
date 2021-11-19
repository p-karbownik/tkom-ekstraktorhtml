package Structures;

import Lexer.TokenType;

public class FieldContent {
    private String attributeName;
    private TokenType contentType;

    public FieldContent(String attributeName, TokenType fieldContentType)
    {
        this.attributeName = attributeName;
        this.contentType = fieldContentType;
    }

    public FieldContent(TokenType contentType)
    {
        this.attributeName = null;
        this.contentType = contentType;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public TokenType getContentType() {
        return contentType;
    }

}
