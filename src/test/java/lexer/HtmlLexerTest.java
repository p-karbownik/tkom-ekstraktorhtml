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

    @Test
    @DisplayName("Test tokenu typu TAG_OPENER")
    public void testTokenTagOpener() throws Exception {
        Token token = getHtmlLexer("<").getNextToken();
        assertEquals(token, new Token(TokenType.TAG_OPENER, "<"));
    }

    @Test
    @DisplayName("Test tokenu typu QUOTE")
    public void testTokenQuote() throws Exception {
        HtmlLexer lexer = getHtmlLexer("\"");
        lexer.setInsideTagMode(true);
        Token token = lexer.getNextToken();
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
        HtmlLexer lexer = getHtmlLexer("=");
        lexer.setInsideTagMode(true);
        Token token = lexer.getNextToken();
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
    public void testStringToken() throws Exception {
        Token token = getHtmlLexer("Waldeinsamkeit").getNextToken();
        assertEquals(token, new Token(TokenType.HTML_TEXT, "Waldeinsamkeit"));
    }
}
