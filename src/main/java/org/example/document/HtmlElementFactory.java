package org.example.document;

public class HtmlElementFactory {
    private final HtmlDocument document;

    public HtmlElementFactory(HtmlDocument document) {
        this.document = document;
    }

    public HtmlElement createElement(String tagName, String id, String textContent, HtmlElement parent) {

        if (document.getElementById(id) != null) {
            throw new IllegalArgumentException("Element with ID " + id + " already exists");
        }

        HtmlElement element = new HtmlElement(tagName, id, textContent, null, parent);

        document.registerElement(element);

        return element;
    }
}