package ParserHTML;

import Exceptions.LexerException;
import Lexer.HtmlLexer;
import Lexer.Token;
import Lexer.TokenType;
import ParserHTML.Structures.*;

import java.io.IOException;
import java.util.*;

public class Parser {
    private HtmlLexer lexer;
    private Token currentToken;
    private Deque<Element> elementsDeque;
    private Root root;
    private HashMap<String, String> voidTags;

    Parser(HtmlLexer lexer)
    {
        this.lexer = lexer;
        buildVoidTagsHashMap();
    }

    public void parse() throws Exception
    {
        readToken();
        root = new Root();
        elementsDeque = new ArrayDeque<>();
        elementsDeque.push(root);
        parseDOCTYPE();

        while (currentToken.getType() != TokenType.ETX)
        {
            if(currentToken.getType() == TokenType.TAG_OPENER)
                parseTag();
            else if(currentToken.getType() == TokenType.HTML_TEXT)
                parseText();
            else if(currentToken.getType() == TokenType.CLOSING_TAG)
                parseClosingTag();
            else if(currentToken.getType() == TokenType.DOCTYPE)
                throw new Exception();
            else if(currentToken.getType() == TokenType.EMPTY_CLOSING_TAG)
                throw new Exception();
            else if(currentToken.getType() == TokenType.COMMENT_TAG_OPENER)
                skipComment();
        }

        elementsDeque.pop(); // wyrzucenie korzenia z kolejki

        if(elementsDeque.size() != 0)
            throw new Exception();
    }

    public Root getRoot()
    {
        return root;
    }

    private void parseClosingTag() throws Exception
    {
        readToken(TokenType.HTML_TEXT);

        String parseTagName = currentToken.getContent();
        Tag tagFromDequeue = (Tag) elementsDeque.pop();
        tagFromDequeue.close();

        if(!Objects.equals(tagFromDequeue.getName(), parseTagName))
            throw new Exception();

        readToken(TokenType.TAG_CLOSING_MARK);
        readToken();
    }

    private void parseText() throws Exception
    {
        StringBuilder textBuilder = new StringBuilder();

        while (currentToken.getType() == TokenType.HTML_TEXT)
        {
            textBuilder.append(currentToken.getContent());
            readToken();

            if(currentToken.getType() == TokenType.HTML_TEXT)
                textBuilder.append(" ");
        }

        Text text = new Text(textBuilder.toString());

        elementsDeque.peek().addChild(text);
    }

    private void parseDOCTYPE() throws Exception
    {
        if(currentToken.getType() != TokenType.DOCTYPE)
            throw new Exception("");

        readToken(TokenType.HTML_TEXT);

        if(currentToken.getContent().toLowerCase().compareTo("doctype") != 0)
            throw new Exception();

        readToken(TokenType.HTML_TEXT);

        if(currentToken.getContent().toLowerCase().compareTo("html") != 0)
            throw new Exception();

        readToken();

        if(currentToken.getType() == TokenType.TAG_CLOSING_MARK)
        {
            readToken();
            root.addChild(new Tag("doctype"));
        }
        else if(currentToken.getType() == TokenType.HTML_TEXT)
        {
            if(currentToken.getContent().toUpperCase().compareTo("PUBLIC") != 0)
                throw new Exception();

            readToken(TokenType.QUOTE, TokenType.SINGLE_QUOTE);

            TokenType matchedTokenType = currentToken.getType();

            do {
                readToken();
            }while (currentToken.getType() != matchedTokenType);

            readToken();

            if(currentToken.getType() == TokenType.TAG_CLOSING_MARK)
            {
                readToken();
                root.addChild(new Tag("doctype"));
            }
            else if(currentToken.getType() == TokenType.QUOTE || currentToken.getType() == TokenType.SINGLE_QUOTE)
            {
                matchedTokenType = currentToken.getType();

                do {
                        readToken();
                }while (currentToken.getType() != matchedTokenType);

                readToken();

                if(currentToken.getType() != TokenType.TAG_CLOSING_MARK)
                    throw new Exception();

                root.addChild(new Tag("doctype"));
            }
        }

    }

