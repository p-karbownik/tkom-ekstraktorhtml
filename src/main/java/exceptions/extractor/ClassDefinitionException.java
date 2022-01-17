package exceptions.extractor;

public class ClassDefinitionException extends Exception{
    private String className;

    public ClassDefinitionException(String className)
    {
        this.className = className;
    }

    public String getClassName()
    {
        return className;
    }
}
