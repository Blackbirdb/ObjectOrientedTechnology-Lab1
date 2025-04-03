package org.example.document;

import org.example.visitor.HtmlVisitor;

import java.util.HashMap;
import java.util.Map;

public class HtmlDocument {
    private HtmlElement root;
    private Map<String, HtmlElement> idToElementMap;
    private HtmlElementFactory factory;

    public HtmlDocument() {
        this.factory = new HtmlElementFactory(this);
        this.idToElementMap = new HashMap<>();
    }

    public HtmlDocument(HtmlElement root) {
        this.root = root;
        this.factory = new HtmlElementFactory(this);
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

    public void unregisterElement(HtmlElement element) {
        idToElementMap.remove(element.getId());
    }

    public void setRoot(HtmlElement root) {
        this.root = root;
    }

    public HtmlElement getRoot() {
        return this.root;
    }

    public HtmlElementFactory getFactory() {
        return this.factory;
    }

    public void accept(HtmlVisitor visitor) {
        root.accept(visitor);
    }
}