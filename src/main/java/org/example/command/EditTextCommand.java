package org.example.command;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;

public class EditTextCommand implements Command {
    private final HtmlDocument document;
    private final String elementId;
    private final String newTextContent;
    private String oldTextContent;

    public EditTextCommand(HtmlDocument document, String elementId, String newTextContent) {
        this.document = document;
        this.elementId = elementId;
        this.newTextContent = newTextContent;
        this.oldTextContent = null;
    }

    @Override
    public void execute() {
        HtmlElement element = document.getElementById(elementId);
        if (element == null) {
            throw new IllegalArgumentException("Element with ID " + elementId + " does not exist.");
        }
        oldTextContent = element.getTextContent();
        element.setTextContent(newTextContent);
    }

    @Override
    public void undo() {
        HtmlElement element = document.getElementById(elementId);
        if (element == null) {
            throw new IllegalArgumentException("Element with ID " + elementId + " does not exist, undo failed.");
        }
        element.setTextContent(oldTextContent);
    }
}