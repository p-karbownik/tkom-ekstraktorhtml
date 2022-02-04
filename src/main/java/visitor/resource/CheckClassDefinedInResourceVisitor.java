package visitor.resource;

import parser.structures.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

public class CheckClassDefinedInResourceVisitor implements Visitor {

    private String classIdentifier;
    private HashMap<String, Resource> resourceHashMap;

    public boolean isClassDefinitionCorrect(Resource resource, HashMap<String, Resource> resourceHashMap) {
        this.resourceHashMap = resourceHashMap;
        return (boolean) resource.accept(this);
    }

    @Override
    public Object visit(Resource resource) {
        return resource.getDefinitionBlock().accept(this);
    }

    @Override
    public Object visit(DefinitionBlock definitionBlock) {
        boolean classNameExistence = (boolean) definitionBlock.getClassLine().accept(this);

        if (classNameExistence)
            return definitionBlock.getFieldsDefinitionBlock().accept(this);
        else
            return false;
    }

    @Override
    public Object visit(TagSentence tagSentence) {
        return null;
    }

    @Override
    public Object visit(ConditionsBlock conditionsBlock) {
        return null;
    }

    @Override
    public Object visit(ClassLine classLine) {
        classIdentifier = classLine.getClassIdentifier();

        try {
            Class.forName(classIdentifier, false, getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            return false;
        }

        return true;
    }

    @Override
    public Object visit(FieldDefinitionBlock fieldDefinitionBlock) {
        var fieldDefinitions = fieldDefinitionBlock.getFieldDefinitionArrayList();

        for (FieldDefinition fieldDefinition : fieldDefinitions) {
            if (!((boolean) fieldDefinition.accept(this)))
                return false;
        }

        return true;
    }

    @Override
    public Object visit(FieldDefinition fieldDefinition) {
        Class<?> clazz;
        try {
            clazz = Class.forName(classIdentifier);
        } catch (Exception e) {
            return false;
        }

        if (doesObjectContainField(clazz, fieldDefinition.getFieldIdentifier())) {
            try {
                Field field = clazz.getDeclaredField(fieldDefinition.getFieldIdentifier());

                if (fieldDefinition.isResource()) {
                    try {
                        Resource resource = resourceHashMap.get(fieldDefinition.getAttributeOrResourceIdentifier());

                        ClassParametersExtractorVisitor extractorVisitor = new ClassParametersExtractorVisitor();
                        extractorVisitor.extract(resource);


                        return field.getType().isAssignableFrom(Class.forName(extractorVisitor.getClassFullName()));
                    }
                    catch (Exception e) {
                        return false;
                    }
                }

                return field.getType().isAssignableFrom((fieldDefinition.isExtractAsImage() ? java.awt.image.BufferedImage.class : String.class));

            } catch (NoSuchFieldException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public Object visit(QuantitativeConstraintSentence quantitativeConstraintSentence) {
        return null;
    }

    private boolean doesObjectContainField(Class<?> clazz, String fieldIdentifier) {
        return Arrays.stream(clazz.getDeclaredFields()).anyMatch(f -> f.getName().equals(fieldIdentifier));
    }

}
