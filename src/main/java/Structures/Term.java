package Structures;

import Lexer.TokenType;

import java.util.List;

public class Term
{
    private TokenType termSubject;
    private TokenType comparisonOperator;
    boolean isNegated;
    List<String> valuesSet;

    public Term()
    {
        termSubject = null;
        comparisonOperator = null;
        isNegated = false;
        valuesSet = null;
    }

    public Term(TokenType termSubject, boolean isNegated, TokenType comparisonOperator, List<String> valuesSet)
    {
        this.termSubject = termSubject;
        this.isNegated = isNegated;
        this.comparisonOperator = comparisonOperator;
        this.valuesSet = valuesSet;
    }

}
