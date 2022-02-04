import example.ImageClass;
import extractor.Extractor;
import lexer.HtmlLexer;
import lexer.Lexer;
import parserhtml.structures.Root;
import reader.Reader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.File;
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

        Extractor extractor = new Extractor(resParser.getParsedResources(), root);
        extractor.extract("liRes3");

        ArrayList<Object> objects = extractor.getExtractedObjects();

        for(Object o : objects)
        {
            System.out.println(o.toString());
        }


    }
}
