package Structures;

import java.util.ArrayList;
import java.util.List;

public class PathToResource
{
    //this
    //tag[Ident]<Number>
    private final int thisTagNumber = -1;

    private class PathNode
    {
        String tagName;
        int tagNumber;

        PathNode(String tagName, int tagNumber)
        {
            this.tagName = tagName;
            this.tagNumber = tagNumber;
        }
    }

    private List<PathNode> path;

    public PathToResource()
    {
        path = new ArrayList<>();
    }


    public void addNodeToPath(String tagName, int tagNumber)
    {
        if(tagName.compareTo("this") == 0)
            path.add(new PathNode(tagName, thisTagNumber));
        else
            path.add(new PathNode(tagName, tagNumber));

    }
}
