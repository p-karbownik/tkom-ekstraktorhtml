package parserhtml;

import exceptions.lexer.LexerException;
import exceptions.parser.DoctypeException;
import exceptions.parser.ElementsDequeException;
import exceptions.parser.TagClosingException;
import exceptions.parser.UnexpectedTokenException;
import lexer.HtmlLexer;
import lexer.Token;
import lexer.TokenType;
import parserhtml.structures.*;

import java.io.IOException;
import java.util.*;

public class Parser {
    private HtmlLexer lexer;
    private Token currentToken;
    private Deque<Element> elementsDeque;
    private Root root;
    private HashMap<String, String> voidTags;

    public Parser(HtmlLexer lexer)
    {
        this.lexer = lexer;
        buildVoidTagsHashMap();
    }

    public void parse() throws DoctypeException, UnexpectedTokenException, IOException, LexerException, TagClosingException, ElementsDequeException {
        readToken();
        root = new Root();
        elementsDeque = new ArrayDeque<>();
        elementsDeque.push(root);

        if(!parseDOCTYPE())
            throw new DoctypeException(currentToken.getPosition());

        boolean result = true;

        while (result)
        {
            result = false;

            if(currentToken.getType() == TokenType.COMMENT) {
                readToken();
                result = true;
                continue;
            }

            result = parseOpeningTag();

            if(!result)
                result = parseClosingTag();
            if(!result)
                result = parseText();
            if(!result && currentToken.getType() != TokenType.ETX)
                throw new UnexpectedTokenException(currentToken.getType(), currentToken.getPosition());

        }

        elementsDeque.pop(); // wyrzucenie korzenia z kolejki

        if(elementsDeque.size() != 0)
            throw new ElementsDequeException(elementsDeque.size() - 1);

        setParent(root);
    }

    public Root getRoot()
    {
        return root;
    }

    private boolean parseClosingTag() throws UnexpectedTokenException, IOException, LexerException, TagClosingException {
        if(currentToken.getType() != TokenType.CLOSING_TAG)
            return false;

        readToken(TokenType.HTML_TEXT);

        String parsedTagName = currentToken.getContent();
        Tag tagFromDequeue = (Tag) elementsDeque.pop();

        if(!Objects.equals(tagFromDequeue.getName(), parsedTagName))
            throw new TagClosingException(tagFromDequeue.getName(), parsedTagName, currentToken.getPosition());
        else
            tagFromDequeue.close();

        readToken(TokenType.TAG_CLOSING_MARK);
        readToken();

        return true;
    }

    private boolean parseText() throws IOException, LexerException {
        if(currentToken.getType() != TokenType.HTML_TEXT)
            return false;

        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append(currentToken.getContent());

        readToken();

        while (currentToken.getType() == TokenType.HTML_TEXT)
        {
            textBuilder.append(currentToken.getContent());
            readToken();
        }

        Text text = new Text(textBuilder.toString());

        elementsDeque.peek().addChild(text);

        return true;
    }

    private boolean parseDOCTYPE() throws UnexpectedTokenException, IOException, LexerException {
        if(currentToken.getType() != TokenType.DOCTYPE)
            return false;

        lexer.setInsideTagMode(true);
        readToken(TokenType.HTML_TEXT);

        if(currentToken.getContent().toLowerCase().compareTo("doctype") != 0)
            return false; //ok

        readToken(TokenType.HTML_TEXT);

        if(currentToken.getContent().toLowerCase().compareTo("html") != 0)
            return false; //ok

        readToken();

        if(currentToken.getType() == TokenType.TAG_CLOSING_MARK)
        {
            readToken();
            root.addChild(new Tag("doctype"));
            lexer.setInsideTagMode(false);
        }
        else if(currentToken.getType() == TokenType.HTML_TEXT)
        {
            if(currentToken.getContent().toUpperCase().compareTo("PUBLIC") != 0)
                return false;

            readToken(TokenType.QUOTE, TokenType.SINGLE_QUOTE);

            TokenType matchedTokenType = currentToken.getType();

            lexer.setAttributeValueReadingMode(true);

            readToken();

            readToken(matchedTokenType);

            readToken();
            if(currentToken.getType() == TokenType.TAG_CLOSING_MARK)
            {
                readToken();
                root.addChild(new Tag("doctype"));
                lexer.setInsideTagMode(false);
            }
            else if(currentToken.getType() == TokenType.QUOTE || currentToken.getType() == TokenType.SINGLE_QUOTE)
            {
                matchedTokenType = currentToken.getType();

                lexer.setAttributeValueReadingMode(true);
                readToken();
                readToken(matchedTokenType);
                readToken();
                if(currentToken.getType() != TokenType.TAG_CLOSING_MARK)
                    return false;

                root.addChild(new Tag("doctype"));
                lexer.setInsideTagMode(false);
            }
        }

        return true;
    }

