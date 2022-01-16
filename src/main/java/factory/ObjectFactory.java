package factory;

import parser.structures.FieldDefinition;
import parser.structures.Resource;
import parserhtml.structures.Element;
import parserhtml.structures.Tag;
import visitor.html.ContentExtractorVisitor;
import visitor.resource.ClassParametersExtractorVisitor;

import java.lang.reflect.Field;
import java.util.HashMap;

public class ObjectFactory {

   public static Object createObject(Resource resource, Element node, HashMap<String, Resource> resourceHashMap) throws Exception {
       var classParametersExtractorVisitor = new ClassParametersExtractorVisitor();

       classParametersExtractorVisitor.extract(resource);

       Class<?> clazz = Class.forName(classParametersExtractorVisitor.getClassFullName());

       Object object = clazz.getDeclaredConstructor().newInstance();

       for(FieldDefinition fieldDefinition : classParametersExtractorVisitor.getFieldDefinitions())
       {
           Field field = clazz.getDeclaredField(fieldDefinition.getFieldIdentifier());

           field.setAccessible(true);

           ContentExtractorVisitor contentExtractorVisitor = new ContentExtractorVisitor();

           Object obj = contentExtractorVisitor.getContent((Tag) node, fieldDefinition, resourceHashMap);

           field.set(object, obj);

           field.setAccessible(false);
       }

       return object;
   }

}
