package Reader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class ReaderTest
{
    @Test
    @DisplayName("Test dla tekstu ze znakiem nastepnej linii")
    public void firstTest() throws IOException {
        String text = "abcd\nef";
        Reader reader = new Reader(new BufferedReader(new StringReader(text)));
        reader.readCharacter();
        char character1 = reader.getCurrentCharacter();

        assertAll("First character",
                () -> assertEquals(character1, 'a'),
                () -> assertEquals(new Position(1,1), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character2 = reader.getCurrentCharacter();

        assertAll("Second character",
                () -> assertEquals(character2, 'b'),
                () -> assertEquals(new Position(1,2), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character3 = reader.getCurrentCharacter();

        assertAll("Third character",
                () -> assertEquals(character3, 'c'),
                () -> assertEquals(new Position(1,3), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character4 = reader.getCurrentCharacter();

        assertAll("Fourth character",
                () -> assertEquals(character4, 'd'),
                () -> assertEquals(new Position(1,4), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character5 = reader.getCurrentCharacter();

        assertAll("Fifth character",
                () -> assertEquals(character5, '\n'),
                () -> assertEquals(new Position(1,5), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character6 = reader.getCurrentCharacter();

        assertAll("Sixth character",
                () -> assertEquals(character6, 'e'),
                () -> assertEquals(new Position(2,1), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character = reader.getCurrentCharacter();

        assertAll("Seventh character",
                () -> assertEquals(character, 'f'),
                () -> assertEquals(new Position(2,2), reader.getCurrentPosition())
        );
    }

    @Test
    @DisplayName("Test dla tekstu ze sekwencją \\n\\r")
    public void secondTest() throws IOException {
        String text = "abcd\n\ref";
        Reader reader = new Reader(new BufferedReader(new StringReader(text)));
        reader.readCharacter();
        char character1 = reader.getCurrentCharacter();

        assertAll("First character",
                () -> assertEquals(character1, 'a'),
                () -> assertEquals(new Position(1,1), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character2 = reader.getCurrentCharacter();

        assertAll("Second character",
                () -> assertEquals(character2, 'b'),
                () -> assertEquals(new Position(1,2), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character3 = reader.getCurrentCharacter();

        assertAll("Third character",
                () -> assertEquals(character3, 'c'),
                () -> assertEquals(new Position(1,3), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character4 = reader.getCurrentCharacter();

        assertAll("Fourth character",
                () -> assertEquals(character4, 'd'),
                () -> assertEquals(new Position(1,4), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character5 = reader.getCurrentCharacter();

        assertAll("Fifth character",
                () -> assertEquals(character5, '\n'),
                () -> assertEquals(new Position(1,5), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character6 = reader.getCurrentCharacter();

        assertAll("Sixth character",
                () -> assertEquals(character6, '\r'),
                () -> assertEquals(new Position(2, 1), reader.getCurrentPosition()));

        reader.readCharacter();
        char character7 = reader.getCurrentCharacter();

        assertAll("Sixth character",
                () -> assertEquals(character7, 'e'),
                () -> assertEquals(new Position(2,1), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character8 = reader.getCurrentCharacter();

        assertAll("Seventh character",
                () -> assertEquals(character8, 'f'),
                () -> assertEquals(new Position(2,2), reader.getCurrentPosition())
        );
    }

    @Test
    @DisplayName("Test dla tekstu z sekwencją \\r\\n")
    public void thirdTest() throws IOException {
        String text = "abcd\r\nef";
        Reader reader = new Reader(new BufferedReader(new StringReader(text)));
        reader.readCharacter();
        char character1 = reader.getCurrentCharacter();

        assertAll("First character",
                () -> assertEquals(character1, 'a'),
                () -> assertEquals(new Position(1,1), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character2 = reader.getCurrentCharacter();

        assertAll("Second character",
                () -> assertEquals(character2, 'b'),
                () -> assertEquals(new Position(1,2), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character3 = reader.getCurrentCharacter();

        assertAll("Third character",
                () -> assertEquals(character3, 'c'),
                () -> assertEquals(new Position(1,3), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character4 = reader.getCurrentCharacter();

        assertAll("Fourth character",
                () -> assertEquals(character4, 'd'),
                () -> assertEquals(new Position(1,4), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character5 = reader.getCurrentCharacter();

        assertAll("Fifth character",
                () -> assertEquals(character5, '\r'),
                () -> assertEquals(new Position(1, 5), reader.getCurrentPosition()));

        reader.readCharacter();
        char character6 = reader.getCurrentCharacter();

        assertAll("Fifth character",
                () -> assertEquals(character6, '\n'),
                () -> assertEquals(new Position(2,1), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character7 = reader.getCurrentCharacter();

        assertAll("Sixth character",
                () -> assertEquals(character7, 'e'),
                () -> assertEquals(new Position(2,1), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character8 = reader.getCurrentCharacter();

        assertAll("Seventh character",
                () -> assertEquals(character8, 'f'),
                () -> assertEquals(new Position(2,2), reader.getCurrentPosition())
        );
    }

    @Test
    @DisplayName("Test dla tekstu ze znakiem tabulatora")
    public void fourthTest() throws IOException{
        String text = "abcd\tef";
        Reader reader = new Reader(new BufferedReader(new StringReader(text)));
        reader.readCharacter();

        char character1 = reader.getCurrentCharacter();

        assertAll("First character",
                () -> assertEquals(character1, 'a'),
                () -> assertEquals(new Position(1,1), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character2 = reader.getCurrentCharacter();
        assertAll("Second character",
                () -> assertEquals(character2, 'b'),
                () -> assertEquals(new Position(1,2), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character3 = reader.getCurrentCharacter();
        assertAll("Third character",
                () -> assertEquals(character3, 'c'),
                () -> assertEquals(new Position(1,3), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character4 = reader.getCurrentCharacter();
        assertAll("Fourth character",
                () -> assertEquals(character4, 'd'),
                () -> assertEquals(new Position(1,4), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character5 = reader.getCurrentCharacter();
        assertAll("Fifth character",
                () -> assertEquals(character5, '\t'),
                () -> assertEquals(new Position(1,5), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character6 = reader.getCurrentCharacter();
        assertAll("Sixth character",
                () -> assertEquals(character6, 'e'),
                () -> assertEquals(new Position(1,6), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character7 = reader.getCurrentCharacter();
        assertAll("Seventh character",
                () -> assertEquals(character7, 'f'),
                () -> assertEquals(new Position(1,7), reader.getCurrentPosition())
        );
    }

    @Test
    @DisplayName("Test dla teskstu ze znakiem powrotu karetki")
    public void fifthTest() throws IOException{
        String text = "abcd\ref";
        Reader reader = new Reader(new BufferedReader(new StringReader(text)));
        reader.readCharacter();

        char character1 = reader.getCurrentCharacter();
        assertAll("First character",
                () -> assertEquals(character1, 'a'),
                () -> assertEquals(new Position(1,1), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character2 = reader.getCurrentCharacter();
        assertAll("Second character",
                () -> assertEquals(character2, 'b'),
                () -> assertEquals(new Position(1, 2), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character3 = reader.getCurrentCharacter();
        assertAll("Third character",
                () -> assertEquals(character3, 'c'),
                () -> assertEquals(new Position(1,3), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character4 = reader.getCurrentCharacter();
        assertAll("Fourth character",
                () -> assertEquals(character4, 'd'),
                () -> assertEquals(new Position(1,4), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character5 = reader.getCurrentCharacter();
        assertAll("Fifth character",
                () -> assertEquals(character5, '\r'),
                () -> assertEquals(new Position(1,5), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character6 = reader.getCurrentCharacter();
        assertAll("Sixth character",
                () -> assertEquals(character6, 'e'),
                () -> assertEquals(new Position(2,1), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character7 = reader.getCurrentCharacter();
        assertAll("Seventh character",
                () -> assertEquals(character7, 'f'),
                () -> assertEquals(new Position(2,2), reader.getCurrentPosition())
        );
    }

    @Test
    @DisplayName("Test dla tekstu ze wszystkimi wyzej wyminionymi znakami")
    public void sixthTest() throws IOException{
        String text = "ab\ncd\ref\tgh";
        Reader reader = new Reader(new BufferedReader(new StringReader(text)));
        reader.readCharacter();

        char character1 = reader.getCurrentCharacter();
        assertAll("First character",
                () -> assertEquals(character1, 'a'),
                () -> assertEquals(new Position(1,1), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character2 = reader.getCurrentCharacter();
        assertAll("Second character",
                () -> assertEquals(character2, 'b'),
                () -> assertEquals(new Position(1,2), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character3 = reader.getCurrentCharacter();
        assertAll("Third character",
                () -> assertEquals(character3, '\n'),
                () -> assertEquals(new Position(1,3), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character4 = reader.getCurrentCharacter();
        assertAll("Fourth character",
                () -> assertEquals(character4, 'c'),
                () -> assertEquals(new Position(2,1), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character5 = reader.getCurrentCharacter();
        assertAll("Fifth character",
                () -> assertEquals(character5, 'd'),
                () -> assertEquals(new Position(2,2), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character6 = reader.getCurrentCharacter();
        assertAll("Sixth character",
                () -> assertEquals(character6, '\r'),
                () -> assertEquals(new Position(2,3), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character7 = reader.getCurrentCharacter();
        assertAll("Seventh character",
                () -> assertEquals(character7, 'e'),
                () -> assertEquals(new Position(3,1), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character8 = reader.getCurrentCharacter();
        assertAll("Eighth character",
                () -> assertEquals(character8, 'f'),
                () -> assertEquals(new Position(3,2), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character9 = reader.getCurrentCharacter();
        assertAll("Ninth character",
                () -> assertEquals(character9, '\t'),
                () -> assertEquals(new Position(3,3), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character10 = reader.getCurrentCharacter();
        assertAll("Tenth character",
                () -> assertEquals(character10, 'g'),
                () -> assertEquals(new Position(3,4), reader.getCurrentPosition())
        );

        reader.readCharacter();

        char character11 = reader.getCurrentCharacter();
        assertAll("Eleventh character",
                () -> assertEquals(character11, 'h'),
                () -> assertEquals(new Position(3,5), reader.getCurrentPosition())
        );

    }

    @Test
    @DisplayName("Test dla tekstu z sekwencją \\036")
    public void seventhTest() throws IOException {
        String text = "abcd\036ef";
        Reader reader = new Reader(new BufferedReader(new StringReader(text)));
        reader.readCharacter();
        char character1 = reader.getCurrentCharacter();

        assertAll("First character",
                () -> assertEquals(character1, 'a'),
                () -> assertEquals(new Position(1,1), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character2 = reader.getCurrentCharacter();

        assertAll("Second character",
                () -> assertEquals(character2, 'b'),
                () -> assertEquals(new Position(1,2), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character3 = reader.getCurrentCharacter();

        assertAll("Third character",
                () -> assertEquals(character3, 'c'),
                () -> assertEquals(new Position(1,3), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character4 = reader.getCurrentCharacter();

        assertAll("Fourth character",
                () -> assertEquals(character4, 'd'),
                () -> assertEquals(new Position(1,4), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character5 = reader.getCurrentCharacter();

        assertAll("Fifth character",
                () -> assertEquals(character5, '\036'),
                () -> assertEquals(new Position(1, 5), reader.getCurrentPosition()));

        reader.readCharacter();
        char character6 = reader.getCurrentCharacter();

        assertAll("Sixth character",
                () -> assertEquals(character6, 'e'),
                () -> assertEquals(new Position(2,1), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character7 = reader.getCurrentCharacter();

        assertAll("Seventh character",
                () -> assertEquals(character7, 'f'),
                () -> assertEquals(new Position(2,2), reader.getCurrentPosition())
        );
    }

    @Test
    @DisplayName("Test dla tekstu z sekwencją \\025")
    public void eightTest() throws IOException {
        String text = "abcd\025ef";
        Reader reader = new Reader(new BufferedReader(new StringReader(text)));
        reader.readCharacter();
        char character1 = reader.getCurrentCharacter();

        assertAll("First character",
                () -> assertEquals(character1, 'a'),
                () -> assertEquals(new Position(1,1), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character2 = reader.getCurrentCharacter();

        assertAll("Second character",
                () -> assertEquals(character2, 'b'),
                () -> assertEquals(new Position(1,2), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character3 = reader.getCurrentCharacter();

        assertAll("Third character",
                () -> assertEquals(character3, 'c'),
                () -> assertEquals(new Position(1,3), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character4 = reader.getCurrentCharacter();

        assertAll("Fourth character",
                () -> assertEquals(character4, 'd'),
                () -> assertEquals(new Position(1,4), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character5 = reader.getCurrentCharacter();

        assertAll("Fifth character",
                () -> assertEquals(character5, '\025'),
                () -> assertEquals(new Position(1, 5), reader.getCurrentPosition()));

        reader.readCharacter();
        char character6 = reader.getCurrentCharacter();

        assertAll("Sixth character",
                () -> assertEquals(character6, 'e'),
                () -> assertEquals(new Position(2,1), reader.getCurrentPosition())
        );

        reader.readCharacter();
        char character7 = reader.getCurrentCharacter();

        assertAll("Seventh character",
                () -> assertEquals(character7, 'f'),
                () -> assertEquals(new Position(2,2), reader.getCurrentPosition())
        );
    }
}
