import Lexer.Lexer;
import Lexer.Token;
import Lexer.TokenType;
import Lexer.Reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Main {
    public static void main(String args[]) {
        //wczytaj zadany plik
        if(args.length == 0)
        {
            System.out.println("Brak sciezki pliku wejsciowego");
            return;
        }

        File file = new File(args[0]);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            Lexer lexer = new Lexer(new Reader(br));

            while (true)
            {
                Token token = lexer.getNextToken();
                if(token.equals(new Token(TokenType.EOF)))
                    break;
                System.out.println("Token: " + token.getType().name()
                + " content: " + token.getContent() + " " + token.getPosition().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Wyswietl wszystkie tokony po kolei, jakie sa

        //zakoncz dzialanie
    }
}
