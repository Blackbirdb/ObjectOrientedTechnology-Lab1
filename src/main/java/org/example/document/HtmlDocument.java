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
    private final Map<String, HtmlElement> idToElementMap;
    @Getter
    private final HtmlElementFactory factory;

    public HtmlDocument() {
        this.factory = new HtmlElementFactory(this);
        this.idToElementMap = new HashMap<>();
    }

    public HtmlDocument(HtmlElement root) {
        this.root = root;
        this.factory = new HtmlElementFactory(this);
        this.idToElementMap = new HashMap<>();
    }

    public HtmlElement getElementById(String id) {
        return idToElementMap.get(id);
    }

    public void registerElement(HtmlElement element) {
        idToElementMap.put(element.getId(), element);
    }

    public void unregisterElement(HtmlElement element) {
        idToElementMap.remove(element.getId());
    }

    public void init(){
        root = factory.createElement("html", "html", null);
        HtmlElement head = factory.createElement("head", "head", root);
        HtmlElement title = factory.createElement("title", "title", head);
        HtmlElement body = factory.createElement("body", "body", root);

        root.insertAtLast(head);
        root.insertAtLast(body);
        head.insertAtLast(title);
    }

    public static boolean isSpecialTag(String tagName) {
        return tagName.equals("html") || tagName.equals("head")
                || tagName.equals("title") || tagName.equals("body");
    }
}