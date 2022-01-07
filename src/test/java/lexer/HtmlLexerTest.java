package lexer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import reader.Reader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HtmlLexerTest {

    private HtmlLexer getHtmlLexer(String s) throws IOException {
        BufferedReader br = new BufferedReader(new StringReader(s));
        return new HtmlLexer(new Reader(br));
    }
/*
    @Test
    @DisplayName("Test tokenu TYPU COMMENT_TAG_OPENER")
    public void testTokenCommentTagOpener() throws Exception{
        Token token = getHtmlLexer("<!--").getNextToken();
        assertEquals(token, new Token(TokenType.COMMENT));
    }

    @Test
    @DisplayName("Test tokenu typu comment")
    public void testTokenCommentTagClosing() throws Exception
    {
        Token token = getHtmlLexer("-->").getNextToken();
        assertEquals(token, new Token(TokenType.COMMENT_TAG_CLOSING));
    }
*/

    @Test
    @DisplayName("Test tokenu typu TAG_OPENER")
    public void testTokenTagOpener() throws Exception {
        Token token = getHtmlLexer("<").getNextToken();
        assertEquals(token, new Token(TokenType.TAG_OPENER, "<"));
    }

    @Test
    @DisplayName("Test tokenu typu QUOTE")
    public void testTokenQuote() throws Exception {
        Token token = getHtmlLexer("\"").getNextToken();
        assertEquals(token, new Token(TokenType.QUOTE, "\""));
    }

    @Test
    @DisplayName("Test tokenu typu DOCTYPE")
    public void testTokenDoctype() throws Exception {
        Token token = getHtmlLexer("<!").getNextToken();
        assertEquals(token, new Token(TokenType.DOCTYPE, "<!"));
    }

    @Test
    @DisplayName("Test tokenu typu ASSIGN_OPERATOR")
    public void testTokenAssignOperator() throws Exception {
        Token token = getHtmlLexer("=").getNextToken();
        assertEquals(token, new Token(TokenType.ASSIGN_OPERATOR, "="));
    }

    @Test
    @DisplayName("Test tokenu typu EMPTY CLOSING TAG")
    public void testTokenEmptyClosingTag() throws Exception {
        Token token = getHtmlLexer("/>").getNextToken();
        assertEquals(token, new Token(TokenType.EMPTY_CLOSING_TAG, "/>"));
    }

    @Test
    @DisplayName("Test tokenu typu TAG CLOSING MARK")
    public void testTokenTagClosingMark() throws Exception {
        Token token = getHtmlLexer(">").getNextToken();
        assertEquals(token, new Token(TokenType.TAG_CLOSING_MARK, ">"));
    }

    @Test
    @DisplayName("Test tokenu typu CLOSING TAG")
    public void testTokenClosingTag() throws Exception {
        Token token = getHtmlLexer("</").getNextToken();
        assertEquals(token, new Token(TokenType.CLOSING_TAG, "</"));
    }

    @Test
    @DisplayName("Test tokenu typu HTML TEXT")
    public void testStringToken() throws Exception
    {
        Token token = getHtmlLexer("Waldeinsamkeit").getNextToken();
        assertEquals(token, new Token(TokenType.HTML_TEXT, "Waldeinsamkeit"));
    }

    @Test
    @DisplayName("Test kodu przykladowego")
    public void testExampleCode() throws Exception
    {
        String text = "<html>\n" +
                "  <head>\n" +
                "    <title>Href Attribute Example</title>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <h1>Href Attribute Example</h1>\n" +
                "    <p>\n" +
                "      <a href=\"https://www.freecodecamp.org/contribute/\">The freeCodeCamp Contribution Page</a> shows you how and where you can contribute to freeCodeCamp's community and growth.\n" +
                "    </p>\n" +
                "  </body>\n" +
                "</html>\n" +
                "<!-- cosik -->";

        HtmlLexer lexer = getHtmlLexer(text);

        Token[] tokens = new Token[]
                {
                        new Token(TokenType.TAG_OPENER),
                        new Token(TokenType.HTML_TEXT, "html"),
                        new Token(TokenType.TAG_CLOSING_MARK),
                        new Token(TokenType.TAG_OPENER),
                        new Token(TokenType.HTML_TEXT, "head"),
                        new Token(TokenType.TAG_CLOSING_MARK),
                        new Token(TokenType.TAG_OPENER),
                        new Token(TokenType.HTML_TEXT, "title"),
                        new Token(TokenType.TAG_CLOSING_MARK),
                        new Token(TokenType.HTML_TEXT, "Href"),
                        new Token(TokenType.HTML_TEXT, "Attribute"),
                        new Token(TokenType.HTML_TEXT, "Example"),
                        new Token(TokenType.CLOSING_TAG),
                        new Token(TokenType.HTML_TEXT, "title"),
                        new Token(TokenType.TAG_CLOSING_MARK),
                        new Token(TokenType.CLOSING_TAG),
                        new Token(TokenType.HTML_TEXT, "head"),
                        new Token(TokenType.TAG_CLOSING_MARK),

                        new Token(TokenType.TAG_OPENER),
                        new Token(TokenType.HTML_TEXT, "body"),
                        new Token(TokenType.TAG_CLOSING_MARK),
                        new Token(TokenType.TAG_OPENER),
                        new Token(TokenType.HTML_TEXT, "h1"),
                        new Token(TokenType.TAG_CLOSING_MARK),
                        new Token(TokenType.HTML_TEXT, "Href"),
                        new Token(TokenType.HTML_TEXT, "Attribute"),
                        new Token(TokenType.HTML_TEXT, "Example"),
                        new Token(TokenType.CLOSING_TAG),
                        new Token(TokenType.HTML_TEXT, "h1"),
                        new Token(TokenType.TAG_CLOSING_MARK),

                        new Token(TokenType.TAG_OPENER),
                        new Token(TokenType.HTML_TEXT, "p"),
                        new Token(TokenType.TAG_CLOSING_MARK),

                        new Token(TokenType.TAG_OPENER),
                        new Token(TokenType.HTML_TEXT, "a"),
                        new Token(TokenType.HTML_TEXT, "href"),
                        new Token(TokenType.ASSIGN_OPERATOR),
                        new Token(TokenType.QUOTE),
                        new Token(TokenType.HTML_TEXT, "https:"),//www.freecodecamp.org/contribute/"),
                        new Token(TokenType.HTML_TEXT, "/"),
                        new Token(TokenType.HTML_TEXT, "/"),
                        new Token(TokenType.HTML_TEXT, "www.freecodecamp.org"),
                        new Token(TokenType.HTML_TEXT, "/"),
                        new Token(TokenType.HTML_TEXT, "contribute"),
                        new Token(TokenType.HTML_TEXT, "/"),
                        new Token(TokenType.QUOTE),
                        new Token(TokenType.TAG_CLOSING_MARK),
                        new Token(TokenType.HTML_TEXT, "The"),
                        new Token(TokenType.HTML_TEXT, "freeCodeCamp"),
                        new Token(TokenType.HTML_TEXT, "Contribution"),
                        new Token(TokenType.HTML_TEXT, "Page"),
                        new Token(TokenType.CLOSING_TAG),
                        new Token(TokenType.HTML_TEXT, "a"),
                        new Token(TokenType.TAG_CLOSING_MARK),
                        new Token(TokenType.HTML_TEXT, "shows"),
                        new Token(TokenType.HTML_TEXT, "you"),
                        new Token(TokenType.HTML_TEXT, "how"),
                        new Token(TokenType.HTML_TEXT, "and"),
                        new Token(TokenType.HTML_TEXT, "where"),
                        new Token(TokenType.HTML_TEXT, "you"),
                        new Token(TokenType.HTML_TEXT, "can"),
                        new Token(TokenType.HTML_TEXT, "contribute"),
                        new Token(TokenType.HTML_TEXT, "to"),
                        new Token(TokenType.HTML_TEXT, "freeCodeCamp's"),
                        new Token(TokenType.HTML_TEXT, "community"),
                        new Token(TokenType.HTML_TEXT, "and"),
                        new Token(TokenType.HTML_TEXT, "growth."),
                        new Token(TokenType.CLOSING_TAG),
                        new Token(TokenType.HTML_TEXT, "p"),
                        new Token(TokenType.TAG_CLOSING_MARK),
                        new Token(TokenType.CLOSING_TAG),
                        new Token(TokenType.HTML_TEXT, "body"),
                        new Token(TokenType.TAG_CLOSING_MARK),
                        new Token(TokenType.CLOSING_TAG),
                        new Token(TokenType.HTML_TEXT, "html"),
                        new Token(TokenType.TAG_CLOSING_MARK),

                        new Token(TokenType.COMMENT, "<!-- cosik -->"),

                        new Token(TokenType.ETX)
                };

        for(Token t : tokens)
        {
            Token token = lexer.getNextToken();
            assertEquals(t, token);
        }
    }
}
