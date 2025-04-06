package org.example.document;

import static org.example.document.HtmlDocument.isSpecialTag;

public class HtmlElementFactory {
    private final HtmlDocument document;

    public HtmlElementFactory(HtmlDocument document) {
        this.document = document;
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
        else if (document.getElementById(id) != null) {
            throw new IllegalArgumentException("Element <" + id + "> is already contained in the document.");
        }
        else if (parent == null && !tagName.equals("html")) {
            throw new IllegalArgumentException("Parent element is null, can't create element.");
        }

        HtmlElement element = new HtmlElement(tagName, id, parent);

        if (isSpecialTag(tagName) && (id == null || id.isEmpty())) {
            element.setId(tagName);
        }

        document.registerElement(element);

        return element;
    }

}