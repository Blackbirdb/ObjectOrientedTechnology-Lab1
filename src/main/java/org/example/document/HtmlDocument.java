package org.example.document;

import java.util.Map;

public class HtmlDocument {
    private HtmlElement root;
    private Map<String, HtmlElement> idToElementMap;

    public HtmlDocument() {}

    public HtmlDocument(HtmlElement root) {
        this.root = root;
    }

    public HtmlElement getElementById(String id) {
        return idToElementMap.get(id);
    }

    public void registerElement(HtmlElement element) {
        if (getElementById(element.getId()) != null) {
            throw new IllegalArgumentException("Element with ID " + element.getId() + " already exists");
        }
        idToElementMap.put(element.getId(), element);
    }

    public void setRoot(HtmlElement root) {
        this.root = root;
    }

    public HtmlElement getRoot() {
        return this.root;
    }
}