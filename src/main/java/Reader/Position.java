package Reader;

import java.util.Objects;

public class Position{
    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Position(Position p)
    {
        row = p.row;
        column = p.column;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String toString()
    {
        return "Position: row = " + row + ", column = " + column;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
