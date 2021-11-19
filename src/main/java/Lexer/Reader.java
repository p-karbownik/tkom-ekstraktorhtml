package Lexer;

import java.io.BufferedReader;
import java.io.IOException;

public class Reader
{
    private final static int startRow = 1;
    private final static int startColumn = 0;
    private final static int tabLength = 8;

    private Position inputReaderPosition;
    private final BufferedReader inputReader;
    private int currentCharacter;
    private boolean isReadingBlocked = false;

    public Reader(BufferedReader inputReader)
    {
        this.inputReader = inputReader;
        inputReaderPosition = new Position(startRow, startColumn);
    }

    public void readCharacter() throws IOException {
        if(currentCharacter == '\n')
        {
            inputReaderPosition.setNextRow();
            inputReaderPosition.setColumn(startColumn);
        }
        else if(currentCharacter == '\t')
        {
            inputReaderPosition.increaseColumn(tabLength);
        }
        else if( currentCharacter == '\r')
        {
            inputReaderPosition.setColumn(startColumn);
        }
        else
            inputReaderPosition.setNextColumn();

        if(!isReadingBlocked)
            currentCharacter = inputReader.read();
        else
            isReadingBlocked = false;
    }

    public char getCurrentCharacter()
    {
        return (char) currentCharacter;
    }

    public Position getCurrentPosition()
    {
        return inputReaderPosition;
    }

    public void blockNextReading()
    {
        isReadingBlocked = true;
    }
}
