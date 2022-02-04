package visitor.resource;

import parser.structures.*;

import java.util.ArrayList;

public class ClassParametersExtractorVisitor implements Visitor{
    private String classFullName;
    private String tagName;
    private ArrayList<ConditionSentence> conditionSentences;
    private ArrayList<FieldDefinition> fieldDefinitions;
    private QuantitativeConstraintSentence quantitativeConstraintSentence;

    public ClassParametersExtractorVisitor()
    {

    }

    public void extract(Resource resource)
    {
        resource.accept(this);
    }

    public String getClassFullName()
    {
        return classFullName;
    }

    public ArrayList<FieldDefinition> getFieldDefinitions()
    {
        return fieldDefinitions;
    }

    public String getTagName()
    {
        return tagName;
    }

    public QuantitativeConstraintSentence getQuantitativeConstraintSentence()
    {
        return quantitativeConstraintSentence;
    }

    public ArrayList<ConditionSentence> getConditionSentences() {
        return conditionSentences;
    }

    @Override
    public Object visit(Resource resource) {
        resource.getDefinitionBlock().accept(this);
        return null;
    }

    @Override
    public Object visit(DefinitionBlock definitionBlock) {
        definitionBlock.getTagSentence().accept(this);

        if(definitionBlock.getConditionsBlock() != null)
            definitionBlock.getConditionsBlock().accept(this);

        definitionBlock.getClassLine().accept(this);
        definitionBlock.getFieldsDefinitionBlock().accept(this);
        definitionBlock.getQuantitativeConstraintSentence().accept(this);
        return null;
    }

    @Override
    public Object visit(TagSentence tagSentence) {
        tagName = tagSentence.getTagName();
        return null;
    }

    @Override
    public Object visit(ConditionsBlock conditionsBlock) {
        this.conditionSentences = conditionsBlock.getConditionSentences();
        return null;
    }

    @Override
    public Object visit(ClassLine classLine) {
        classFullName = classLine.getClassIdentifier();
        return null;
    }

    @Override
    public Object visit(FieldDefinitionBlock fieldDefinitionBlock) {
        fieldDefinitions = fieldDefinitionBlock.getFieldDefinitionArrayList();
        return null;
    }

    @Override
    public Object visit(FieldDefinition fieldDefinition) {
        return null;
    }

    @Override
    public Object visit(QuantitativeConstraintSentence quantitativeConstraintSentence) {
        this.quantitativeConstraintSentence = quantitativeConstraintSentence;
        return null;
    }
}
