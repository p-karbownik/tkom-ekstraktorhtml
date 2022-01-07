package parser.structures;

import java.util.ArrayList;
import java.util.Objects;

public class PathToResource
{

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

    private PathNode pathRoot;

    public PathToResource(ArrayList<PathElement> elements)
    {
        pathRoot = createPath(elements);
    }

    private PathNode createPath(ArrayList<PathElement> elements)
    {
        pathRoot = new PathNode();
        PathNode current = pathRoot;
        PathNode previous = null;

        for(PathElement e : elements)
        {
            current.pathElement = e;
            current.next = previous;

            previous = current;
            current = new PathNode();
        }

        return pathRoot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathToResource that = (PathToResource) o;
        PathNode thisPathNode = pathRoot;
        PathNode thatPathNode = that.pathRoot;

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
