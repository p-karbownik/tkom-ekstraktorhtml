package visitor.html;

import parserhtml.structures.Attribute;
import parserhtml.structures.Root;
import parserhtml.structures.Tag;
import parserhtml.structures.Text;

import java.net.MalformedURLException;

public interface Visitor {

    Object visit(Root root) throws Exception;

    Object visit(Tag tag) throws Exception;

    Object visit(Attribute attribute) throws Exception;

    Object visit(Text text);
}
