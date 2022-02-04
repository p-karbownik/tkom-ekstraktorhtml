package visitor.resource;

import parser.structures.*;

public interface Visitor {

    Object visit(Resource resource);

    Object visit(DefinitionBlock definitionBlock);

    Object visit(TagSentence tagSentence);

    Object visit(ConditionsBlock conditionsBlock);

    Object visit(ClassLine classLine);

    Object visit(FieldDefinitionBlock fieldDefinitionBlock);

    Object visit(FieldDefinition fieldDefinition);

    Object visit(QuantitativeConstraintSentence quantitativeConstraintSentence);
}
