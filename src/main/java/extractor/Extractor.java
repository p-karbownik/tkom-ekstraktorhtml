package extractor;

import factory.ObjectFactory;
import parser.structures.AmountSentence;
import parser.structures.QuantitativeConstraintSentence;
import parser.structures.RangeSentence;
import parser.structures.Resource;
import parserhtml.structures.Element;
import parserhtml.structures.Root;
import parserhtml.structures.Tag;
import visitor.html.TagExtractorVisitor;
import visitor.resource.CheckClassDefinedInResourceVisitor;
import visitor.resource.ClassParametersExtractorVisitor;

import java.util.ArrayList;
import java.util.HashMap;

public class Extractor {

    private HashMap<String, Resource> resources;
    private Root htmlDocumentRoot;
    private ArrayList<Object> extractedObjects;
    private boolean extractionResult;

    public Extractor(HashMap<String, Resource> resources, Root htmlDocumentRoot)
    {
        this.resources = resources;
        this.htmlDocumentRoot = htmlDocumentRoot;
        extractionResult = false;
    }

    public void setResources(HashMap<String, Resource> resources)
    {
        this.resources = resources;
    }

    public void setHtmlDocumentRoot(Root htmlDocumentRoot)
    {
        this.htmlDocumentRoot = htmlDocumentRoot;
    }

    public ArrayList<Object> getExtractedObjects()
    {
        return extractedObjects;
    }

    public void extract(String resourceName) throws Exception {
        CheckClassDefinedInResourceVisitor checkClassDefinedInResourceVisitor = new CheckClassDefinedInResourceVisitor();

        if(checkClassDefinedInResourceVisitor.isClassDefinitionCorrect(resources.get(resourceName), resources))
        {
            ClassParametersExtractorVisitor classParametersExtractorVisitor = new ClassParametersExtractorVisitor();
            classParametersExtractorVisitor.extract(resources.get(resourceName));

            if(classParametersExtractorVisitor.getQuantitativeConstraintSentence() instanceof AmountSentence)
            {
                AmountSentence amountSentence = (AmountSentence) classParametersExtractorVisitor.getQuantitativeConstraintSentence();

                if(amountSentence.getAmount() == 0 && !amountSentence.isAmountEqualEvery())
                    return;
            }

            TagExtractorVisitor tagExtractorVisitor = new TagExtractorVisitor();
            tagExtractorVisitor.extractElements(htmlDocumentRoot, classParametersExtractorVisitor.getTagName(), classParametersExtractorVisitor.getFieldDefinitions(), classParametersExtractorVisitor.getConditionSentences(), resources);

            ArrayList<Element> elements = tagExtractorVisitor.getExtractedElements();
            ArrayList<Object> extractedObjects = new ArrayList<>();

            for(Element element : elements)
            {
                Object obj = ObjectFactory.createObject(resources.get(resourceName), element, resources);

                if(obj != null)
                    extractedObjects.add(obj);
            }

            if(classParametersExtractorVisitor.getQuantitativeConstraintSentence() instanceof RangeSentence)
            {
                RangeSentence rangeSentence = (RangeSentence) classParametersExtractorVisitor.getQuantitativeConstraintSentence();

                this.extractedObjects = new ArrayList<>(extractedObjects.subList(rangeSentence.getFrom(), rangeSentence.getTo()));
            }
            else
            {
                AmountSentence amountSentence = (AmountSentence) classParametersExtractorVisitor.getQuantitativeConstraintSentence();

                if(amountSentence.isAmountEqualEvery())
                    this.extractedObjects = extractedObjects;
                else
                    this.extractedObjects = new ArrayList<>(extractedObjects.subList(0, amountSentence.getAmount() - 1));
            }

        }
        else
            extractionResult = false;
    }
}
