package parser.structures;

import java.util.ArrayList;
import java.util.Objects;

public class FieldDefinitionBlock {

    ArrayList<FieldDefinition> fieldDefinitionArrayList;

    public FieldDefinitionBlock(ArrayList<FieldDefinition> fieldDefinitionArrayList)
    {
        this.fieldDefinitionArrayList = fieldDefinitionArrayList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDefinitionBlock that = (FieldDefinitionBlock) o;
        return Objects.equals(fieldDefinitionArrayList, that.fieldDefinitionArrayList);
    }
}
