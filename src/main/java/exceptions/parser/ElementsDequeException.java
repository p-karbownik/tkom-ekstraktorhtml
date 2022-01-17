package exceptions.parser;

public class ElementsDequeException extends Exception{
    private int dequeueSize;

    public ElementsDequeException(int dequeueSize)
    {
        super("The elements deque is too large");
        this.dequeueSize = dequeueSize;
    }

    public int getDequeueSize() {
        return dequeueSize;
    }
}
