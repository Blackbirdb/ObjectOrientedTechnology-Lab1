package org.example.editor;

import java.util.Stack;

public class HtmlEditor {
    private String htmlContent;
    private Stack<String> undoStack = new Stack<>();
    private Stack<String> redoStack = new Stack<>();

    public HtmlEditor(String initialHtml) {
        this.htmlContent = initialHtml;
    }

    public void edit(String newHtml) {
        undoStack.push(htmlContent);
        htmlContent = newHtml;
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(htmlContent);
            htmlContent = undoStack.pop();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(htmlContent);
            htmlContent = redoStack.pop();
        }
    }

    public String getHtmlContent() {
        return htmlContent;
    }
}
