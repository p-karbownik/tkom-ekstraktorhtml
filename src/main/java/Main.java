
import Lexer.HtmlLexer;
import ParserHTML.Parser;
import ParserHTML.Structures.Root;
import Reader.Reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;


public class Main {
    public static void main(String args[]) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(args[0]));

        Parser parser = new Parser(new HtmlLexer(new Reader(in)));
        parser.parse();
        Root root = parser.getRoot();

        System.out.println("Koniec");
    }
}