    private boolean parseOpeningTag() throws UnexpectedTokenException, IOException, LexerException {
        if(currentToken.getType() != TokenType.TAG_OPENER)
            return false;

        lexer.setInsideTagMode(true);
        readToken(TokenType.HTML_TEXT);

        String tagName = currentToken.getContent();

        if(currentToken.getContent().toLowerCase().compareTo("script") == 0)
        {
            skipScript();
            return true;
        }

        if(currentToken.getContent().toLowerCase().compareTo("style") == 0)
        {
            skipStyle();
            return true;
        }

        readToken();

        ArrayList<Attribute> attributes = parseAttributes();

        Tag tag = new Tag(tagName, attributes);

        if(voidTags.containsKey(tagName))
            tag.close();

        if(currentToken.getType() == TokenType.TAG_CLOSING_MARK)
        {
            lexer.setInsideTagMode(false);
            readToken();

            elementsDeque.peek().addChild(tag);

            if(!voidTags.containsKey(tagName))
            {
                elementsDeque.push(tag);
            }
        }

        else if(currentToken.getType() == TokenType.EMPTY_CLOSING_TAG)
        {
            lexer.setInsideTagMode(false);
            readToken();

            elementsDeque.peek().addChild(tag);
            tag.close();
        }
        else
            throw new UnexpectedTokenException(currentToken.getType(), currentToken.getPosition(), TokenType.EMPTY_CLOSING_TAG, TokenType.TAG_CLOSING_MARK);

        lexer.setInsideTagMode(false);

        return true;
    }

    private ArrayList<Attribute> parseAttributes() throws IOException, LexerException, UnexpectedTokenException {
        ArrayList<Attribute> attributes = new ArrayList<>();

        while(currentToken.getType() == TokenType.HTML_TEXT)
        {
            String attributeName = currentToken.getContent();
            String value = null;
            readToken();

            if(currentToken.getType() == TokenType.ASSIGN_OPERATOR)
            {
                readToken(TokenType.QUOTE, TokenType.SINGLE_QUOTE);

                TokenType matchedType = currentToken.getType();

                lexer.setAttributeValueReadingMode(true);
                readToken();

                value = currentToken.getContent();

                readToken();

                if(currentToken.getType() != matchedType)
                    throw new UnexpectedTokenException(currentToken.getType(), currentToken.getPosition(), matchedType);
                else if(currentToken.getType() == TokenType.ETX)
                    throw new UnexpectedTokenException(TokenType.ETX, currentToken.getPosition());
                else
                    readToken();
            }

            attributes.add(new Attribute(attributeName, value));
        }

        return attributes;
    }

    private void skipScript() throws UnexpectedTokenException, IOException, LexerException {
        while (currentToken.getType() != TokenType.CLOSING_TAG && currentToken.getType() != TokenType.ETX) {
            readTokenUntilCharacters('<');
            readToken();
        }

        if(currentToken.getType() == TokenType.ETX)
            throw new UnexpectedTokenException(TokenType.ETX, currentToken.getPosition());

        readToken(TokenType.HTML_TEXT);

        if(currentToken.getContent().compareTo("script") == 0)
        {
            readToken(TokenType.TAG_CLOSING_MARK);
            readToken();
        }
        else
            skipScript();

        lexer.setInsideTagMode(false);
    }

    private void skipStyle() throws UnexpectedTokenException, IOException, LexerException {
        while (currentToken.getType() != TokenType.CLOSING_TAG && currentToken.getType() != TokenType.ETX) {
            readTokenUntilCharacters('<');
            readToken();
        }

        if(currentToken.getType() == TokenType.ETX)
            throw new UnexpectedTokenException(TokenType.ETX, currentToken.getPosition());

        readToken(TokenType.HTML_TEXT);
        readToken(TokenType.TAG_CLOSING_MARK);
        readToken();
        lexer.setInsideTagMode(false);
    }

    private void readToken() throws IOException, LexerException {
        currentToken = lexer.getNextToken();
    }

    private void readTokenUntilCharacters(Character... characters) throws IOException {
        currentToken = lexer.getHTMLTextToken(characters);
    }

    private void readToken(TokenType... tokenType) throws UnexpectedTokenException, IOException, LexerException {
        readToken();

        boolean throwException = true;

        for(TokenType t : tokenType)
            if (currentToken.getType() == t) {
                throwException = false;
                break;
            }

        if(throwException)
            throw new UnexpectedTokenException(currentToken.getType(), currentToken.getPosition(), tokenType);
    }

    private void setParent(Element root)
    {
        if(root instanceof Root)
        {
            for(Element e : ((Root) root).getChildren())
            {
                e.setParent(root);

                if(e instanceof Tag)
                    setParent(e);
            }
        }
        else if(root instanceof Tag)
        {
            for(Element e : ((Tag) root).getChildren())
            {
                e.setParent(root);

                if(e instanceof Tag)
                    setParent(e);
            }
        }
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
        lexer.setInsideTagMode(true);
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
        parseOpeningTag();
    }

    void parseClosingTagForTest() throws Exception
    {
        parseClosingTag();
    }
}
