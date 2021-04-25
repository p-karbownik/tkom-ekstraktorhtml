package Lexer;

public class Position implements Cloneable{
    int row;
    int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public void setNextColumn() {
        column++;
    }

    public void setNextRow() {
        row++;
    }

    public void setColumn(int column) {
        this.column = column;
    }


    public String toString()
    {
        return "Position: row = " + row + ", column = " + column;
    }

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
