package org.example.command;

import org.example.editor.HtmlEditor;

public class InsertTextCommand implements Command {
    private HtmlEditor editor;
    private String textToInsert;
    private String previousHtml;

    public InsertTextCommand(HtmlEditor editor, String text) {
        this.editor = editor;
        this.textToInsert = text;
    }

    @Override
    public void execute() {
        previousHtml = editor.getHtmlContent();
        editor.edit(previousHtml + textToInsert);
    }

    @Override
    public void undo() {
        editor.edit(previousHtml);
    }
}