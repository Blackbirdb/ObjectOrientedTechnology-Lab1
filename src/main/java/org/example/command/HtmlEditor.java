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
    private int modifiedCount = 0;
    @Getter
    private final String filePath;

    public HtmlEditor(String filePath) throws IOException {
        this.document = HtmlFileParser.readHtmlFromFile(filePath);
        this.filePath = filePath;
        this.history = new CommandHistory();
    }

    public boolean isModified(){
        return modifiedCount > 0;
    }

    public HtmlElement getElementById(String id) {
        return document.getElementById(id);
    }

    public void insertElement(String tagName, String idValue, String insertLocation, String textContent) {
        Command cmd = new InsertElementCommand(document, tagName, idValue, insertLocation, textContent);
        history.executeCommand(cmd);
        modifiedCount++;
    }

    public void appendElement(String tagName, String idValue, String parentElement, String textContent) {
        Command cmd = new AppendElementCommand(document, tagName, idValue, parentElement, textContent);
        history.executeCommand(cmd);
        modifiedCount++;
    }

    public void editId(String oldId, String newId) {
        Command cmd = new EditIdCommand(document, oldId, newId);
        history.executeCommand(cmd);
        modifiedCount++;
    }

    public void editText(String element, String newTextContent) {
        Command cmd = new EditTextCommand(document, element, newTextContent);
        history.executeCommand(cmd);
        modifiedCount++;
    }

    public void deleteElement(String elementId) {
        Command cmd = new DeleteElementCommand(document, elementId);
        history.executeCommand(cmd);
        modifiedCount++;
    }

    public void undo() {
        history.undo();
        modifiedCount--;
    }

    public void redo() {
        history.redo();
        modifiedCount++;
    }

    public void saveToFile(String filePath) throws IOException {
        HtmlFileParser.saveHtmlDocumentToFile(document, filePath);
    }

    public void save() throws IOException {
        HtmlFileParser.saveHtmlDocumentToFile(document, this.filePath);
        modifiedCount = 0;
    }

    public void printTree() {
        TreePrinter.print(document);
    }

    public void spellCheck(){
        SpellChecker.printErrorMap(document);
    }
}