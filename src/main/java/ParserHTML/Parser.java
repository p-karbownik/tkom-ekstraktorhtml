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
        root = new Root(); // nie ma sensu tworzyc obiekty, gdy nie mamy jego wewnetrznych danych, obiekt powinien byc utworzony na koncu
        elementsDeque = new ArrayDeque<>();
        elementsDeque.push(root);
        parseDOCTYPE();

        while (currentToken.getType() != TokenType.ETX)
        {
            if(currentToken.getType() == TokenType.TAG_OPENER) // te ify lamia zasade jednej odpowiedzialnosci w kodzie wywalic do parse
                parseOpeningTag(); // parseTag i inne powinny zwracac, czy udalo sie przeparsowac czy nie, te ify wstawic na poczatek kazdej odpowiadajcej metody, co pozwoli na zlikwidowaniu zaleznosci while od ETX
            else if(currentToken.getType() == TokenType.HTML_TEXT)
                parseText();
            else if(currentToken.getType() == TokenType.CLOSING_TAG)
                parseClosingTag();
            /*else if(currentToken.getType() == TokenType.DOCTYPE)
                throw new Exception();
            else if(currentToken.getType() == TokenType.EMPTY_CLOSING_TAG)
                throw new Exception(); */ // do wyrzucenia pacz uwagi wyzej
            else if(currentToken.getType() == TokenType.COMMENT_TAG_OPENER)
                skipComment();
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

    private void parseClosingTag() throws Exception
    {
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
    }

    private void parseText() throws Exception
    {
        StringBuilder textBuilder = new StringBuilder();
        //mozna by miec metode w lekserze metode czytaj do napotkanego zadanego znaku
        while (currentToken.getType() == TokenType.HTML_TEXT)
        {
            textBuilder.append(currentToken.getContent());
            readToken(); //brakuje obslugi znakow z & "eskajpowanych" <- konwersja tego powinna byc w lekserze
// ma byc zywy tekst, niezakodowany dla html'a
            if(currentToken.getType() == TokenType.HTML_TEXT)
                textBuilder.append(" ");
        }

        Text text = new Text(textBuilder.toString());

        elementsDeque.peek().addChild(text);
    }

    private void parseDOCTYPE() throws Exception //zwracanie boola czy udalo sie
    {
        if(currentToken.getType() != TokenType.DOCTYPE)
            throw new Exception(""); //przerzucenie odpowiedzialnosci za brak doctypu tu jest bledne, powinno to byc w parsowaniu calego dokumentu

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
        }
        else if(currentToken.getType() == TokenType.HTML_TEXT)
        {
            if(currentToken.getContent().toUpperCase().compareTo("PUBLIC") != 0)
                throw new Exception();

            readToken(TokenType.QUOTE, TokenType.SINGLE_QUOTE);

            TokenType matchedTokenType = currentToken.getType();
            //lekser od html powinien miec metode do czytania tekstu, ktora pozwoli na czytanie tekstu z bialymi znakami
            //czesc wspolne kodu wydzielic do metody, ktora pozwoli na skipowanie
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
                matchedTokenType = currentToken.getType(); // wydzielic do tryParseNamespace i na koncu sprawdzic czy jest poczatek zamkniecia

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

    private void parseOpeningTag() throws Exception
    {
        if(currentToken.getType() != TokenType.TAG_OPENER)
            return; // tutaj wrzucic false i obedzie sie bez ifa na zewnatrz przed wywolaneim

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

        //powinno byc rekursywne wywolanie parsowania kontentu
        //to podejscie co mam, traci informacje o tym co moze byc dalej
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
