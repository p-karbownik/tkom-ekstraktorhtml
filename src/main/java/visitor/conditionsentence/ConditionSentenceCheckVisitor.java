package visitor.conditionsentence;

import parser.structures.*;
import parserhtml.structures.Attribute;
import parserhtml.structures.Element;
import parserhtml.structures.Root;
import parserhtml.structures.Tag;

import java.util.ArrayList;
import java.util.Collections;

public class ConditionSentenceCheckVisitor implements Visitor {
    private Element htmlRoot;

    public boolean checkCondition(ConditionSentence conditionSentence) {
        return (boolean) conditionSentence.accept(this);
    }

    public boolean checkCondition(ConditionSentence conditionSentence, Element root) {
        this.htmlRoot = root;
        return checkCondition(conditionSentence);
    }

    public boolean checkCondition(Condition condition, Element root)
    {
        this.htmlRoot = root;
        return (boolean) condition.accept(this);
    }

    public void setRoot(Element element) {
        htmlRoot = element;
    }

    @Override
    public Object visit(ConditionSentence conditionSentence) {
        return conditionSentence.getCondition().accept(this);
    }

    @Override
    public Object visit(Condition condition) {
        for (Term term : condition.getTerms())
            if (((boolean) term.accept(this)))
                return true;

        return false;
    }

    @Override
    public Object visit(Term term) {
        for (Factor factor : term.getFactors()) {
            if (!((boolean) factor.accept(this)))
                return false;
        }
        return true;
    }

    @Override
    public Object visit(Factor factor) {
        if (factor.getCondition() != null)
            return factor.getCondition().accept(this);

        Path.PathNode root = factor.getPath().getRoot();

        if (root.getPathElement() instanceof DescendantPathElement)
            return handleDescendantPathElement(htmlRoot, factor.getFactorObject());

        if (root.getPathElement() instanceof AncestorPathElement)
            return handleAncestorPathElement(root, factor.getFactorObject());

        if (root.getPathElement() instanceof ParentPathElement)
            return handleParentPathElement(root, factor.getFactorObject());

        if (root.getPathElement() instanceof ChildPathElement)
            return handleChildPathElement(root, factor.getFactorObject());

        if (root.getPathElement() instanceof SelfPathElement)
            return handleSelfPathElement(factor.getFactorObject());

        return false;

    }

    private boolean handleParentPathElement(Path.PathNode root, FactorObject factorObject) {
        Element relativeNode = htmlRoot;
        Path.PathNode currentPathRoot = root;

        while (currentPathRoot != null) {
            if (relativeNode.getParent() != null) {
                relativeNode = relativeNode.getParent();
                currentPathRoot = currentPathRoot.getNext();
            } else
                return false;
        }

        if (relativeNode instanceof Root)
            return false;

        return checkFactorObject((Tag) relativeNode, factorObject);
    }

    private boolean handleSelfPathElement(FactorObject factorObject) {
        if (htmlRoot instanceof Root)
            return false;

        return checkFactorObject((Tag) htmlRoot, factorObject);
    }

