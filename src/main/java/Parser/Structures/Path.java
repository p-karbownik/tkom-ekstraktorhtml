package Parser.Structures;

import Lexer.TokenType;

import java.util.ArrayList;
import java.util.Objects;

public class Path {

    private class PathNode
    {
        PathElement pathElement;
        PathNode next;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PathNode pathNode = (PathNode) o;
            return Objects.equals(pathElement, pathNode.pathElement) && Objects.equals(next, pathNode.next);
        }

    }

    private PathNode root;

    public Path(TokenType tokenType) {
        //self
        //ancestor
        //descendant

        if(tokenType == TokenType.SELF)
        {
            root = new PathNode();
            root.next = null;
            root.pathElement = new SelfPathElement();
        }
        else if(tokenType == TokenType.ANCESTOR)
        {
            root = new PathNode();
            root.next = null;
            root.pathElement = new AncestorPathElement();
        }
        else
        {
            root = new PathNode();
            root.next = null;
            root.pathElement = new DescendantPathElement();
        }
    }

    public Path(ArrayList<PathElement> elements)
    {
        createPath(elements);
    }

    private void createPath(ArrayList<PathElement> elements)
    {
        root = new PathNode();
        PathNode current = root;
        PathNode previous = null;

        for(PathElement e : elements)
        {
            current.pathElement = e;
            current.next = previous;

            previous = current;
            current = new PathNode();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Path that = (Path) o;
        Path.PathNode thisPathNode = root;
        Path.PathNode thatPathNode = that.root;

        while (thisPathNode != null)
        {
            if(!thatPathNode.equals(thisPathNode))
                return false;

            thisPathNode = thisPathNode.next;
            thatPathNode = thatPathNode.next;
        }

        return true;
    }

}
