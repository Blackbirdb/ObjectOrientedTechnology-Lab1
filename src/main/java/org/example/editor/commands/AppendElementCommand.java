package org.example.editor.commands;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;

public class AppendElementCommand implements Command {
    private final HtmlDocument document;
    private final String tagName;
    private final String idValue;
    private final String parentElement;
    private final String textContent;

    public AppendElementCommand(HtmlDocument document, String tagName, String idValue, String parentElement, String textContent) {
        this.document = document;
        this.tagName = tagName;
        this.idValue = idValue;
        this.parentElement = parentElement;
        this.textContent = textContent;
    }

    @Override
    public void execute() {
        HtmlElement parent = document.getElementById(parentElement);
        if (parent == null) {
            throw new IllegalArgumentException("Parent element is null, can't append to root element.");
        }
        else if (parent.getTagName().equals("html")) {
            throw new IllegalArgumentException("Can't append to html element.");
        }
        else if (HtmlDocument.isSpecialTag(tagName)) {
            throw new IllegalArgumentException("Can't append special tag element.");
        }
        HtmlElement element = document.getFactory().createElement(tagName, idValue, textContent, parent);
        parent.insertAtLast(element);
    }
    @Override
    public void undo() {
        HtmlElement elementToRemove = document.getElementById(idValue);
        if (elementToRemove == null) {
            throw new IllegalArgumentException("Element with ID " + idValue + " does not exist.");
        }
        else if (HtmlDocument.isSpecialTag(tagName)) {
            throw new IllegalArgumentException("Can't delete special tag element.");
        }

        HtmlElement parent = document.getElementById(parentElement);
        if (parent == null) {
            throw new IllegalArgumentException("Parent element is null, can't remove from root element.");
        }
        else if (parent.getTagName().equals("html")) {
            throw new IllegalArgumentException("Can't remove from html element.");
        }

        parent.removeChild(elementToRemove);
        document.unregisterElement(elementToRemove);
    }
}
