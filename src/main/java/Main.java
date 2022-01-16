import extractor.Extractor;
import lexer.HtmlLexer;
import lexer.Lexer;
import parserhtml.structures.Root;
import reader.Reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Main {

    public static void main(String args[]) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(args[0]));

        parserhtml.Parser parser = new parserhtml.Parser(new HtmlLexer(new Reader(in)));
        parser.parse();
        Root root = parser.getRoot();

        parser.Parser resParser = new parser.Parser(new Lexer(new Reader(new BufferedReader(new FileReader(args[1])))));
        resParser.parse();

        System.out.println(example.Meta.class.getCanonicalName());

        Extractor extractor = new Extractor(resParser.getParsedResources(), root);
        extractor.extract("ress");

        ArrayList<Object> objects = extractor.getExtractedObjects();

        for(Object o : objects)
        {
            System.out.println(o.toString());
        }
    }
}
