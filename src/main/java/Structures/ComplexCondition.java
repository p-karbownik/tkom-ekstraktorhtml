package Structures;

import Lexer.TokenType;

import java.util.ArrayList;

public class ComplexCondition extends Condition
{
    private ArrayList<SimpleCondition> conditions;
    private ArrayList<TokenType> operators;

    public ComplexCondition()
    {
        conditions = new ArrayList<>();
        operators = new ArrayList<>();
    }

    public void addCondition(SimpleCondition condition)
    {
        conditions.add(condition);
    }

    public void addOperators(TokenType operator)
    {
        operators.add(operator);
    }
}
