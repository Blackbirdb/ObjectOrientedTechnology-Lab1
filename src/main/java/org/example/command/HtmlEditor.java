package org.example.command;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.example.document.HtmlElementFactory;

import java.util.Stack;

public class HtmlEditor {
    private final HtmlDocument document;
    private final CommandHistory history = new CommandHistory();

    public HtmlEditor(HtmlDocument document) {
        this.document = document;
    }

    public HtmlElement getElementById(String id) {
        return document.getElementById(id);
    }

    // 插入元素
    public void insertElement(String tagName, String idValue, String insertLocation, String textContent) {
        Command cmd = new InsertElementCommand(document, tagName, idValue, insertLocation, textContent);
        history.executeCommand(cmd);
    }

    public void appendElement(String tagName, String idValue, String parentElement, String textContent) {
        Command cmd = new AppendElementCommand(document, tagName, idValue, parentElement, textContent);
        history.executeCommand(cmd);
    }

    public void editId(String oldId, String newId) {
        Command cmd = new EditIdCommand(document, oldId, newId);
        history.executeCommand(cmd);
    }

    public void editText(String element, String newTextContent) {
        Command cmd = new EditTextCommand(document, element, newTextContent);
        history.executeCommand(cmd);
    }

    // 删除元素
    public void deleteElement(String elementId) {
        Command cmd = new DeleteElementCommand(document, elementId);
        history.executeCommand(cmd);
    }

    // 撤销
    public void undo() {
        history.undo();
    }

    // 重做
    public void redo() {
        history.redo();
    }
}