import example.ImageMeta;
import extractor.Extractor;
import lexer.HtmlLexer;
import lexer.Lexer;
import parserhtml.structures.Root;
import reader.Reader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class RKExtractionExampleMain {
    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader("src/main/resources/rynek_kolejowy/rynek_kolejowy.html"));

        parserhtml.Parser parser = new parserhtml.Parser(new HtmlLexer(new Reader(in)));
        parser.parse();
        Root root = parser.getRoot();

        parser.Parser resParser = new parser.Parser(new Lexer(new Reader(new BufferedReader(new FileReader("src/main/resources/rynek_kolejowy/rynek_kolejowy.txt")))));
        resParser.parse();

        System.out.println("Przyklad ekstrakcji obrazu i zapisuj do pliku");

        Extractor extractor = new Extractor(resParser.getParsedResources(), root);
        extractor.extract("imageRes");

        ArrayList<Object> objects = extractor.getExtractedObjects();

        for (Object o : objects) {
            System.out.println(o.toString());
        }

        //zapisanie obrazku

        BufferedImage bi = ((ImageMeta) objects.get(0)).getImage();
        File outputfile = new File("saved.png");
        ImageIO.write(bi, "png", outputfile);

        System.out.println("\nWydobycie resource metaTextRes");

        extractor.extract("metaTextRes");

        objects = extractor.getExtractedObjects();

        for (Object o : objects) {
            System.out.println(o.toString());
        }

        System.out.println("\nWydobycie resource metaTextRes1 - metaTextRes z ograniczeniem self has attribute name == \"description\" ");

        extractor.extract("metaTextRes1");

        objects = extractor.getExtractedObjects();

        for (Object o : objects) {
            System.out.println(o.toString());
        }

        System.out.println("\nWydobycie resource metaTextRes2 - metaTextRes z ograniczeniem self has attribute name != \"description\" ");

        extractor.extract("metaTextRes2");

        objects = extractor.getExtractedObjects();

        for (Object o : objects) {
            System.out.println(o.toString());
        }

        System.out.println("\nWydobycie resource metaTextRes3 - metaTextRes z ograniczeniem self has attribute name in {\"description\", \"title\"} ");

        extractor.extract("metaTextRes3");

        objects = extractor.getExtractedObjects();

        for (Object o : objects) {
            System.out.println(o.toString());
        }

        System.out.println("\nWydobycie resource liTextRes");

        extractor.extract("liTextRes");

        objects = extractor.getExtractedObjects();

        for (Object o : objects) {
            System.out.println(o.toString());
        }
    }
}
