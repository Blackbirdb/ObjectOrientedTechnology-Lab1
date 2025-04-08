package org.example.editor.commands;

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
        document.editId(oldId, newId);
    }

    @Override
    public void undo() {
        document.editId(newId, oldId);
    }
}