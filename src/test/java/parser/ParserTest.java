package parser;

import lexer.Lexer;
import parser.structures.*;
import reader.Reader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import lexer.TokenType;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {

    private Lexer getLexer(String s) throws IOException {
        BufferedReader br = new BufferedReader(new StringReader(s));
        return new Lexer(new Reader(br));
    }

    @Test
    @DisplayName("Test parsowania set fields")
    public void testParseSetFields() throws Exception {
        Lexer lexer = getLexer("set fields");
        Parser parser = new Parser(lexer);

        try {
            parser.parseSetFieldsForTest();
            assert true;
        } catch (Exception e)
        {
            assert false;
        }
    }

    @Test
    @DisplayName("Test parsowania ClassLine")
    public void testParseClassLine() throws Exception {
        Lexer lexer = getLexer("export to class = Resource;");
        Parser parser = new Parser(lexer);

        try {
            ClassLine classLine = parser.parseClassLineForTest();

            assertEquals(classLine, new ClassLine("Resource"));
        } catch (Exception e)
        {
            assert false;
        }
    }

    @Test
    @DisplayName("Test parsowania TagSentence")
    public void testParseTagSentence() throws Exception
    {
        Lexer lexer = getLexer("tag = img;");
        Parser parser = new Parser(lexer);

        try {
            TagSentence tagSentence = parser.parseTagSentenceForTest();

            assertEquals(new TagSentence("img"), tagSentence);
        } catch (Exception e)
        {
            assert false;
        }
    }

    @Test
    @DisplayName("Test parsowania AmountSentece z every")
    public void testParseAmountSentence1() throws Exception
    {
        Lexer lexer = getLexer("amount = every;");
        Parser parser = new Parser(lexer);

        try {
            AmountSentence amountSentence = parser.parseAmountSentenceForTest();

            assertEquals(amountSentence, new AmountSentence(true));
        } catch (Exception e)
        {
            assert false;
        }
    }

    @Test
    @DisplayName("Test parsowania AmountSentence z wartoscia liczbowa")
    public void testParseAmountSentence2() throws Exception
    {
        Lexer lexer = getLexer("amount = 15;");
        Parser parser = new Parser(lexer);

        try {
            AmountSentence amountSentence = parser.parseAmountSentenceForTest();

            assertEquals(amountSentence, new AmountSentence(15));
        } catch (Exception e)
        {
            assert false;
        }
    }

    @Test
    @DisplayName("Test parsowania TagPathElement tylko z identyfikatorem")
    public void testParseTagPathElement1() throws Exception
    {
        Lexer lexer = getLexer("tag[id]");
        Parser parser = new Parser(lexer);

        try {
            TagPathElement tagPathElement = parser.parseTagPathElementForTest();

            assertEquals(tagPathElement, new TagPathElement("id"));
        } catch (Exception e)
        {
            assert false;
        }
    }

    @Test
    @DisplayName("Test prasowania TagPathElement z indentyfikatorem i liczba")
    public void testParseTagPathElement2() throws Exception
    {
        Lexer lexer = getLexer("tag[id][4]");
        Parser parser = new Parser(lexer);

        try {
            TagPathElement tagPathElement = parser.parseTagPathElementForTest();

            assertEquals(tagPathElement, new TagPathElement("id",4));
        } catch (Exception e)
        {
            assert false;
        }
    }

    @Test
    @DisplayName("Test parsowania RangeSentence")
    public void testParseRangeSentence() throws Exception
    {
        Lexer lexer = getLexer("range = (4,10);");
        Parser parser = new Parser(lexer);

        try {
            RangeSentence rangeSentence = parser.parseRangeSentenceForTest();

            assertEquals(rangeSentence, new RangeSentence(4, 10));
        } catch (Exception e)
        {
            assert false;
        }
    }

    @Test
    @DisplayName("Test parsowania PathToResource z self")
    public void testParsePathToResource1() throws Exception
    {
        Lexer lexer = getLexer("from(self)");

        Parser parser = new Parser(lexer);

        try {
            PathToResource pathToResource = parser.parsePathToResourceForTest();
            ArrayList<PathElement> pathElements = new ArrayList<>();
            pathElements.add(new SelfPathElement());
            assertEquals(pathToResource, new PathToResource(pathElements));
        } catch (Exception e)
        {
            assert false;
        }
    }

    @Test
    @DisplayName("Test parsowania PathToResource z jednoelementowa sciezką")
    public void testParsePathToResource2() throws Exception
    {
        Lexer lexer = getLexer("from(tag[id])");

        Parser parser = new Parser(lexer);

        try {
            PathToResource pathToResource = parser.parsePathToResourceForTest();
            ArrayList<PathElement> pathElements = new ArrayList<>();
            pathElements.add(new TagPathElement("id"));
            assertEquals(pathToResource, new PathToResource(pathElements));
        } catch (Exception e)
        {
            //e.printStackTrace();
            assert false;
        }
    }

    @Test
    @DisplayName("Test parsowania PathToResource z dwuelementowa sciezka")
    public void testParsePathToResource3() throws Exception
    {
        Lexer lexer = getLexer("from(tag[id].tag[id][4])");

        Parser parser = new Parser(lexer);

        try {
            PathToResource pathToResource = parser.parsePathToResourceForTest();
            ArrayList<PathElement> pathElements = new ArrayList<>();
            pathElements.add(new TagPathElement("id"));
            pathElements.add(new TagPathElement("id", 4));
            assertEquals(pathToResource, new PathToResource(pathElements));
        } catch (Exception e)
        {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    @DisplayName("Test parsowania najprostszego przypadku FieldDefinition")
    public void testParseFieldDefinition1() throws Exception
    {
        Lexer lexer = getLexer("field cosik = from(self);");
        Parser parser = new Parser(lexer);

        try {
            FieldDefinition fieldDefinition = parser.parseFieldDefinitionForTest();
            ArrayList<PathElement> pathElements = new ArrayList<>();
            pathElements.add(new SelfPathElement());
            assertEquals(fieldDefinition, new FieldDefinition("cosik", new PathToResource(pathElements)));
        } catch (Exception e)
        {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    @DisplayName("Test parsowania FieldDefinition przypadek z attribute")
    public void testParseFieldDefinition2() throws Exception
    {
        Lexer lexer = getLexer("field cosik = from(tag[id][4]).attribute[src];");
        Parser parser = new Parser(lexer);

        try {
            FieldDefinition fieldDefinition = parser.parseFieldDefinitionForTest();
            ArrayList<PathElement> pathElements = new ArrayList<>();
            pathElements.add(new TagPathElement("id", 4));
            assertEquals(fieldDefinition, new FieldDefinition("cosik", new PathToResource(pathElements), "src", false));
        } catch (Exception e)
        {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    @DisplayName("Test parsowania FieldDefinition przypadek z attribute i image")
    public void testParseFieldDefinition3() throws Exception
    {
        Lexer lexer = getLexer("field cosik = from(tag[id][4]).attribute[src].asImg;");
        Parser parser = new Parser(lexer);

        try {
            FieldDefinition fieldDefinition = parser.parseFieldDefinitionForTest();
            ArrayList<PathElement> pathElements = new ArrayList<>();
            pathElements.add(new TagPathElement("id", 4));
            assertEquals(fieldDefinition, new FieldDefinition("cosik", new PathToResource(pathElements), "src", false, true));
        } catch (Exception e)
        {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    @DisplayName("Test parsowania ComparisonOperator nr 1")
    public void testParseComparisonOperator1() throws Exception
    {
        Lexer lexer = getLexer("==");
        Parser parser = new Parser(lexer);

        ComparisonOperator comparisonOperator = parser.parseComparisonOperatorForTest();
        ComparisonOperator expectedComparisonOperator = new ComparisonOperator(TokenType.EQUAL);
        assertEquals(expectedComparisonOperator, comparisonOperator);
    }

    @Test
    @DisplayName("Test parsowania ComparisonOperator nr 2")
    public void testParseComparisonOperator2() throws Exception {
        Lexer lexer = getLexer("!=");
        Parser parser = new Parser(lexer);

        ComparisonOperator comparisonOperator = parser.parseComparisonOperatorForTest();
        ComparisonOperator expectedComparisonOperator = new ComparisonOperator(TokenType.NOT_EQUAL);
        assertEquals(expectedComparisonOperator, comparisonOperator);
    }

    @Test
    @DisplayName("Test parsowania Subject nr 1")
    public void testParseSubject1() throws Exception{
        Lexer lexer = getLexer("class");
        Parser parser = new Parser(lexer);

        Subject parsedSubject = parser.parseSubjectForTest();
        Subject expectedSubject = new ClassSubject();

        assertEquals(expectedSubject, parsedSubject);
    }

    @Test
    @DisplayName("Test parsowania Subject nr 2")
    public void testParseSubject2() throws Exception{
        Lexer lexer = getLexer("attribute");
        Parser parser = new Parser(lexer);

        Subject parsedSubject = parser.parseSubjectForTest();
        Subject expectedSubject = new AttributeSubject();

        assertEquals(expectedSubject, parsedSubject);
    }

    @Test
    @DisplayName("Test parsowania ValueSet nr 1")
    public void testParseValueSet1() throws Exception
    {
        Lexer lexer = getLexer("{\"cosik\"}");
        Parser parser = new Parser(lexer);

        ValueSet parsedValueSet = parser.parseValueSetForTest();
        ArrayList<String> values = new ArrayList<>();
        values.add("cosik");
        ValueSet expectedValueSet = new ValueSet(values);

        assertEquals(expectedValueSet, parsedValueSet);
    }

    @Test
    @DisplayName("Test parsowania ValueSet nr 2")
    public void testParseValueSet2() throws Exception
    {
        Lexer lexer = getLexer("{\"value1\", \"value2\" }");
        Parser parser = new Parser(lexer);

        ValueSet parsedValueSet = parser.parseValueSetForTest();
        ArrayList<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        ValueSet expectedValueSet = new ValueSet(values);

        assertEquals(expectedValueSet, parsedValueSet);
    }

    @Test
    public void testParsePath1() throws Exception
    {
        Lexer lexer = getLexer("self");
        Parser parser = new Parser(lexer);

        Path expectedPath = new Path(TokenType.SELF);
        Path parsedPath = parser.parsePathForTest();

        assertEquals(expectedPath, parsedPath);
    }

    @Test
    public void testParsePath2() throws Exception
    {
        Lexer lexer = getLexer("ancestor");
        Parser parser = new Parser(lexer);

        Path expectedPath = new Path(TokenType.ANCESTOR);
        Path parsedPath = parser.parsePathForTest();

        assertEquals(expectedPath, parsedPath);
    }

    @Test
    public void testParsePath3() throws Exception
    {
        Lexer lexer = getLexer("descendant");
        Parser parser = new Parser(lexer);

        Path expectedPath = new Path(TokenType.DESCENDANT);
        Path parsedPath = parser.parsePathForTest();

        assertEquals(expectedPath, parsedPath);
    }

    @Test
    public void testParsePath4() throws Exception
    {
        Lexer lexer = getLexer("parent.parent");
        Parser parser = new Parser(lexer);

        ArrayList<PathElement> pathElements = new ArrayList<>();
        pathElements.add(new ParentPathElement());
        pathElements.add(new ParentPathElement());

        Path expectedPath = new Path(pathElements);
        Path parsedPath = parser.parsePathForTest();

        assertEquals(expectedPath, parsedPath);
    }

    @Test
    public void testParsePath5() throws Exception
    {
        Lexer lexer = getLexer("child(self has tag a and self has attribute href).child");
        Parser parser = new Parser(lexer);

        ArrayList<PathElement> pathElements = new ArrayList<>();

        Factor factor1 = new Factor(new Path(TokenType.SELF), new FactorObject(false, new TagSubject("a")));

        AttributeSubject attributeSubjectFactor2 = new AttributeSubject();
        attributeSubjectFactor2.setIdentifier("href");
        Factor factor2 = new Factor(new Path(TokenType.SELF), new FactorObject(false, attributeSubjectFactor2));

        ArrayList<Factor> factors = new ArrayList<>();
        factors.add(factor1);
        factors.add(factor2);
        Term term = new Term(factors);
        ArrayList<Term> terms = new ArrayList<>();
        terms.add(term);
        Condition condition = new Condition(terms);
        pathElements.add(new ChildPathElement(new RelativeCondition(condition)));
        pathElements.add(new ChildPathElement());

        Path expectedPath = new Path(pathElements);
        Path parsedPath = parser.parsePathForTest();

        assertEquals(expectedPath, parsedPath);
    }

    @Test
    public void testParseConditionSentence() throws Exception
    {
        Lexer lexer = getLexer("if (self has not attribute src and parent has tag a) or self has class rtg in { \"cosik1\", \"cosik2\" };");
        Parser parser = new Parser(lexer);
        ConditionSentence parsedConditionSentence = parser.parseConditionSentenceForTest();

        //pierwszy factor

        AttributeSubject attributeSubjectFactor1 = new AttributeSubject();
        attributeSubjectFactor1.setIdentifier("src");
        Factor factor1 = new Factor(new Path(TokenType.SELF), new FactorObject(true, attributeSubjectFactor1));

        //drugi factor

        ArrayList<PathElement> pathElementsOfFactor1 = new ArrayList<>();
        pathElementsOfFactor1.add(new ParentPathElement());

        Factor factor2 = new Factor(new Path(pathElementsOfFactor1), new FactorObject(false, new TagSubject("a")));

        //trzeci factor

        ArrayList<String> valuesOfFactor3 = new ArrayList<>();
        valuesOfFactor3.add("cosik1");
        valuesOfFactor3.add("cosik2");

        ValueSet valueSetOfFactor3 = new ValueSet(valuesOfFactor3);
        ComparisonObject comparisonObject = new ComparisonObject(valueSetOfFactor3);
        ClassSubject classSubjectFactor3 = new ClassSubject();
        classSubjectFactor3.setIdentifier("rtg");

        Factor factor3 = new Factor(new Path(TokenType.SELF), new FactorObject(false, classSubjectFactor3, comparisonObject));

        //Term 1
        ArrayList<Factor> factorsOfTerm1 = new ArrayList<>();
        factorsOfTerm1.add(factor1);
        factorsOfTerm1.add(factor2);
        Term term1 = new Term(factorsOfTerm1);
        ArrayList<Term> termsOfCondition1 = new ArrayList<>();
        termsOfCondition1.add(term1);
        Condition condition1 = new Condition(termsOfCondition1);

        Factor factor4 = new Factor(condition1);
        ArrayList<Factor> factorsOfTerm2 = new ArrayList<>();
        factorsOfTerm2.add(factor4);
        Term term2 = new Term(factorsOfTerm2);

        ArrayList<Factor> factorsOfTerm3 = new ArrayList<>();
        factorsOfTerm3.add(factor3);
        Term term3 = new Term(factorsOfTerm3);

        ArrayList<Term> termsOfCondition2 = new ArrayList<>();
        termsOfCondition2.add(term2);
        termsOfCondition2.add(term3);

        ConditionSentence expectedConditionSentence = new ConditionSentence(new Condition(termsOfCondition2));

        assertEquals(expectedConditionSentence, parsedConditionSentence);
    }

    @Test
    public void testParseResource() throws Exception
    {
        String resourceCode = "resource res\n" +
                "{\n" +
                "tag = a;\n" +
                "export to class = Resource;\n" +
                "set fields\n" +
                "{\n" +
                "field img = from(tag[img][1].tag[div][1]).attribute[src].asImg;\n" +
                "field author = from(tag[div][1].tag[div][1].tag[div][2]);\n" +
                "field description = from(tag[div][2].tag[div][1].tag[div][2]);\n" +
                "}\n" +
                "amount = every;\n" +
                "}\n" +
                "\n" +
                "resource ress\n" +
                "{\n" +
                "tag = img;\n" +
                "conditions\n" +
                "{\n" +
                "if parent.parent has class media-content;\n" +
                "}\n" +
                "export to class = Link;\n" +
                "set fields\n" +
                "{\n" +
                "field link_to_image = from(self).attribute[src];\n" +
                "field description = from(self).resource[res];\n" +
                "}\n" +
                "amount = every;\n" +
                "}" ;

        Lexer lexer = getLexer(resourceCode);
        Parser parser = new Parser(lexer);

        parser.parse();
        HashMap<String, Resource> parsedResources = parser.getParsedResources();

        // resource res ...

        TagSentence resTagSentence = new TagSentence("a");
        ClassLine classLine = new ClassLine("Resource");
        AmountSentence amountSentence = new AmountSentence(true);

        // fields
        ArrayList<PathElement> elements = new ArrayList<>();
        elements.add(new TagPathElement("img", 1));
        elements.add(new TagPathElement("div", 1));

        PathToResource pathToResource1 = new PathToResource(elements);
        FieldDefinition imgField = new FieldDefinition("img", pathToResource1, "src", false, true);

        //"field author = from(tag[div][1].tag[div][1].tag[div][2]);\n" +
        //"field description = from(tag[div][2].tag[div][1].tag[div][2])"

        elements = new ArrayList<>();
        elements.add(new TagPathElement("div", 1));
        elements.add(new TagPathElement("div", 1));
        elements.add(new TagPathElement("div", 2));

        PathToResource pathToResource2 = new PathToResource(elements);
        FieldDefinition authorField = new FieldDefinition("author", pathToResource2);

        elements = new ArrayList<>();
        elements.add(new TagPathElement("div", 2));
        elements.add(new TagPathElement("div", 1));
        elements.add(new TagPathElement("div", 2));
        PathToResource pathToResource3 = new PathToResource(elements);
        FieldDefinition descriptionField = new FieldDefinition("description", pathToResource3);

        ArrayList<FieldDefinition> fieldDefinitions = new ArrayList<>();
        fieldDefinitions.add(imgField);
        fieldDefinitions.add(authorField);
        fieldDefinitions.add(descriptionField);
        FieldDefinitionBlock resFieldDefinitionBlock = new FieldDefinitionBlock(fieldDefinitions);

        DefinitionBlock definitionBlock = new DefinitionBlock(resTagSentence, null, classLine, resFieldDefinitionBlock, amountSentence);
        Resource expectedResResource = new Resource("res", definitionBlock);

        Resource parsedResResource = parsedResources.get("res");
        assertEquals(expectedResResource, parsedResResource);

        TagSentence ressTagSentence = new TagSentence("img");
        ClassLine ressClassLine = new ClassLine("Link");
        AmountSentence ressAmountSentence = new AmountSentence(true);

        elements = new ArrayList<>();
        elements.add(new SelfPathElement());

        PathToResource linkToImagePathToResource = new PathToResource(elements);
        PathToResource descriptionPathRoResource = new PathToResource(elements);

        FieldDefinition linkToImageFieldDefinition = new FieldDefinition("link_to_image", linkToImagePathToResource, "src", false);
        FieldDefinition descriptionFieldDefinition = new FieldDefinition("description", descriptionPathRoResource, "res",true);

        fieldDefinitions = new ArrayList<>();
        fieldDefinitions.add(linkToImageFieldDefinition);
        fieldDefinitions.add(descriptionFieldDefinition);

        FieldDefinitionBlock ressFieldDefinitionBlock = new FieldDefinitionBlock(fieldDefinitions);

        ArrayList<PathElement> pathElements = new ArrayList<>();
        pathElements.add(new ParentPathElement());
        pathElements.add(new ParentPathElement());

        ClassSubject classSubject = new ClassSubject();
        classSubject.setIdentifier("media-content");
        FactorObject factorObject = new FactorObject(false, classSubject);

        Factor factor = new Factor(new Path(pathElements), factorObject);
        ArrayList<Factor> factors = new ArrayList<>();
        factors.add(factor);

        ArrayList<Term> terms = new ArrayList<>();
        terms.add(new Term(factors));

        Condition condition = new Condition(terms);
        ConditionSentence conditionSentence = new ConditionSentence(condition);
        ArrayList<ConditionSentence> conditionSentences = new ArrayList<>();
        conditionSentences.add(conditionSentence);

        ConditionsBlock conditionsBlock = new ConditionsBlock(conditionSentences);

        DefinitionBlock ressDefinitionBlock = new DefinitionBlock(ressTagSentence, conditionsBlock, ressClassLine, ressFieldDefinitionBlock, ressAmountSentence);
        Resource expectedRessResource = new Resource("ress", ressDefinitionBlock);

        Resource parsedRessResource = parsedResources.get("ress");
        assertEquals(expectedRessResource, parsedRessResource);
    }

    @Test
    public void testParseComparisonObject1() throws Exception
    {
        Lexer lexer = getLexer("== \"cosik\"");
        Parser parser = new Parser(lexer);
        ComparisonObject comparisonObject = parser.parseComparisonObjectForTest();
        ComparisonObject expectedComparisonObject = new ComparisonObject(new ComparisonOperator(TokenType.EQUAL), "cosik");

        assertEquals(expectedComparisonObject, comparisonObject);
    }

    @Test
    public void testParseComparisonObject2() throws Exception
    {
        Lexer lexer = getLexer("!= \"cosik\"");
        Parser parser = new Parser(lexer);
        ComparisonObject comparisonObject = parser.parseComparisonObjectForTest();
        ComparisonObject expectedComparisonObject = new ComparisonObject(new ComparisonOperator(TokenType.NOT_EQUAL), "cosik");

        assertEquals(expectedComparisonObject, comparisonObject);
    }

    @Test
    public void testParseComparisonObject3() throws Exception
    {
        Lexer lexer = getLexer("in {\"cosik\"}");
        Parser parser = new Parser(lexer);
        ArrayList<String> values = new ArrayList<>();
        values.add("cosik");
        ValueSet valueSet = new ValueSet(values);
        ComparisonObject comparisonObject = parser.parseComparisonObjectForTest();
        ComparisonObject expectedComparisonObject = new ComparisonObject(valueSet);

        assertEquals(expectedComparisonObject, comparisonObject);
    }
}
