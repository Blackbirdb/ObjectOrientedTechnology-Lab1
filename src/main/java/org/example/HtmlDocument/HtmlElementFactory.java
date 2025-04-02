package org.example.HtmlDocument;

public class HtmlElementFactory {
    private final HtmlDocument document;

    public HtmlElementFactory(HtmlDocument document) {
        this.document = document;
    }

    public HtmlElement createElement(String tagName, String id, String textContent) {

        if (document.getElementById(id) != null) {
            throw new IllegalArgumentException("Element with ID " + id + " already exists");
        }

        HtmlElement element = new HtmlElement(tagName, id, textContent, null);

        document.registerElement(element);

        return element;
    }
}