package parser.structures;

import visitor.resource.Visitable;
import visitor.resource.Visitor;

import java.util.Objects;

public class DefinitionBlock implements Visitable {
    private TagSentence tagSentence;
    private ConditionsBlock conditionsBlock;
    private ClassLine classLine;
    private FieldDefinitionBlock fieldsDefinitionBlock;
    private QuantitativeConstraintSentence quantitativeConstraintSentence;

    public DefinitionBlock(TagSentence tagSentence, ConditionsBlock conditionsBlock, ClassLine classLine, FieldDefinitionBlock fieldsDefinitionBlock, QuantitativeConstraintSentence quantitativeConstraintSentence)
    {
        this.tagSentence = tagSentence;
        this.conditionsBlock = conditionsBlock;
        this.classLine = classLine;
        this.fieldsDefinitionBlock = fieldsDefinitionBlock;
        this.quantitativeConstraintSentence = quantitativeConstraintSentence;
    }

    public TagSentence getTagSentence()
    {
        return tagSentence;
    }

    public ClassLine getClassLine() {
        return classLine;
    }

    public FieldDefinitionBlock getFieldsDefinitionBlock()
    {
        return fieldsDefinitionBlock;
    }

    public QuantitativeConstraintSentence getQuantitativeConstraintSentence()
    {
        return quantitativeConstraintSentence;
    }

    public ConditionsBlock getConditionsBlock() {
        return conditionsBlock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefinitionBlock that = (DefinitionBlock) o;
        return Objects.equals(tagSentence, that.tagSentence) && Objects.equals(conditionsBlock, that.conditionsBlock) && Objects.equals(classLine, that.classLine) && Objects.equals(fieldsDefinitionBlock, that.fieldsDefinitionBlock) && Objects.equals(quantitativeConstraintSentence, that.quantitativeConstraintSentence);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
