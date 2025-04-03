package org.example.command;

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
        HtmlElement element = document.getFactory().createElement(tagName, idValue, textContent, parent);
        parent.insertAtLast(element);
    }
    @Override
    public void undo() {
        HtmlElement elementToRemove = document.getElementById(idValue);
        HtmlElement parent = document.getElementById(parentElement);
        parent.removeChild(elementToRemove);
        document.unregisterElement(elementToRemove);
    }
}
