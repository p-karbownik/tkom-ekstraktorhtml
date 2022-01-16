package parser.structures;

import visitor.resource.Visitable;
import visitor.resource.Visitor;

import java.util.ArrayList;
import java.util.Objects;

public class FieldDefinitionBlock implements Visitable {

    ArrayList<FieldDefinition> fieldDefinitionArrayList;

    public FieldDefinitionBlock(ArrayList<FieldDefinition> fieldDefinitionArrayList)
    {
        this.fieldDefinitionArrayList = fieldDefinitionArrayList;
    }

    public ArrayList<FieldDefinition> getFieldDefinitionArrayList() {
        return fieldDefinitionArrayList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDefinitionBlock that = (FieldDefinitionBlock) o;
        return Objects.equals(fieldDefinitionArrayList, that.fieldDefinitionArrayList);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
