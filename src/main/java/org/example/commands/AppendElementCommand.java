package org.example.commands;

import org.example.document.HtmlDocument;

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
        document.appendElement(tagName, idValue, parentElement, textContent);
    }
    @Override
    public void undo() {
        document.removeElement(tagName);
    }
}
