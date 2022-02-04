package parserhtml;

import lexer.HtmlLexer;
import parserhtml.structures.*;
import reader.Reader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {

    private HtmlLexer getLexer(String s) throws IOException {
        BufferedReader br = new BufferedReader(new StringReader(s));
        return new HtmlLexer(new Reader(br));
    }

    @Test
    @DisplayName("Test parsowania tekstu")
    public void parseHtmlTextTest() throws Exception {
        HtmlLexer lexer = getLexer("Very serious text");
        Parser parser = new Parser(lexer);

        parser.parseTextForTest();

        Root root = new Root();
        Tag t = new Tag("test");
        Text text = new Text("Very serious text");
        t.addChild(text);
        root.addChild(t);
        Root parsedRoot = parser.getRoot();

        assertEquals(root, parser.getRoot());
    }

    @Test
    @DisplayName("Test parsowania atrybutów")
    public void parseAttributesTest() throws Exception {
        HtmlLexer lexer = getLexer("src=\"img_girl.jpg\" width=\"500\" height=\"600\"");
        Parser parser = new Parser(lexer);

        ArrayList<Attribute> parsedAttributesArrayList = parser.parseAttributesForTest();
        ArrayList<Attribute> expectedAttributes = new ArrayList<>();

        String value = "img_girl.jpg";
        expectedAttributes.add(new Attribute("src", value));

        String value1 = "500";
        expectedAttributes.add(new Attribute("width", value1));

        String value2 = "600";
        expectedAttributes.add(new Attribute("height", value2));

        assertEquals(expectedAttributes, parsedAttributesArrayList);
    }

    @Test
    @DisplayName("Test parsowania doctype nr1")
    public void parseDoctypeTest1() throws Exception {
        HtmlLexer lexer = getLexer("<!DOCTYPE html>");
        Parser parser = new Parser(lexer);
        parser.parseDoctypeForTest();
        Root parsedRoot = parser.getRoot();

        Tag tag = new Tag("doctype");

        Root expectedRoot = new Root();
        expectedRoot.addChild(tag);

        assertEquals(expectedRoot, parsedRoot);
    }

    @Test
    @DisplayName("Test parsowania doctype nr 2")
    public void parseDoctypeTest2() throws Exception {
        HtmlLexer lexer = getLexer("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
        Parser parser = new Parser(lexer);
        parser.parseDoctypeForTest();
        Root parsedRoot = parser.getRoot();

        Tag tag = new Tag("doctype");

        Root expectedRoot = new Root();
        expectedRoot.addChild(tag);

        assertEquals(expectedRoot, parsedRoot);
    }

    @Test
    @DisplayName("Test parsowania doctype nr 3")
    public void parseDoctypeTest3() throws Exception {
        HtmlLexer lexer = getLexer("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        Parser parser = new Parser(lexer);
        parser.parseDoctypeForTest();
        Root parsedRoot = parser.getRoot();

        Tag tag = new Tag("doctype");

        Root expectedRoot = new Root();
        expectedRoot.addChild(tag);

        assertEquals(expectedRoot, parsedRoot);
    }

    @Test
    @DisplayName("Test void tag z zamknieciem />")
    public void parseTagTest1() throws Exception
    {
        HtmlLexer lexer = getLexer("<area />");
        Parser parser = new Parser(lexer);

        parser.parseTagForTest();
        Root parsedRoot = parser.getRoot();
        Root expectedRoot = new Root();
        ArrayList<Attribute> attributes = new ArrayList<>();

        Tag tag = new Tag("area", attributes);
        tag.close();

        expectedRoot.addChild(tag);

        assertEquals(expectedRoot, parsedRoot);
    }

    @Test
    @DisplayName("Test void tag z normalnym zamknieciem")
    public void parseTagTest2() throws Exception
    {
        HtmlLexer lexer = getLexer("<area>");
        Parser parser = new Parser(lexer);

        parser.parseTagForTest();
        Root parsedRoot = parser.getRoot();
        Root expectedRoot = new Root();
        ArrayList<Attribute> attributes = new ArrayList<>();

        Tag tag = new Tag("area", attributes);
        tag.close();

        expectedRoot.addChild(tag);

        assertEquals(expectedRoot, parsedRoot);
    }

    @Test
    @DisplayName("Test samego tagu bez atrybutow")
    public void parseTagTest3() throws Exception
    {
        HtmlLexer lexer = getLexer("<html>");
        Parser parser = new Parser(lexer);

        parser.parseTagForTest();
        Root parsedRoot = parser.getRoot();
        Root expectedRoot = new Root();
        ArrayList<Attribute> attributes = new ArrayList<>();

        Tag tag = new Tag("html", attributes);

        expectedRoot.addChild(tag);

        assertEquals(expectedRoot, parsedRoot);
    }

    @Test
    @DisplayName("Test samego tagu bez atrybutow z odpowiadajacym mu tagiem zamykajacym")
    public void parseTagTest4() throws Exception
    {
        HtmlLexer lexer = getLexer("<html></html>");
        Parser parser = new Parser(lexer);

        parser.parseTagForTest();
        parser.parseClosingTagForTest();

        Root parsedRoot = parser.getRoot();
        Root expectedRoot = new Root();
        ArrayList<Attribute> attributes = new ArrayList<>();

        Tag tag = new Tag("html", attributes);
        tag.close();

        expectedRoot.addChild(tag);

        assertEquals(expectedRoot, parsedRoot);
    }

    @Test
    @DisplayName("Test parsowania tagu z atrybutami")
    public void parseTagTest5() throws Exception
    {
        HtmlLexer lexer = getLexer("<a href=\"https://www.w3schools.com\">");
        Parser parser = new Parser(lexer);

        parser.parseTagForTest();

        Root parsedRoot = parser.getRoot();
        Root expectedRoot = new Root();

        ArrayList<Attribute> attributes = new ArrayList<>();
        String value = "https://www.w3schools.com";
        attributes.add(new Attribute("href", value));

        Tag tag = new Tag("a", attributes);
        expectedRoot.addChild(tag);

        assertEquals(expectedRoot, parsedRoot);
    }

    @Test
    @DisplayName("Test parsowania całego prostego dokumentu")
    public void parseHTMLDocumentTest() throws Exception
    {
        String documentText =
                "<!DOCTYPE html>\n" +
                "<!-- komentarz -->\n" +
                "<script> jakis skrypt </script>" +
                "<html>\n" +
                "<head>\n" +
                "<title>Title of the document</title>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "The content of the document......\n" +
                "</body>\n" +
                "\n" +
                "</html>";

        HtmlLexer lexer = getLexer(documentText);
        Parser parser = new Parser(lexer);
        parser.parse();

        Root parsedRoot = parser.getRoot();
        Root expectedRoot = new Root();

        Text textInTitle = new Text("Title of the document");
        Text textInBody = new Text("The content of the document......\n");


        Tag doctypeTag = new Tag("doctype");
        Tag htmlTag = new Tag("html", new ArrayList<>());
        Tag headTag = new Tag("head", new ArrayList<>());
        Tag titleTag = new Tag("title", new ArrayList<>());

        Tag bodyTag = new Tag("body", new ArrayList<>());

        titleTag.addChild(textInTitle);
        bodyTag.addChild(textInBody);
        htmlTag.addChild(headTag);
        headTag.addChild(titleTag);
        htmlTag.addChild(bodyTag);
        expectedRoot.addChild(doctypeTag);
        expectedRoot.addChild(htmlTag);
        titleTag.close();
        bodyTag.close();
        htmlTag.close();
        headTag.close();

        assertEquals(expectedRoot, parsedRoot);
    }

}
