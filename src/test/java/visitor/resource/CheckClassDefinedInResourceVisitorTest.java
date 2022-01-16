package visitor.resource;

import org.junit.jupiter.api.Test;
import parser.structures.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckClassDefinedInResourceVisitorTest {
    //visitor.resource.CheckClassDefinedInResourceVisitorTest$SampleClass
    private class SampleClass
    {
        String field1;
        String field2;
        Image image;
    }

    @Test
    public void test()
    {
        TagSentence tagSentence = new TagSentence("a");
        ClassLine classLine = new ClassLine("visitor.resource.CheckClassDefinedInResourceVisitorTest$SampleClass");
        QuantitativeConstraintSentence quantitativeConstraintSentence = new AmountSentence(true);

        FieldDefinition field1Definition = new FieldDefinition("field1", null);
        FieldDefinition field2Definition = new FieldDefinition("field2", null);
        FieldDefinition imageFieldDefinition = new FieldDefinition("image", null, null, false, true);

        ArrayList<FieldDefinition> fieldDefinitionArrayList = new ArrayList<>();

        fieldDefinitionArrayList.add(field1Definition);
        fieldDefinitionArrayList.add(field2Definition);
        fieldDefinitionArrayList.add(imageFieldDefinition);

        FieldDefinitionBlock fieldDefinitionBlock = new FieldDefinitionBlock(fieldDefinitionArrayList);

        DefinitionBlock definitionBlock = new DefinitionBlock(tagSentence, null, classLine, fieldDefinitionBlock, quantitativeConstraintSentence);

        Resource resource = new Resource("resource", definitionBlock);
        HashMap<String, Resource> resourceHashMap = new HashMap<>();
        resourceHashMap.put("resource", resource);
        var checkClassDefinedInResourceVisitor = new CheckClassDefinedInResourceVisitor();

        boolean realResult = checkClassDefinedInResourceVisitor.isClassDefinitionCorrect(resource, resourceHashMap);

        assertTrue(realResult);
    }
}
