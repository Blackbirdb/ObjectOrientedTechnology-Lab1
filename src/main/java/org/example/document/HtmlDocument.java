package org.example.document;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

// receiver
public class HtmlDocument {
    @Getter
    @Setter
    private HtmlElement root;
    @Getter private final Map<String, HtmlElement> idToElementMap;
//    @Getter
//    private final HtmlElementFactory factory;

    public HtmlDocument() {
//        this.factory = new HtmlElementFactory(this);
        this.idToElementMap = new HashMap<>();
    }

    public HtmlDocument(HtmlElement root) {
        this.root = root;
//        this.factory = new HtmlElementFactory(this);
        this.idToElementMap = new HashMap<>();
    }

    public HtmlElement getElementById(String id) {
        return idToElementMap.get(id);
    }

    public boolean existElementById(String id) {
        return (idToElementMap.containsKey(id));
    }

    public void registerElement(HtmlElement element) {
        idToElementMap.put(element.getId(), element);
    }

    public void unregisterElement(HtmlElement element) {
        idToElementMap.remove(element.getId());
    }

    public void init(){
        root = createElement("html", "html", null);
        HtmlElement head = createElement("head", "head", root);
        HtmlElement title = createElement("title", "title", head);
        HtmlElement body = createElement("body", "body", root);

        root.insertAtLast(head);
        root.insertAtLast(body);
        head.insertAtLast(title);
    }

    public static boolean isSpecialTag(String tagName) {
        return tagName.equals("html") || tagName.equals("head")
                || tagName.equals("title") || tagName.equals("body");
    }

    public void appendElement(String tagName, String idValue, String parentElement, String textContent) {
        HtmlElement parent = getElementById(parentElement);
        insertValidation(tagName,parent);
        HtmlElement element = createElement(tagName, idValue, textContent, parent);
        parent.insertAtLast(element);
    }

    public void insertElement(String tagName, String idValue, String parentElement, String textContent, int index) {
        HtmlElement parent = getElementById(parentElement);
        insertValidation(tagName,parent);
        HtmlElement element = createElement(tagName, idValue, textContent, parent);
        parent.insertAtIndex(index, element);
    }

    private void insertValidation(String tagName, HtmlElement parent){
        if (HtmlDocument.isSpecialTag(tagName)) {
            throw new IllegalArgumentException("Can't insert special tag element.");
        }
        if (parent == null) {
            throw new IllegalArgumentException("Parent element is null, can't insert into root element.");
        }
        else if (parent.getTagName().equals("html")) {
            throw new IllegalArgumentException("Can't insert into html element.");
        }
    }

    public void removeElement(String idValue) {
        validateElementExistence(idValue);

        HtmlElement elementToRemove = getElementById(idValue);
        if (HtmlDocument.isSpecialTag(elementToRemove.getTagName())) {
            throw new IllegalArgumentException("Can't delete special tag element.");
        }

        HtmlElement parent = (HtmlElement) elementToRemove.getParent();
        if (parent == null) {
            throw new IllegalArgumentException("Parent element is null, can't remove from root element.");
        }
        else if (parent.getTagName().equals("html")) {
            throw new IllegalArgumentException("Can't remove from html element.");
        }

        parent.removeChild(elementToRemove);
        unregisterElement(elementToRemove);
    }

    public String getParentId(String childId) {
        validateElementExistence(childId);
        HtmlElement element = getElementById(childId);
        HtmlElement parent = (HtmlElement) element.getParent();
        if (parent == null) {
            return null;
        }
        return parent.getId();
    }

    public int getElementIndex(String childId) {
        HtmlElement parent = getParent(childId);
        if (parent == null) {
            return -1;
        }
        return parent.getChildIndex(idToElementMap.get(childId));
    }

    public String getElementText(String childId) {
        validateElementExistence(childId);
        HtmlElement element = getElementById(childId);
        return element.getTextContent();
    }

    public String getElementTag(String childId) {
        validateElementExistence(childId);
        HtmlElement element = getElementById(childId);
        return element.getTagName();
    }

    private HtmlElement getParent(String childId) {
        validateElementExistence(childId);
        HtmlElement element = getElementById(childId);
        return (HtmlElement) element.getParent();
    }

    public void validateElementExistence(String id) {
        if (!idToElementMap.containsKey(id)) {
            throw new IllegalArgumentException("Element with ID " + id + " does not exist.");
        }
    }

    public void editId(String oldId, String newId) {
        if (!existElementById(oldId)) {
            throw new IllegalArgumentException("Element with ID " + oldId + " does not exist.");
        }

        if (existElementById(newId)) {
            throw new IllegalArgumentException("Element with ID " + newId + " already exists, can't change ID.");
        }

        HtmlElement element = idToElementMap.get(oldId);
        unregisterElement(element);
        element.setId(newId);
        registerElement(element);
    }

    public String editText(String elementId, String newText) {
        validateElementExistence(elementId);
        HtmlElement element = getElementById(elementId);
        String oldTextContent = element.getTextContent();
        element.setTextContent(newText);

        return oldTextContent;
    }

    /**
     * create element is only responsible for creating the element and registering it in the document.
     * it does not add the element to the parent, because we don't know where to add it.
     */
    public HtmlElement createElement(String tagName, String id, String textContent, HtmlElement parent) {

        HtmlElement element = createElement(tagName, id, parent);

        if (textContent != null) {
            element.setTextContent(textContent);
        }

        return element;
    }

    public HtmlElement createElement(String tagName, String id, HtmlElement parent) {

        if (!isSpecialTag(tagName) && (id == null || id.isEmpty())) {
            throw new IllegalArgumentException("Tag <" + tagName + "> is not a special tag, and no id is given.");
        }
        else if (getElementById(id) != null) {
            throw new IllegalArgumentException("Element <" + id + "> is already contained in the document.");
        }
        else if (parent == null && !tagName.equals("html")) {
            throw new IllegalArgumentException("Parent element is null, can't create element.");
        }

        HtmlElement element = new HtmlElement(tagName, id, parent);

        if (isSpecialTag(tagName) && (id == null || id.isEmpty())) {
            element.setId(tagName);
        }

        registerElement(element);

        return element;
    }
}