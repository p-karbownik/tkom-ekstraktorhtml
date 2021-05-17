package Structures;

import Lexer.TokenType;

import java.util.ArrayList;

public class PathInCondition
{
    ArrayList<PathNode> path;

    private class PathNode
    {
        TokenType nodeType;

        PathNode(TokenType nodeType)
        {
            this.nodeType = nodeType;
        }
    }

    public PathInCondition()
    {
        path = new ArrayList<>();
    }

    public void addNode(TokenType node)
    {
        path.add(new PathNode(node));
    }
}
