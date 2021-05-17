package Structures;

import java.util.ArrayList;

public class Resource {
    private TagSentence tagSentence;
    private ArrayList<FieldDefinition> fieldDefinitions;

    public Resource(TagSentence tagSentence, ArrayList<FieldDefinition> fieldDefinitions)
    {
        this.tagSentence = tagSentence;
        this.fieldDefinitions = fieldDefinitions;
    }
}
