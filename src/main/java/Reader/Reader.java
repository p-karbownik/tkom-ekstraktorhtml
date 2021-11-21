package Reader;

import java.io.BufferedReader;
import java.io.IOException;

public class Reader
{
    private final static int startRow = 1;
    private final static int startColumn = 0;
    private final static int tabLength = 8;

    private Position readerPosition;
    private final BufferedReader inputReader;
    private char currentCharacter;

    public Reader(BufferedReader inputReader)
    {
        this.inputReader = inputReader;
        readerPosition = new Position(startRow, startColumn);
    }

    public void readCharacter() throws IOException {
        if(currentCharacter == '\n')
        {
            readerPosition.setRow(readerPosition.getRow() + 1);
            readerPosition.setColumn(startColumn);
        }
        else if(currentCharacter == '\t')
        {
            readerPosition.setColumn(readerPosition.getColumn() + tabLength);
        }
        else if( currentCharacter == '\r')
        {
            readerPosition.setColumn(startColumn);
        }

        currentCharacter = (char) inputReader.read();
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
