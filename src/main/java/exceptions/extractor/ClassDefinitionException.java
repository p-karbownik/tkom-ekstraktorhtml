package exceptions.extractor;

public class ClassDefinitionException extends Exception{
    private String className;

    public ClassDefinitionException(String className)
    {
        super("The definition of class " + className + " is incorrect");
        this.className = className;
    }

    public String getClassName()
    {
        return className;
    }
}
