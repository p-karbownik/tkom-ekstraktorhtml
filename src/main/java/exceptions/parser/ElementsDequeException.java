package exceptions.parser;

public class ElementsDequeException extends Exception{
    private int dequeueSize;

    public ElementsDequeException(int dequeueSize)
    {
        this.dequeueSize = dequeueSize;
    }

    public int getDequeueSize() {
        return dequeueSize;
    }
}
