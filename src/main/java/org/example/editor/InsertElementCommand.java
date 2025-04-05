package org.example.editor;

import lombok.NonNull;
import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;

public class InsertElementCommand implements Command {
    private final HtmlDocument document;
    private final String tagName;
    private final String idValue;
    private final String insertLocation;
    private final String textContent;

    public InsertElementCommand(@NonNull HtmlDocument document, String tagName,
                                 String idValue, String insertLocation, String textContent) {
        this.document = document;
        this.tagName = tagName;
        this.idValue = idValue;
        this.insertLocation = insertLocation;
        this.textContent = textContent;
    }

    @Override
    public void execute() {
        if (document.isSpecialTag(tagName)) {
            throw new IllegalArgumentException("Can't insert special tag element.");
        }

        HtmlElement element = document.getElementById(insertLocation);
        if (element == null) {
            throw new IllegalArgumentException("Element with ID " + insertLocation + " does not exist.");
        }

        HtmlElement parent = element.getParent();
        if (parent == null) {
            throw new NullPointerException("Parent element is null, can't insert into root element.");
        }
        else if (parent.getTagName().equals("html")) {
            throw new IllegalArgumentException("Can't insert into html element.");
        }

        HtmlElement newElement = document.getFactory().createElement(tagName, idValue, textContent, parent);
        parent.insertBefore(newElement, document.getElementById(insertLocation));
    }

    @Override
    public void undo() {
        HtmlElement elementToRemove = document.getElementById(idValue);
        if (elementToRemove == null) {
            throw new IllegalArgumentException("Element with ID " + idValue + " does not exist.");
        }
        else if (document.isSpecialTag(tagName)) {
            throw new IllegalArgumentException("Can't delete special tag element.");
        }

        HtmlElement parent = elementToRemove.getParent();
        if (parent == null) {
            throw new IllegalArgumentException("Parent element is null, can't remove from root element.");
        }
        else if (parent.getTagName().equals("html")) {
            throw new IllegalArgumentException("Can't remove from html element.");
        }

        document.unregisterElement(elementToRemove);
        parent.removeChild(elementToRemove);
    }
}