package Lexer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LexerTest {

    private Lexer getLexer(String s) throws IOException {
        BufferedReader br = new BufferedReader(new StringReader(s));
        return new Lexer(new Reader(br));
    }

    @Test
    @DisplayName("Test tokena typu STRING")
    public void testTokenString() throws Exception {
        Token token = getLexer("[String]").getNextToken();
        assertAll("string token",
                () -> assertEquals(TokenType.STRING, token.getType()),
                () -> assertEquals("String", token.getContent()));
    }

    @Test
    @DisplayName("Test tokena typu NUMBER")
    public void testTokenNumber() throws Exception {
        Token token = getLexer("<12345>").getNextToken();
        assertAll("string token",
                () -> assertEquals(TokenType.NUMBER, token.getType()),
                () -> assertEquals("12345", token.getContent()));
    }

    @Test
    @DisplayName("Test wykrywania tokenu typu RESOURCE")
    public void testResourceToken() throws Exception {
        Token resourceToken = getLexer("resource").getNextToken();
        assertEquals(resourceToken, new Token(TokenType.RESOURCE));
    }

    @Test
    @DisplayName("Test wykrywania tokenu TAG")
    public void testTagToken() throws Exception {
        Token tagToken = getLexer("tag").getNextToken();
        assertEquals(tagToken, new Token(TokenType.TAG));
    }

    @Test
    @DisplayName("Test wykrywania tokenu conditions")
    public void testConditionsToken() throws Exception
    {
        Token conditionsToken = getLexer("conditions").getNextToken();
        assertEquals(conditionsToken, new Token(TokenType.CONDITIONS));
    }

    @Test
    @DisplayName("Test wykrywania tokenu IF")
    public void testIfToken() throws Exception
    {
        Token ifToken = getLexer("if").getNextToken();
        assertEquals(ifToken, new Token(TokenType.IF));
    }

    @Test
    @DisplayName("Test wykrywania tokenu PARENT")
    public void testParentToken() throws Exception
    {
        Token parentToken = getLexer("parent").getNextToken();
        assertEquals(parentToken, new Token(TokenType.PARENT));
    }

    @Test
    @DisplayName("Test wykrywania tokenu CHILD")
    public void testChildToken() throws Exception
    {
        Token childToken = getLexer("child").getNextToken();
        assertEquals(childToken, new Token(TokenType.CHILD));
    }

    @Test
    @DisplayName("Test wykrywania tokenu HAS")
    public void testHasToken() throws Exception
    {
        Token hasToken = getLexer("has").getNextToken();
        assertEquals(hasToken, new Token(TokenType.HAS));
    }

    @Test
    @DisplayName("Test wykrywania tokenu CLASS")
    public void testClassToken() throws Exception
    {
        Token classToken = getLexer("class").getNextToken();
        assertEquals(classToken, new Token(TokenType.CLASS));
    }

    @Test
    @DisplayName("Test wykrywania tokenu ATTRIBUTE")
    public void testAttributeToken() throws Exception
    {
        Token attributeToken = getLexer("attribute").getNextToken();
        assertEquals(attributeToken, new Token(TokenType.ATTRIBUTE));
    }

    @Test
    @DisplayName("Test wykrywania tokenu OR")
    public void testOrToken() throws Exception
    {
        Token parentToken = getLexer("or").getNextToken();
        assertEquals(parentToken, new Token(TokenType.OR));
    }

    @Test
    @DisplayName("Test wykrywania tokenu AND")
    public void testAndToken() throws Exception
    {
        Token childToken = getLexer("and").getNextToken();
        assertEquals(childToken, new Token(TokenType.AND));
    }

    @Test
    @DisplayName("Test wykrywania tokenu EXPORT")
    public void testExportToken() throws Exception
    {
        Token hasToken = getLexer("export").getNextToken();
        assertEquals(hasToken, new Token(TokenType.EXPORT));
    }

    @Test
    @DisplayName("Test wykrywania tokenu TO")
    public void testToToken() throws Exception
    {
        Token classToken = getLexer("to").getNextToken();
        assertEquals(classToken, new Token(TokenType.TO));
    }

    @Test
    @DisplayName("Test wykrywania tokenu SET")
    public void testSetToken() throws Exception
    {
        Token attributeToken = getLexer("set").getNextToken();
        assertEquals(attributeToken, new Token(TokenType.SET));
    }

    @Test
    @DisplayName("Test wykrywania tokenu FIELDS")
    public void testFieldsToken() throws Exception
    {
        Token attributeToken = getLexer("fields").getNextToken();
        assertEquals(attributeToken, new Token(TokenType.FIELDS));
    }

    @Test
    @DisplayName("Test wykrywania tokenu TEXT")
    public void testTextToken() throws Exception
    {
        Token attributeToken = getLexer("text").getNextToken();
        assertEquals(attributeToken, new Token(TokenType.TEXT));
    }

    @Test
    @DisplayName("Test wykrywania tokenu IMG")
    public void testImgToken() throws Exception
    {
        Token attributeToken = getLexer("img").getNextToken();
        assertEquals(attributeToken, new Token(TokenType.IMG));
    }
                //EVERY, // every
    @Test
    @DisplayName("Test wykrywania tokenu EVERY")
    public void testEveryToken() throws Exception
    {
        Token attributeToken = getLexer("every").getNextToken();
        assertEquals(attributeToken, new Token(TokenType.EVERY));
    }

   @Test
   @DisplayName("Test wykrywania tokenu FROM")
   public void testFromToken() throws Exception
   {
       Token attributeToken = getLexer("from").getNextToken();
       assertEquals(attributeToken, new Token(TokenType.FROM));
   }

   @Test
   @DisplayName("Test wykrywania tokenu THIS")
   public void testThisToken() throws Exception
   {
       Token attributeToken = getLexer("this").getNextToken();
       assertEquals(attributeToken, new Token(TokenType.THIS));
   }
                //AMOUNT, // amount
   @Test
   @DisplayName("Test wykrywania tokenu AMOUNT")
   public void testAmountToken() throws Exception
   {
       Token attributeToken = getLexer("amount").getNextToken();
       assertEquals(attributeToken, new Token(TokenType.AMOUNT));
   }

   @Test
   @DisplayName("Test wykrywania tokenu NO")
   public void testNoToken() throws Exception
   {
       Token attributeToken = getLexer("no").getNextToken();
       assertEquals(attributeToken, new Token(TokenType.NO));
   }

   @Test
   @DisplayName("Test wykrywania tokenu FIELD")
   public void testFieldToken() throws Exception
   {
       Token attributeToken = getLexer("field").getNextToken();
       assertEquals(attributeToken, new Token(TokenType.FIELD));
   }

   @Test
   @DisplayName("Test wykrywania tokenu LEFT_BRACE")
   public void testLeftBraceToken() throws Exception
   {
       Token attributeToken = getLexer("set").getNextToken();
       assertEquals(attributeToken, new Token(TokenType.SET));
   }

   @Test
   @DisplayName("Test wykrywania tokenu RIGHT_BRACE")
   public void testRightBraceToken() throws Exception
   {
       Token attributeToken = getLexer("}").getNextToken();
       assertEquals(attributeToken, new Token(TokenType.RIGHT_BRACE));
   }

   @Test
   @DisplayName("Test wykrywania tokenu EQUAL")
   public void testEqualToken() throws Exception
   {
       Token attributeToken = getLexer("==").getNextToken();
       assertEquals(attributeToken, new Token(TokenType.EQUAL));
   }

   @Test
   @DisplayName("Test wykrywania tokenu NOT_EQUAL")
   public void testNotEqualToken() throws Exception
   {
       Token attributeToken = getLexer("!=").getNextToken();
       assertEquals(attributeToken, new Token(TokenType.NOT_EQUAL));
   }

    @Test
    @DisplayName("Test wykrywania tokenu Assign Operator")
    public void testAssignOperatorToken() throws Exception
    {
        Token attributeToken = getLexer("=").getNextToken();
        assertEquals(attributeToken, new Token(TokenType.ASSIGN_OPERATOR));
    }

    @Test
    @DisplayName("Test wykrywania tokenu DOT")
    public void testDotToken() throws Exception
    {
        Token attributeToken = getLexer(".").getNextToken();
        assertEquals(attributeToken, new Token(TokenType.DOT));
    }

    @Test
    @DisplayName("Test wykrywania tokenu SEMI_COLON")
    public void testSemiColonToken() throws Exception
    {
        Token attributeToken = getLexer(";").getNextToken();
        assertEquals(attributeToken, new Token(TokenType.SEMI_COLON));
    }
    @Test
    @DisplayName("Test wykrywania tokenu LEFT_ROUND_BRACKET")
    public void testLeftRoundBracketToken() throws Exception
    {
        Token attributeToken = getLexer("(").getNextToken();
        assertEquals(attributeToken, new Token(TokenType.LEFT_ROUND_BRACKET));
    }
    @Test
    @DisplayName("Test wykrywania tokenu RIGHT_ROUND_BRACKET")
    public void testRightRoundToken() throws Exception
    {
        Token attributeToken = getLexer(")").getNextToken();
        assertEquals(attributeToken, new Token(TokenType.RIGHT_ROUND_BRACKET));
    }
    @Test
    @DisplayName("Uproszczony kod przykladu numer 1")
    public void testFirstSimplifiedExampleCode() throws IOException {
        String text = "resource\n{\ntag = [a];\n" +
                "\n" +
                "export to class = [Resource];\n" +
                "set fields\n" +
                "{\n" +
                "field[img] = from tag[div]<1>.tag[img]<1>.attribute[src].img;\n" +
                "}\n" +
                "amount = every;\n" +
                "}";
        Lexer lexer = getLexer(text);
        Token[] tokens = new Token[]
                {
                        new Token(TokenType.RESOURCE),
                        new Token(TokenType.LEFT_BRACE),
                        new Token(TokenType.TAG), new Token(TokenType.ASSIGN_OPERATOR), new Token(TokenType.STRING, "a"), new Token(TokenType.SEMI_COLON),
                        new Token(TokenType.EXPORT),
                        new Token(TokenType.TO),
                        new Token(TokenType.CLASS),
                        new Token(TokenType.ASSIGN_OPERATOR),
                        new Token(TokenType.STRING, "Resource"),
                        new Token(TokenType.SEMI_COLON),
                        new Token(TokenType.SET),
                        new Token(TokenType.FIELDS),
                        new Token(TokenType.LEFT_BRACE),

                        new Token(TokenType.FIELD),
                        new Token(TokenType.STRING, "img"),
                        new Token(TokenType.ASSIGN_OPERATOR),
                        new Token(TokenType.FROM),
                        new Token(TokenType.TAG),
                        new Token(TokenType.STRING, "div"),
                        new Token(TokenType.NUMBER, "1"),
                        new Token(TokenType.DOT),

                        new Token(TokenType.TAG),
                        new Token(TokenType.STRING, "img"),
                        new Token(TokenType.NUMBER, "1"),
                        new Token(TokenType.DOT),

                        new Token(TokenType.ATTRIBUTE),
                        new Token(TokenType.STRING, "src"),
                        new Token(TokenType.DOT),
                        new Token(TokenType.IMG),
                        new Token(TokenType.SEMI_COLON),

                        new Token(TokenType.RIGHT_BRACE),

                        new Token(TokenType.AMOUNT),
                        new Token(TokenType.ASSIGN_OPERATOR),
                        new Token(TokenType.EVERY),
                        new Token(TokenType.SEMI_COLON),
                        new Token(TokenType.RIGHT_BRACE)
                        //new Token(TokenType.EOF)
                };
        
        List tokenList = new ArrayList<>(Arrays.asList(tokens));
        try {
            for (Object t: tokenList) {
                Token token = lexer.getNextToken();
                assertEquals(token, t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test kodu przykladu numer 2")
    public void testSecondExampleCode() throws IOException {
        File file = new File("src/main/resources/example2.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        Lexer lexer = new Lexer(new Reader(br));
        Token[] tokens = new Token[]
                {
                        new Token(TokenType.RESOURCE),
                        new Token(TokenType.LEFT_BRACE),

                        new Token(TokenType.TAG),
                        new Token(TokenType.ASSIGN_OPERATOR),
                        new Token(TokenType.STRING, "img"),
                        new Token(TokenType.SEMI_COLON),

                        new Token(TokenType.CONDITIONS),
                        new Token(TokenType.LEFT_BRACE),

                        new Token(TokenType.IF),
                        new Token(TokenType.PARENT),
                        new Token(TokenType.DOT),
                        new Token(TokenType.PARENT),
                        new Token(TokenType.HAS),
                        new Token(TokenType.CLASS),
                        new Token(TokenType.EQUAL),
                        new Token(TokenType.STRING,"media-content"),
                        new Token(TokenType.SEMI_COLON),

                        new Token(TokenType.IF),
                        new Token(TokenType.PARENT),
                        new Token(TokenType.HAS),
                        new Token(TokenType.ATTRIBUTE),
                        new Token(TokenType.EQUAL),
                        new Token(TokenType.STRING, "title"),
                        new Token(TokenType.SEMI_COLON),

                        new Token(TokenType.IF),
                        new Token(TokenType.THIS),
                        new Token(TokenType.HAS),
                        new Token(TokenType.ATTRIBUTE),
                        new Token(TokenType.EQUAL),
                        new Token(TokenType.STRING, "alt"),
                        new Token(TokenType.SEMI_COLON),

                        new Token(TokenType.RIGHT_BRACE),

                        new Token(TokenType.EXPORT),
                        new Token(TokenType.TO),
                        new Token(TokenType.CLASS),
                        new Token(TokenType.ASSIGN_OPERATOR),
                        new Token(TokenType.STRING, "Link"),
                        new Token(TokenType.SEMI_COLON),
                        new Token(TokenType.SET),
                        new Token(TokenType.FIELDS),
                        new Token(TokenType.LEFT_BRACE),

                        new Token(TokenType.FIELD),
                        new Token(TokenType.STRING, "link_to_image"),
                        new Token(TokenType.ASSIGN_OPERATOR),
                        new Token(TokenType.FROM),
                        new Token(TokenType.THIS),
                        new Token(TokenType.DOT),
                        new Token(TokenType.ATTRIBUTE),
                        new Token(TokenType.STRING, "src"),
                        new Token(TokenType.DOT),
                        new Token(TokenType.TEXT),
                        new Token(TokenType.SEMI_COLON),

                        new Token(TokenType.FIELD),
                        new Token(TokenType.STRING, "description"),
                        new Token(TokenType.ASSIGN_OPERATOR),
                        new Token(TokenType.FROM),
                        new Token(TokenType.THIS),
                        new Token(TokenType.DOT),
                        new Token(TokenType.ATTRIBUTE),
                        new Token(TokenType.STRING, "alt"),
                        new Token(TokenType.DOT),
                        new Token(TokenType.TEXT),
                        new Token(TokenType.SEMI_COLON),

                        new Token(TokenType.RIGHT_BRACE),

                        new Token(TokenType.AMOUNT),
                        new Token(TokenType.ASSIGN_OPERATOR),
                        new Token(TokenType.EVERY),
                        new Token(TokenType.SEMI_COLON),
                        new Token(TokenType.RIGHT_BRACE)
                        //new Token(TokenType.EOF)
                };

        List tokenList = new ArrayList<>(Arrays.asList(tokens));
        try {
            for (Object t: tokenList)
                assertEquals(lexer.getNextToken(), t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test kodu przykladu numer 3")
    public void testThirdExampleCode() throws IOException {
        File file = new File("src/main/resources/example3.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        Lexer lexer = new Lexer(new Reader(br));
        Token[] tokens = new Token[]
                {
                        new Token(TokenType.RESOURCE),
                        new Token(TokenType.LEFT_BRACE),

                        new Token(TokenType.TAG),
                        new Token(TokenType.ASSIGN_OPERATOR),
                        new Token(TokenType.STRING, "div"),
                        new Token(TokenType.SEMI_COLON),

                        new Token(TokenType.CONDITIONS),
                        new Token(TokenType.LEFT_BRACE),

                        new Token(TokenType.IF),
                        new Token(TokenType.LEFT_ROUND_BRACKET),
                        new Token(TokenType.CHILD),
                        new Token(TokenType.HAS),
                        new Token(TokenType.TAG),
                        new Token(TokenType.EQUAL),
                        new Token(TokenType.STRING, "a"),
                        new Token(TokenType.AND),
                        new Token(TokenType.HAS),
                        new Token(TokenType.ATTRIBUTE),
                        new Token(TokenType.EQUAL),
                        new Token(TokenType.STRING, "href"),
                        new Token(TokenType.RIGHT_ROUND_BRACKET),
                        new Token(TokenType.OR),
                        new Token(TokenType.LEFT_ROUND_BRACKET),
                        new Token(TokenType.CHILD),
                        new Token(TokenType.DOT),
                        new Token(TokenType.CHILD),
                        new Token(TokenType.HAS),
                        new Token(TokenType.TAG),
                        new Token(TokenType.EQUAL),
                        new Token(TokenType.STRING, "img"),
                        new Token(TokenType.AND),
                        new Token(TokenType.HAS),
                        new Token(TokenType.ATTRIBUTE),
                        new Token(TokenType.EQUAL),
                        new Token(TokenType.STRING, "src"),
                        new Token(TokenType.RIGHT_ROUND_BRACKET),
                        new Token(TokenType.SEMI_COLON),
                        new Token(TokenType.RIGHT_BRACE),

                        new Token(TokenType.EXPORT),
                        new Token(TokenType.TO),
                        new Token(TokenType.CLASS),
                        new Token(TokenType.ASSIGN_OPERATOR),
                        new Token(TokenType.STRING, "Link"),
                        new Token(TokenType.SEMI_COLON),
                        new Token(TokenType.SET),
                        new Token(TokenType.FIELDS),
                        new Token(TokenType.LEFT_BRACE),

                        new Token(TokenType.FIELD),
                        new Token(TokenType.STRING, "link_to_image"),
                        new Token(TokenType.ASSIGN_OPERATOR),
                        new Token(TokenType.FROM),
                        new Token(TokenType.TAG),
                        new Token(TokenType.STRING, "a"),
                        new Token(TokenType.NUMBER, "1"),
                        new Token(TokenType.DOT),
                        new Token(TokenType.TAG),
                        new Token(TokenType.STRING, "img"),
                        new Token(TokenType.NUMBER, "1"),
                        new Token(TokenType.DOT),
                        new Token(TokenType.ATTRIBUTE),
                        new Token(TokenType.STRING, "src"),
                        new Token(TokenType.DOT),
                        new Token(TokenType.TEXT),
                        new Token(TokenType.SEMI_COLON),

                        new Token(TokenType.FIELD),
                        new Token(TokenType.STRING, "description"),
                        new Token(TokenType.ASSIGN_OPERATOR),
                        new Token(TokenType.FROM),
                        new Token(TokenType.TAG),
                        new Token(TokenType.STRING, "a"),
                        new Token(TokenType.NUMBER, "1"),
                        new Token(TokenType.DOT),
                        new Token(TokenType.TAG),
                        new Token(TokenType.STRING, "img"),
                        new Token(TokenType.NUMBER, "1"),
                        new Token(TokenType.DOT),
                        new Token(TokenType.ATTRIBUTE),
                        new Token(TokenType.STRING, "alt"),
                        new Token(TokenType.DOT),
                        new Token(TokenType.TEXT),
                        new Token(TokenType.SEMI_COLON),

                        new Token(TokenType.RIGHT_BRACE),

                        new Token(TokenType.AMOUNT),
                        new Token(TokenType.ASSIGN_OPERATOR),
                        new Token(TokenType.EVERY),
                        new Token(TokenType.SEMI_COLON),
                        new Token(TokenType.RIGHT_BRACE),
                        //new Token(TokenType.EOF)
                };

        List tokenList = new ArrayList<>(Arrays.asList(tokens));
        try {
            for (Object t: tokenList)
                assertEquals(lexer.getNextToken(), t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
