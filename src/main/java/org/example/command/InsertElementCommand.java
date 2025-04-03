package org.example.command;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.example.document.HtmlNode;

public class InsertElementCommand implements Command {
    private HtmlDocument document;
    private String tagName;
    private String idValue;
    private String insertLocation;
    private String textContent;

    public InsertElementCommand(HtmlDocument document, String tagName, String idValue, String insertLocation, String textContent) {
        this.document = document;
        this.tagName = tagName;
        this.idValue = idValue;
        this.insertLocation = insertLocation;
        this.textContent = textContent;
    }

    public InsertElementCommand(HtmlDocument document, String tagName, String idValue, String insertLocation) {
        this.document = document;
        this.tagName = tagName;
        this.idValue = idValue;
        this.insertLocation = insertLocation;
        this.textContent = null;
    }

    @Override
    public void execute() {
        HtmlElement parent = document.getElementById(insertLocation).getParent();
        HtmlElement newElement = document.getFactory().createElement(tagName, idValue, textContent, parent);
        parent.insertBefore(newElement, document.getElementById(insertLocation));
    }

    @Override
    public void undo() {
        HtmlElement elementToRemove = document.getElementById(idValue);
        HtmlElement parent = document.getElementById(insertLocation).getParent();
        parent.removeChild(elementToRemove);
        document.unregisterElement(elementToRemove);
    }
}