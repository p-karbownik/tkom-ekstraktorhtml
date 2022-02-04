package parser.structures;

import visitor.resource.Visitable;
import visitor.resource.Visitor;

import java.util.Objects;

public class Resource implements Visitable
{
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

    public DefinitionBlock getDefinitionBlock()
    {
        return definitionBlock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(name, resource.name) && Objects.equals(definitionBlock, resource.definitionBlock);
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
