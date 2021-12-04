package Reader;

import java.io.BufferedReader;
import java.io.IOException;

public class Reader
{
    private final static int startRow = 1;
    private final static int startColumn = 0;
//    private final static int tabLength = 8; //mozna olac problem tabulacji - bo edytory roznie na to pacza

    private Position readerPosition;
    private final BufferedReader inputReader;
    private char currentCharacter;
    private char escapeSequenceFirstChar;

    public Reader(BufferedReader inputReader)
    {
        this.inputReader = inputReader;
        readerPosition = new Position(startRow, startColumn);
        escapeSequenceFirstChar = 0;
    }

    public void readCharacter() throws IOException {

        if (currentCharacter == '\036' || currentCharacter == '\025')
        {
            readerPosition.setRow(readerPosition.getRow() + 1);
            readerPosition.setColumn(startColumn);
        }

        else if(currentCharacter == '\n')
        {
            if(escapeSequenceFirstChar != '\r')
            {
                readerPosition.setRow(readerPosition.getRow() + 1);
                readerPosition.setColumn(startColumn);
                escapeSequenceFirstChar = '\n';
            }

            else {
                readerPosition.setColumn(startColumn);
                escapeSequenceFirstChar = 0;
            }
        }

        else if( currentCharacter == '\r')
        {
            if(escapeSequenceFirstChar != '\n')
            {
                readerPosition.setColumn(startColumn);
                readerPosition.setRow(readerPosition.getRow() + 1);
                escapeSequenceFirstChar = '\r';
            }
            else
            {
                readerPosition.setColumn(startColumn);
                escapeSequenceFirstChar = 0;
            }
        }

        else
            escapeSequenceFirstChar = 0;

        // obsluga new line https://en.wikipedia.org/wiki/Newline
        currentCharacter = (char) inputReader.read(); // zmienic na etx (3) dla -1

        if(currentCharacter == 0xffff)
            currentCharacter = 3; //etx

        readerPosition.setColumn(readerPosition.getColumn() + 1);
    }

    public char getCurrentCharacter()
    {
        return currentCharacter;
    }

    public Position getCurrentPosition()
    {
            return new Position(readerPosition);
    }
}
