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

        HtmlElement element = createElement(tagName, id, parent);

        if (textContent != null) {
            element.setTextContent(textContent);
        }

        return element;
    }

    public HtmlElement createElement(String tagName, String id, HtmlElement parent) {

        if (isSpecialTag(tagName)) {
            HtmlElement element = new HtmlElement(tagName, id, parent);
            return element;
        }

        else if (id == null || id.isEmpty() || document.getElementById(id) != null) {
            throw new IllegalArgumentException("Element illegal.");
        }

        HtmlElement element = new HtmlElement(tagName, id, parent);

        document.registerElement(element);

        return element;
    }

    private boolean isSpecialTag(String tagName) {
        return tagName.equals("html") || tagName.equals("head")
                || tagName.equals("title") || tagName.equals("body");
    }
}