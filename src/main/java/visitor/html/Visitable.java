package visitor.html;

import java.net.MalformedURLException;

public interface Visitable {
    Object accept(Visitor visitor) throws MalformedURLException, Exception;
}
