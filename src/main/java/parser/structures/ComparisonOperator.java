package parser.structures;

import lexer.TokenType;

public class ComparisonOperator {
    public enum Type {equal, notEqual};

    private Type type;

    public ComparisonOperator(TokenType tokenType)
    {
        if(tokenType == TokenType.EQUAL)
            type = Type.equal;
        else
            type = Type.notEqual;
    }

    public boolean isEqualType()
    {
        return type == Type.equal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComparisonOperator that = (ComparisonOperator) o;
        return type == that.type;
    }

}
