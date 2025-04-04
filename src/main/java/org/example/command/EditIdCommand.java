package org.example.command;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;

public class EditIdCommand implements Command {
    private final HtmlDocument document;
    private final String oldId;
    private final String newId;

    public EditIdCommand(HtmlDocument document, String oldId, String newId) {
        this.document = document;
        this.oldId = oldId;
        this.newId = newId;
    }

    @Override
    public void execute() {
        HtmlElement element = document.getElementById(oldId);

        if (element == null) {
            throw new IllegalArgumentException("Element with ID " + oldId + " does not exist.");
        }

        if (document.getElementById(newId) != null) {
            throw new IllegalArgumentException("Element with ID " + newId + " already exists");
        }

        document.unregisterElement(element);
        element.setId(newId);
        document.registerElement(element);
    }

    @Override
    public void undo() {
        HtmlElement element = document.getElementById(newId);

        if (element == null) {
            throw new IllegalArgumentException("Element with ID " + newId + " does not exist.");
        }

        if (document.getElementById(oldId) != null) {
            throw new IllegalArgumentException("Element with ID " + oldId + " already exists, undo failed.");
        }

        document.unregisterElement(element);
        element.setId(oldId);
        document.registerElement(element);
    }
}