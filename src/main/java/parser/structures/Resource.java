package parser.structures;

import java.util.Objects;

public class Resource {
    private String name;
    private DefinitionBlock definitionBlock;

    public Resource(String name, DefinitionBlock definitionBlock)
    {
        this.name = name;
        this.definitionBlock = definitionBlock;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(name, resource.name) && Objects.equals(definitionBlock, resource.definitionBlock);
    }
}
