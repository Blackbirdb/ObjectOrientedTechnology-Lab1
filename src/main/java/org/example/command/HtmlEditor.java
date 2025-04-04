package org.example.command;

import lombok.Getter;
import lombok.Setter;
import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.example.service.HtmlFileParser;
import org.example.service.SpellChecker;
import org.example.service.TreePrinter;

import java.io.IOException;


public class HtmlEditor {
    @Getter
    @Setter
    private HtmlDocument document;
    private final CommandHistory history;

    public HtmlEditor(HtmlDocument document) {
        this.document = document;
        this.history = new CommandHistory();
    }

    public HtmlEditor(String filePath) throws IOException {
        this.document = HtmlFileParser.readHtmlFromFile(filePath);
        this.history = new CommandHistory();
    }

    public HtmlElement getElementById(String id) {
        return document.getElementById(id);
    }

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

    public void deleteElement(String elementId) {
        Command cmd = new DeleteElementCommand(document, elementId);
        history.executeCommand(cmd);
    }

    public void undo() {
        history.undo();
    }

    public void redo() {
        history.redo();
    }

    public void saveToFile(String filePath) throws IOException {
        HtmlFileParser.saveHtmlDocumentToFile(document, filePath);
    }

    public void printTree() {
        TreePrinter.print(document);
    }

    public void spellCheck(){
        SpellChecker.printErrorMap(document);
    }
}