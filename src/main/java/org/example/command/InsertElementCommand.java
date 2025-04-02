package org.example.command;

import org.example.document.HtmlElement;
import org.example.document.HtmlNode;

public class InsertElementCommand implements Command {
    private HtmlEditor editor;
    private String tagName;
    private String idValue;
    private String insertLocation;
    private String textContent;

    public InsertElementCommand(HtmlEditor editor, String tagName, String idValue, String insertLocation, String textContent) {
        this.editor = editor;
        this.tagName = tagName;
        this.idValue = idValue;
        this.insertLocation = insertLocation;
        this.textContent = textContent;
    }

    @Override
    public void execute() {
        HtmlElement parent = editor.getElementById(insertLocation).getParent();
        HtmlElement newElement = editor.getFactory().createElement(tagName, idValue, textContent, parent);
        parent.insertBefore(newElement, editor.getElementById(insertLocation));
    }

    @Override
    public void undo() {
        HtmlElement elementToRemove = editor.getElementById(idValue);
        HtmlElement parent = editor.getElementById(insertLocation).getParent();
        parent.removeChild(elementToRemove);
        editor.getDocument().unregisterElement(elementToRemove);
    }
}