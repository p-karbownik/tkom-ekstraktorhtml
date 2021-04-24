package Lexer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.StringReader;

public class LexerTest {

    @Test
    @DisplayName("Test tokena typu STRING")
    public void testTokenString() throws Exception {
        BufferedReader br = new BufferedReader(new StringReader("[String]"));
        Lexer lexer = new Lexer(br);
        Token token = lexer.getNextToken();
        assertAll("string token",
                () -> assertEquals(TokenType.STRING, token.getType()),
                () -> assertEquals("String", token.getContent()));
    }
}