    private boolean handleChildPathElement(Path.PathNode root, FactorObject factorObject) {
        Element relativeRoot = htmlRoot;
        Path.PathNode currentPathRoot = root;

        // children
        // budowanie zbioru

        ArrayList<Tag> currentTags = new ArrayList<>();
        ArrayList<Tag> resultTagsArray = new ArrayList<>();

        for (Element e : ((Tag) relativeRoot).getChildren())
            if (e instanceof Tag)
                currentTags.add((Tag) e);

        while (!currentTags.isEmpty() && currentPathRoot != null)
        {
            ChildPathElement childPathElement = (ChildPathElement) currentPathRoot.getPathElement();

            ArrayList<Tag> filteredTagsArrayList = new ArrayList<>();

            if(childPathElement.getRelativeCondition() == null)
                filteredTagsArrayList.addAll(currentTags);
            else
                for(Tag e : currentTags)
                {
                    ConditionSentenceCheckVisitor conditionSentenceCheckVisitor = new ConditionSentenceCheckVisitor();

                    if(conditionSentenceCheckVisitor.checkCondition(childPathElement.getRelativeCondition().getCondition(), e))
                        filteredTagsArrayList.add(e);
                }

            if(currentPathRoot.getNext() == null)
            {
                resultTagsArray.addAll(filteredTagsArrayList);
                break;
            }

            currentPathRoot = currentPathRoot.getNext();

            currentTags = new ArrayList<>();

            for(Tag tag : filteredTagsArrayList)
                for(Element element : tag.getChildren())
                    if(element instanceof Tag)
                        currentTags.add((Tag) element);

        }

        // sprawdzanie warunkow we zbiorze

        for (Tag tag : resultTagsArray) {
            if (checkFactorObject(tag, factorObject))
                return true;
        }

        return false;//<- ogolnie ta czesc nalezy wydzielic do osobnej funkcji (najlepiej tak zrobic)
    }

    @Override
    public Object visit(FactorObject factorObject) {
        return null;
    }

    private boolean checkFactorObject(Tag tag, FactorObject factorObject) {
        Subject subject = factorObject.getSubject();

        if (subject instanceof TagSubject)
            return factorObject.isNegated() ? !(tag.getName().compareTo(subject.getIdentifier()) == 0) : (tag.getName().compareTo(subject.getIdentifier()) == 0);

        else if (subject instanceof ClassSubject) {
            Attribute classAttribute = null;

            for (Attribute attribute : tag.getAttributes())
                if (attribute.getName().compareTo("class") == 0) {
                    classAttribute = attribute;
                    break;
                }

            if (classAttribute == null)
                return false;

            return factorObject.isNegated() ? !(classAttribute.getValue().compareTo(subject.getIdentifier()) == 0) : (classAttribute.getValue().compareTo(subject.getIdentifier()) == 0) ;
        } else {
            for (Attribute attribute : tag.getAttributes()) {
                if (attribute.getName().compareTo(subject.getIdentifier()) == 0) {

                    if(factorObject.getComparisonObject() == null)
                        return !factorObject.isNegated();

                    ComparisonObject comparisonObject = factorObject.getComparisonObject();

                    // porownanie wartosci
                    ComparisonOperator comparisonOperator = comparisonObject.getComparisonOperator();

                    if(comparisonOperator == null)
                    {
                        //przypadek, gdy mamy ValueSet
                        ValueSet valueSet = comparisonObject.getValueSet();

                        return factorObject.isNegated() ? !valueSet.getValues().contains(attribute.getValue()) : valueSet.getValues().contains(attribute.getValue());
                    }

                    if(comparisonOperator.isEqualType())
                        return factorObject.isNegated() ? !(attribute.getValue().compareTo(comparisonObject.getValue()) == 0) : (attribute.getValue().compareTo(comparisonObject.getValue()) == 0);
                    else
                        return factorObject.isNegated() ? !(attribute.getValue().compareTo(comparisonObject.getValue()) != 0) : (attribute.getValue().compareTo(comparisonObject.getValue()) != 0);
                }
            }

            return factorObject.isNegated();

        }
    }

    private boolean handleAncestorPathElement(Path.PathNode root, FactorObject factorObject) {
        if (htmlRoot.getParent() == null || htmlRoot.getParent() instanceof Root)
            return false;

        Tag parent = (Tag) htmlRoot.getParent();

        while (parent != null) {
            if (checkFactorObject(parent, factorObject))
                return true;

            if (parent.getParent() instanceof Root)
                break;

            parent = (Tag) parent.getParent();
        }

        return false;
    }

    private boolean handleDescendantPathElement(Element element, FactorObject factorObject) {
        if (!(element instanceof Tag))
            return false;

        for (Element e : ((Tag) element).getChildren()) {
            if (handleDescendantPathElement(e, factorObject))
                return true;
        }


        return checkFactorObject((Tag) element, factorObject);
    }
}
