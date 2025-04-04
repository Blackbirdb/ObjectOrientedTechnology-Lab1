package org.example.document;

public class HtmlElementFactory {
    private final HtmlDocument document;

    public HtmlElementFactory(HtmlDocument document) {
        this.document = document;
    }

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
        else if (document.getElementById(id) != null) {
            throw new IllegalArgumentException("Element <" + id + "> is already contained in the document.");
        }

        HtmlElement element = new HtmlElement(tagName, id, parent);

        if (isSpecialTag(tagName) && (id == null || id.isEmpty())) {
            element.setId(tagName);
        }

        document.registerElement(element);

        return element;
    }

    private boolean isSpecialTag(String tagName) {
        return tagName.equals("html") || tagName.equals("head")
                || tagName.equals("title") || tagName.equals("body");
    }
}