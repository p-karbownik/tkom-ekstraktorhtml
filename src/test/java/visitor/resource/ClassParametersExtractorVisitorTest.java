package visitor.resource;

import org.junit.jupiter.api.Test;
import parser.structures.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassParametersExtractorVisitorTest {

    @Test
    public void extractTest()
    {
        TagSentence resTagSentence = new TagSentence("a");
        ClassLine classLine = new ClassLine("Resource");
        AmountSentence amountSentence = new AmountSentence(true);

        // fields
        ArrayList<PathElement> elements = new ArrayList<>();
        elements.add(new TagPathElement("img", 1));
        elements.add(new TagPathElement("div", 1));

        PathToResource pathToResource1 = new PathToResource(elements);
        FieldDefinition imgField = new FieldDefinition("img", pathToResource1, "src", false, true);

        elements = new ArrayList<>();
        elements.add(new TagPathElement("div", 1));
        elements.add(new TagPathElement("div", 1));
        elements.add(new TagPathElement("div", 2));

        PathToResource pathToResource2 = new PathToResource(elements);
        FieldDefinition authorField = new FieldDefinition("author", pathToResource2);

        elements = new ArrayList<>();
        elements.add(new TagPathElement("div", 2));
        elements.add(new TagPathElement("div", 1));
        elements.add(new TagPathElement("div", 2));
        PathToResource pathToResource3 = new PathToResource(elements);
        FieldDefinition descriptionField = new FieldDefinition("description", pathToResource3);

        ArrayList<FieldDefinition> fieldDefinitions = new ArrayList<>();
        fieldDefinitions.add(imgField);
        fieldDefinitions.add(authorField);
        fieldDefinitions.add(descriptionField);
        FieldDefinitionBlock resFieldDefinitionBlock = new FieldDefinitionBlock(fieldDefinitions);

        DefinitionBlock definitionBlock = new DefinitionBlock(resTagSentence, null, classLine, resFieldDefinitionBlock, amountSentence);
        Resource expectedResResource = new Resource("res", definitionBlock);



        ClassParametersExtractorVisitor classParametersExtractorVisitor = new ClassParametersExtractorVisitor();
        classParametersExtractorVisitor.extract(expectedResResource);

        assertEquals(resTagSentence.getTagName(), classParametersExtractorVisitor.getTagName());
        assertEquals(classLine.getClassIdentifier(), classParametersExtractorVisitor.getClassFullName());
        assertEquals(fieldDefinitions, classParametersExtractorVisitor.getFieldDefinitions());
        assertEquals(amountSentence, classParametersExtractorVisitor.getQuantitativeConstraintSentence());
    }
}
