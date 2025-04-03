package org.example.document;

public class HtmlElementFactory {
    private final HtmlDocument document;

    public HtmlElementFactory(HtmlDocument document) {
        this.document = document;
    }

    /**
     * Creates an HTML element with the specified tag name, ID, and text content.
     * Already checks if id is repeated.
     * @param tagName
     * @param id
     * @param textContent
     * @param parent
     * @return
     */
    public HtmlElement createElement(String tagName, String id, String textContent, HtmlElement parent) {

        if (document.getElementById(id) != null) {
            throw new IllegalArgumentException("Element with ID " + id + " already exists");
        }

        HtmlElement element = new HtmlElement(tagName, id, null, parent);

        if (textContent != null) {
            element.setTextContent(textContent);
        }

        document.registerElement(element);

        return element;
    }
}