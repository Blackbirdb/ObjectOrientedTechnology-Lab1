package org.example.command;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.example.document.HtmlElementFactory;

import java.util.Stack;

public class HtmlEditor {
    private final HtmlDocument document;
    private final HtmlElementFactory factory;
    private final CommandHistory history = new CommandHistory();

    public HtmlEditor(HtmlDocument document) {
        this.document = document;
        this.factory = document.getFactory();
    }

    public HtmlElement getElementById(String id) {
        return document.getElementById(id);
    }

    public HtmlElementFactory getFactory() {
        return factory;
    }

    public HtmlDocument getDocument() {
        return document;
    }

    //
//    // 插入元素
//    public void insertElement(String tagName, String id, String insertAfterId) {
//        HtmlElement newElement = new HtmlElement(tagName);
//        newElement.setId(id);
//        Command cmd = new InsertElementCommand(document, newElement, insertAfterId);
//        history.executeCommand(cmd);
//    }
//
//    // 删除元素
//    public void deleteElement(String elementId) {
//        Command cmd = new DeleteElementCommand(document, elementId);
//        history.executeCommand(cmd);
//    }
//
//    // 修改ID
//    public void changeElementId(String oldId, String newId) {
//        HtmlElement element = document.getElementById(oldId);
//        if (element != null) {
//            Command cmd = new ChangeIdCommand(document, element, newId);
//            history.executeCommand(cmd);
//        }
//    }
//
//    // 修改文本
//    public void changeText(String elementId, String newText) {
//        HtmlElement element = document.getElementById(elementId);
//        if (element != null) {
//            Command cmd = new ChangeTextCommand(element, newText);
//            history.executeCommand(cmd);
//        }
//    }

    // 撤销
    public void undo() {
        history.undo();
    }

    // 重做
    public void redo() {
        history.redo();
    }
}