package visitor.html;

import factory.ObjectFactory;
import parser.structures.*;
import parserhtml.structures.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class ContentExtractorVisitor implements Visitor {

    private Tag root;
    private PathToResource pathToResource;
    private FieldDefinition fieldDefinition;
    private HashMap<String, Resource> resourceHashMap;

    public Object getContent(Tag root, FieldDefinition fieldDefinition, HashMap<String, Resource> resourceHashMap) throws Exception {
        this.root = root;
        this.pathToResource = fieldDefinition.getPathToResource();
        this.fieldDefinition = fieldDefinition;
        this.resourceHashMap = resourceHashMap;
        return root.accept(this);
    }

    @Override
    public Object visit(Root root) {
        return null;
    }

    @Override
    public Object visit(Tag tag) throws Exception {
        //przesuniecie sie do wymaganego node
        moveToTag();

        if(fieldDefinition.getAttributeOrResourceIdentifier() == null) // to jest glupota
            return extractText();

        if(fieldDefinition.isResource())
            return extractResource(fieldDefinition);
        for(Attribute attribute : root.getAttributes())
        {
            Object result = attribute.accept(this);

            if(result != null)
                return result;
        }

        return null;
    }

    @Override
    public Object visit(Attribute attribute) throws IOException {
        if(attribute.getName().compareTo(fieldDefinition.getAttributeOrResourceIdentifier()) != 0)
            return null;

        if(fieldDefinition.isExtractAsImage())
        {
            URL url = new URL(attribute.getValue());
            BufferedImage image = ImageIO.read(url);
            return image;
        }
        else
            return attribute.getValue();

    }

    @Override
    public Object visit(Text text) {
        return text.getContent();
    }

    private void moveToTag() {
        if (pathToResource.getPathRoot().getPathElement() instanceof SelfPathElement)
            return;

        PathToResource.PathNode currentPathNode = pathToResource.getPathRoot();
        Tag currentTag = root;

        while (currentPathNode != null)
        {
            TagPathElement tagPathElement = (TagPathElement) currentPathNode.getPathElement();
            currentTag = getDesiredTag(currentTag, tagPathElement.getIdentifier(), tagPathElement.getNumber());

            currentPathNode = currentPathNode.getNext();
        }

        root = currentTag;
    }

    private Tag getDesiredTag(Tag tag, String tagName, int tagNumber) {
        int i = 0;

        for (Element e : tag.getChildren()) {
            if (!(e instanceof Tag))
                continue;

            if (((Tag) e).getName().compareTo(tagName) == 0) {
                if (i == tagNumber)
                    return (Tag) e;
                else
                    i += 1;
            }
        }

        return null;
    }

    private String extractText()
    {
        StringBuilder stringBuilder = new StringBuilder();

        for(Element e : root.getChildren())
        {
            if(e instanceof Text)
            {
                stringBuilder.append((String) ((Text) e).accept(this));
                stringBuilder.append('\n');
            }
        }

        return stringBuilder.toString();
    }

    private Object extractResource(FieldDefinition fieldDefinition) throws Exception {
        return ObjectFactory.createObject(resourceHashMap.get(fieldDefinition.getAttributeOrResourceIdentifier()), root, resourceHashMap);
    }
}
