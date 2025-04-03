package org.example.command;

import lombok.NonNull;
import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.example.document.HtmlNode;

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
        HtmlElement parent = document.getElementById(insertLocation).getParent();
        if (parent == null) {
            throw new NullPointerException("Parent element is null, can't insert into root element.");
        }
        HtmlElement newElement = document.getFactory().createElement(tagName, idValue, textContent, parent);
        parent.insertBefore(newElement, document.getElementById(insertLocation));
    }

    @Override
    public void undo() {
        HtmlElement elementToRemove = document.getElementById(idValue);
        HtmlElement parent = elementToRemove.getParent();
        parent.removeChild(elementToRemove);
        document.unregisterElement(elementToRemove);
    }
}