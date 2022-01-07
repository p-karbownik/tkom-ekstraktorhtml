package parser.structures;

import java.util.Objects;

public class DefinitionBlock {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefinitionBlock that = (DefinitionBlock) o;
        return Objects.equals(tagSentence, that.tagSentence) && Objects.equals(conditionsBlock, that.conditionsBlock) && Objects.equals(classLine, that.classLine) && Objects.equals(fieldsDefinitionBlock, that.fieldsDefinitionBlock) && Objects.equals(quantitativeConstraintSentence, that.quantitativeConstraintSentence);
    }

}
