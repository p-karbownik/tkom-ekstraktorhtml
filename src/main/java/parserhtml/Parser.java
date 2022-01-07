package parserhtml;

import exceptions.LexerException;
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

    public void parse() throws Exception
    {
        readToken();
        root = new Root(); // nie ma sensu tworzyc obiekty, gdy nie mamy jego wewnetrznych danych, obiekt powinien byc utworzony na koncu
        elementsDeque = new ArrayDeque<>();
        elementsDeque.push(root);
        parseDOCTYPE();

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

        }
        //skonczylismy parsowanie tego co sie da => sprawdzamy czy jest etx
        elementsDeque.pop(); // wyrzucenie korzenia z kolejki

        if(elementsDeque.size() != 0)
            throw new Exception();
    }

    public Root getRoot()
    {
        return root;
    }

    private boolean parseClosingTag() throws Exception
    {
        if(currentToken.getType() != TokenType.CLOSING_TAG)
            return false;

        //tutaj ifa na poczatek wrzucic, co jest w while
        readToken(TokenType.HTML_TEXT);

        String parseTagName = currentToken.getContent();
        Tag tagFromDequeue = (Tag) elementsDeque.pop();
        // moga byc problemy ze kolejnosc jest odwrocona
        if(!Objects.equals(tagFromDequeue.getName(), parseTagName))
            throw new Exception(); //
        else
            tagFromDequeue.close();

        readToken(TokenType.TAG_CLOSING_MARK);
        readToken();

        return true;
    }

    private boolean parseText() throws Exception
    {
        if(currentToken.getType() != TokenType.HTML_TEXT)
            return false;

        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append(currentToken.getContent());

        readToken();

        //mozna by miec metode w lekserze metode czytaj do napotkanego zadanego znaku
        while (currentToken.getType() == TokenType.HTML_TEXT)
        {
            textBuilder.append(currentToken.getContent());
            readToken(); //brakuje obslugi znakow z & "eskajpowanych" <- konwersja tego powinna byc w lekserze
// ma byc zywy tekst, niezakodowany dla html'a
        }

        Text text = new Text(textBuilder.toString());

        elementsDeque.peek().addChild(text);

        return true;
    }

    private void parseDOCTYPE() throws Exception //zwracanie boola czy udalo sie
    {
        if(currentToken.getType() != TokenType.DOCTYPE)
            throw new Exception(""); //przerzucenie odpowiedzialnosci za brak doctypu tu jest bledne, powinno to byc w parsowaniu calego dokumentu

        lexer.setInsideTagMode(true);
        readToken(TokenType.HTML_TEXT);

        if(currentToken.getContent().toLowerCase().compareTo("doctype") != 0)
            throw new Exception(); //ok

        readToken(TokenType.HTML_TEXT);

        if(currentToken.getContent().toLowerCase().compareTo("html") != 0)
            throw new Exception(); //ok

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
                throw new Exception();

            readToken(TokenType.QUOTE, TokenType.SINGLE_QUOTE);

            TokenType matchedTokenType = currentToken.getType();
            //lekser od html powinien miec metode do czytania tekstu, ktora pozwoli na czytanie tekstu z bialymi znakami
            //czesc wspolne kodu wydzielic do metody, ktora pozwoli na skipowanie
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
                matchedTokenType = currentToken.getType(); // wydzielic do tryParseNamespace i na koncu sprawdzic czy jest poczatek zamkniecia

                lexer.setAttributeValueReadingMode(true);
                readToken();
                readToken(matchedTokenType);
                readToken();
                if(currentToken.getType() != TokenType.TAG_CLOSING_MARK)
                    throw new Exception();

                root.addChild(new Tag("doctype"));
                lexer.setInsideTagMode(false);
            }
        }

    }

    private boolean parseOpeningTag() throws Exception
    {
        if(currentToken.getType() != TokenType.TAG_OPENER)
            return false; // tutaj wrzucic false i obedzie sie bez ifa na zewnatrz przed wywolaneim

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

        //powinno byc rekursywne wywolanie parsowania kontentu
        //to podejscie co mam, traci informacje o tym co moze byc dalej
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
            throw new Exception();

        lexer.setInsideTagMode(false);

        return true;
    }

    private ArrayList<Attribute> parseAttributes() throws Exception
    {
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
                    throw new Exception();
                else if(currentToken.getType() == TokenType.ETX)
                    throw new Exception();
                else
                    readToken();
            }

            attributes.add(new Attribute(attributeName, value));
        }

        return attributes;
    }

    private void skipScript() throws Exception
    {
        while (currentToken.getType() != TokenType.CLOSING_TAG) {
            readTokenUntilCharacters('<');
            readToken();
        }

        if(currentToken.getType() == TokenType.ETX)
            throw new Exception();

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

    private void skipStyle() throws Exception
    {
        while (currentToken.getType() != TokenType.CLOSING_TAG) {
            readTokenUntilCharacters('<');
            readToken();
        }

        if(currentToken.getType() == TokenType.ETX)
            throw new Exception();

        readToken(TokenType.HTML_TEXT);
        readToken(TokenType.TAG_CLOSING_MARK);
        readToken();
        lexer.setInsideTagMode(false);
    }


    private void readToken() throws IOException, LexerException {
        currentToken = lexer.getNextToken();
        if(currentToken != null && currentToken.getPosition() != null && currentToken.getPosition().getRow() == 2381)
            System.out.println("Jestem tuuuu");
    }

    private void readTokenUntilCharacters(Character... characters) throws IOException {
        currentToken = lexer.getHTMLTextToken(characters);
    }

    private void readToken(TokenType... tokenType) throws Exception {
        readToken();

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