    private void parseTag() throws Exception
    {
        if(currentToken.getType() != TokenType.TAG_OPENER)
            return;

        readToken(TokenType.HTML_TEXT);

        String tagName = currentToken.getContent();

        if(currentToken.getContent().toLowerCase().compareTo("script") == 0)
        {
            skipScript();
            return;
        }

        readToken();

        ArrayList<Attribute> attributes = parseAttributes();

        Tag tag = new Tag(tagName, attributes);

        if(voidTags.containsKey(tagName))
            tag.close();

        if(currentToken.getType() == TokenType.TAG_CLOSING_MARK)
        {
            readToken();

            elementsDeque.peek().addChild(tag);

            if(!voidTags.containsKey(tagName))
            {
                elementsDeque.push(tag);
            }
        }

        else if(currentToken.getType() == TokenType.EMPTY_CLOSING_TAG)
        {
            readToken();

            elementsDeque.peek().addChild(tag);
            tag.close();
        }
        else
            throw new Exception();
    }

    private ArrayList<Attribute> parseAttributes() throws Exception
    {
        ArrayList<Attribute> attributes = new ArrayList<>();

        while(currentToken.getType() == TokenType.HTML_TEXT)
        {
            String attributeName = currentToken.getContent();
            ArrayList<String> values = new ArrayList<>();

            readToken();

            if(currentToken.getType() == TokenType.ASSIGN_OPERATOR)
            {
                readToken(TokenType.QUOTE, TokenType.SINGLE_QUOTE);

                TokenType matchedType = currentToken.getType();

                readToken();

                while (currentToken.getType() != matchedType)
                {
                    values.add(currentToken.getContent());
                    readToken();
                }

                if(currentToken.getType() != matchedType)
                    throw new Exception();
                else
                    readToken();
            }

            attributes.add(new Attribute(attributeName, values));
        }

        return attributes;
    }

    private void skipComment() throws Exception
    {
        if(currentToken.getType() != TokenType.COMMENT_TAG_OPENER)
            return;

        while (currentToken.getType() != TokenType.COMMENT_TAG_CLOSING && currentToken.getType() != TokenType.ETX)
        {
            readToken();
        }

        if(currentToken.getType() == TokenType.ETX)
            throw new Exception();

        if(currentToken.getType() == TokenType.COMMENT_TAG_CLOSING)
            readToken();
    }

    private void skipScript() throws Exception
    {
        while (currentToken.getType() != TokenType.ETX &&
        currentToken.getType() != TokenType.CLOSING_TAG)
            readToken();

        if(currentToken.getType() == TokenType.ETX)
            throw new Exception();

        readToken(TokenType.HTML_TEXT);
        readToken(TokenType.TAG_CLOSING_MARK);
        readToken();
    }

    private void readToken() throws IOException, LexerException {
        currentToken = lexer.getNextToken();
    }

    private void readToken(TokenType... tokenType) throws Exception {
        currentToken = lexer.getNextToken();

        boolean throwException = true;

        for(TokenType t : tokenType)
            if (currentToken.getType() == t) {
                throwException = false;
                break;
            }

        if(throwException)
            throw new Exception("");
    }

    private void buildVoidTagsHashMap()
    {
        voidTags = new HashMap<>();
        voidTags.put("area", "area");
        voidTags.put("base", "base");
        voidTags.put("br", "br");
        voidTags.put("col", "col");
        voidTags.put("command", "command");
        voidTags.put("embed", "embed");
        voidTags.put("hr", "hr");
        voidTags.put("img", "img");
        voidTags.put("input", "input");
        voidTags.put("keygen", "keygen");
        voidTags.put("link", "link");
        voidTags.put("meta", "meta");
        voidTags.put("param", "param");
        voidTags.put("source", "source");
        voidTags.put("track", "track");
        voidTags.put("wbr", "wbr");
    }

    void parseTextForTest() throws Exception
    {
        root = new Root();
        elementsDeque = new ArrayDeque<>();
        readToken();
        Tag tag = new Tag("test");
        elementsDeque.push(tag);
        root.addChild(tag);
        parseText();
    }

    ArrayList<Attribute> parseAttributesForTest() throws Exception
    {
        readToken();

        return parseAttributes();
    }

    void parseDoctypeForTest() throws Exception
    {
        readToken();
        root = new Root();
        parseDOCTYPE();
    }

    void parseTagForTest() throws Exception
    {
        readToken();
        elementsDeque = new ArrayDeque<>();
        root = new Root();
        elementsDeque.push(root);
        parseTag();
    }

    void parseClosingTagForTest() throws Exception
    {
        parseClosingTag();
    }
}
