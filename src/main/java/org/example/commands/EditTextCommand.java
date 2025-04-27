package org.example.commands;

import org.example.document.HtmlDocument;

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
        oldTextContent = document.editText(elementId, newTextContent);
    }

    @Override
    public void undo() {
        document.editText(elementId, oldTextContent);
    }
}