package org.example.editor.commands;

import lombok.NonNull;
import org.example.document.HtmlDocument;

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
        String parent = document.getParentId(insertLocation);
        int index = document.getElementIndex(insertLocation);
        document.insertElement(tagName, idValue, parent, textContent, index);
    }

    @Override
    public void undo() {
        document.removeElement(idValue);
    }
}