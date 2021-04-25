import Lexer.Lexer;
import Lexer.Token;
import java.io.*;

public class Main {
    public static void main(String args[]) {
        //wczytaj zadany plik
        File file = new File("src/main/resources/code.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            Lexer lexer = new Lexer(br);

            while (true)
            {
                Token token = lexer.getNextToken();
                System.out.println("Token: " + token.getType().name()
                + " content: " + token.getContent() + " " +token.getPosition().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Wyswietl wszystkie tokony po kolei, jakie sa

        //zakoncz dzialanie
    }
}
