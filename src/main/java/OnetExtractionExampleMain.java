import extractor.Extractor;
import lexer.HtmlLexer;
import lexer.Lexer;
import parserhtml.structures.Root;
import reader.Reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class OnetExtractionExampleMain {

    public static void main(String[] args) throws Exception
    {
        BufferedReader in = new BufferedReader(new FileReader("src/main/resources/onet/onet.html"));

        parserhtml.Parser parser = new parserhtml.Parser(new HtmlLexer(new Reader(in)));
        parser.parse();
        Root root = parser.getRoot();

        parser.Parser resParser = new parser.Parser(new Lexer(new Reader(new BufferedReader(new FileReader("src/main/resources/onet/onet.txt")))));
        resParser.parse();

        System.out.println("Wydobycie resA - dla amount = 0");

        Extractor extractor = new Extractor(resParser.getParsedResources(), root);
        extractor.extract("resA");

        ArrayList<Object> objects = extractor.getExtractedObjects();

        for (Object o : objects) {
            System.out.println(o.toString());
        }


        System.out.println("\nWydobycie resource liRes0 - przyklad z zagniezdzeniem resA, amount every");

        extractor.extract("liRes0");

        objects = extractor.getExtractedObjects();

        for (Object o : objects) {
            System.out.println(o.toString());
        }

        System.out.println("\nWydobycie resource liRes1 - przyklad z zagniezdzeniem resA, amount 4 ");

        extractor.extract("liRes1");

        objects = extractor.getExtractedObjects();

        for (Object o : objects) {
            System.out.println(o.toString());
        }

        System.out.println("\nWydobycie resource liRes2 - przyklad z zagniezdzeniem resA, range(2,6) ");

        extractor.extract("liRes2");

        objects = extractor.getExtractedObjects();

        for (Object o : objects) {
            System.out.println(o.toString());
        }

        System.out.println("\nWydobycie resource liRes3 - przyklad z zagniezdzeniem resA, range(3,3)  ");

        extractor.extract("liRes3");

        objects = extractor.getExtractedObjects();

        for (Object o : objects) {
            System.out.println(o.toString());
        }

        System.out.println("\nWydobycie resource headRes");

        extractor.extract("headRes");

        objects = extractor.getExtractedObjects();

        for (Object o : objects) {
            System.out.println(o.toString());
        }

        System.out.println("\nWydobycie resource divRes");

        extractor.extract("divRes");

        objects = extractor.getExtractedObjects();

        for (Object o : objects) {
            System.out.println(o.toString());
        }

        System.out.println("\nWydobycie resource divRes1");

        extractor.extract("divRes1");

        objects = extractor.getExtractedObjects();

        for (Object o : objects) {
            System.out.println(o.toString());
        }
    }
}