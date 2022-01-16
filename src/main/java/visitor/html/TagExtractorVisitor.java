package visitor.html;

import parser.structures.*;
import parserhtml.structures.*;
import visitor.conditionsentence.ConditionSentenceCheckVisitor;
import visitor.resource.CheckClassDefinedInResourceVisitor;
import visitor.resource.ClassParametersExtractorVisitor;

import java.util.ArrayList;
import java.util.HashMap;

public class TagExtractorVisitor implements Visitor {

    private ArrayList<FieldDefinition> fieldDefinitions;
    private ArrayList<Element> extractedElements;
    private ArrayList<ConditionSentence> conditionSentences;
    private String tagName;
    private HashMap<String, Resource> resourceHashMap;

    public void extractElements(Root root, String tagName, ArrayList<FieldDefinition> fieldDefinitions, ArrayList<ConditionSentence> conditionSentences, HashMap<String, Resource> resourceHashMap) throws Exception {
        this.tagName = tagName;
        extractedElements = new ArrayList<>();
        this.fieldDefinitions = fieldDefinitions;
        this.conditionSentences = conditionSentences;
        this.resourceHashMap = resourceHashMap;
        visit(root);
    }

    public ArrayList<Element> getExtractedElements() {
        return extractedElements;
    }

    public void setFieldDefinitions(ArrayList<FieldDefinition> fieldDefinitions) {
        this.fieldDefinitions = fieldDefinitions;
    }

    @Override
    public Object visit(Root root) throws Exception {
        for (Element e : root.getChildren()) {
            if (e instanceof Tag)
                ((Tag) e).accept(this);
            if (e instanceof Text)
                ((Text) e).accept(this);
        }

        return null;
    }

    @Override
    public Object visit(Tag tag) throws Exception {
        if (tag.getName().compareTo(tagName) == 0) {
            boolean checkPathRequirementsResult = true;

            for (FieldDefinition fieldDefinition : fieldDefinitions)
                if (!checkPathRequirements(tag, fieldDefinition.getPathToResource(), fieldDefinition.getAttributeOrResourceIdentifier(), fieldDefinition.isResource())) {
                    checkPathRequirementsResult = false;
                    break;
                }

            if (checkPathRequirementsResult) {
                if (conditionSentences == null || conditionSentences.isEmpty())
                    extractedElements.add(tag);

                else {
                    boolean checkConditionSentencesResult = true;

                    for (ConditionSentence conditionSentence : conditionSentences) {
                        ConditionSentenceCheckVisitor conditionSentenceCheckVisitor = new ConditionSentenceCheckVisitor();

                        if (!conditionSentenceCheckVisitor.checkCondition(conditionSentence, tag)) {
                            checkConditionSentencesResult = false;
                            break;
                        }
                    }

                    if (checkConditionSentencesResult)
                        extractedElements.add(tag);
                }
            }
        }

        for (Element e : tag.getChildren()) {
            if (e instanceof Tag)
                ((Tag) e).accept(this);
        }

        return null;
    }

    @Override
    public Object visit(Attribute attribute) {
        return null;
    }

    @Override
    public Object visit(Text text) {
        return null;
    }

    private boolean checkPathRequirements(Tag tag, PathToResource pathToResource, String resourceOrAttributeIdentifier, boolean isResourceIdentifier) {
        PathToResource.PathNode currentPathNode = pathToResource.getPathRoot();
        Tag currentTag = tag;

        if (!(currentPathNode.getPathElement() instanceof SelfPathElement)) {
            //przesuwanie sie po tagu
            while (currentTag != null && currentPathNode != null) {
                currentTag = getDesiredTag(currentTag, ((TagPathElement) currentPathNode.getPathElement()).getIdentifier(), ((TagPathElement) currentPathNode.getPathElement()).getNumber());
                currentPathNode = currentPathNode.getNext();
            }
        }

        if (currentTag == null)
            return false;

        if (resourceOrAttributeIdentifier != null) {

            if (!isResourceIdentifier) {
                //przypadek, gdy wydobywany jest atrybut
                for (Attribute attribute : currentTag.getAttributes()) {
                    if (attribute.getName().compareTo(resourceOrAttributeIdentifier) == 0)
                        return true;
                }

                return false;
            }

            else {
                //klasa domyslnie jest, brak definicji danej klasy zostal obsluzony na poziomie parsera

                Resource resource = resourceHashMap.get(resourceOrAttributeIdentifier);

                //sprawdzenie obecnosci klasy - done
                CheckClassDefinedInResourceVisitor checkClassDefinedInResourceVisitor = new CheckClassDefinedInResourceVisitor();

                if(!checkClassDefinedInResourceVisitor.isClassDefinitionCorrect(resource, resourceHashMap))
                    return false;

                ClassParametersExtractorVisitor classParametersExtractorVisitor = new ClassParametersExtractorVisitor();
                classParametersExtractorVisitor.extract(resource);

                //sprawdzenie czy nazwa tagu sie pokrywa

                //przesuniecie
                if(currentTag.getName().compareTo(classParametersExtractorVisitor.getTagName()) != 0)
                    return false;

                //sprawdzenie czy sciezki sie zgadzaja

                boolean checkPathRequirementsResult = true;

                for (FieldDefinition fieldDefinition : classParametersExtractorVisitor.getFieldDefinitions())
                    if (!checkPathRequirements(currentTag, fieldDefinition.getPathToResource(), fieldDefinition.getAttributeOrResourceIdentifier(), fieldDefinition.isResource())) {
                        checkPathRequirementsResult = false;
                        break;
                    }

                if(!checkPathRequirementsResult)
                    return false;


                //czy conditions zachodza
                if(classParametersExtractorVisitor.getConditionSentences() != null &&
                !classParametersExtractorVisitor.getConditionSentences().isEmpty())
                {
                    for (ConditionSentence conditionSentence : classParametersExtractorVisitor.getConditionSentences()) {
                        ConditionSentenceCheckVisitor conditionSentenceCheckVisitor = new ConditionSentenceCheckVisitor();

                        if (!conditionSentenceCheckVisitor.checkCondition(conditionSentence, currentTag))
                            return false;
                    }
                }

                //jak tam sie nie wywalilo to znaczy, ze jest ok
            }
        }

        // sprawdzenie czy tag spelnia wymagania
        return true;
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
}
