package org.example.editor;

import lombok.Getter;
import lombok.Setter;
import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.example.service.HtmlFileParser;

import java.io.IOException;


public class HtmlEditor {
    @Getter @Setter private HtmlDocument document;
    private final CommandHistory history;
    @Getter private final String filePath;
    @Getter @Setter private Boolean showId;

    public HtmlEditor(String filePath) {
        this.document = HtmlFileParser.readHtmlFromFile(filePath);
        this.filePath = filePath;
        this.history = new CommandHistory();
        this.showId = true;
    }

    public HtmlElement getElementById(String id) {
        return document.getElementById(id);
    }

    public boolean isModified() {
        return history.isModified();
    }

    /************************** Revocable Commands **************************/
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

    /************************** Irrevocable Commands **************************/

    public void saveToFile(String filePath) {
        IrrevocableCommand cmd = new SaveFileCommand(document, filePath);
        history.executeCommand(cmd);
        history.resetModified();
    }

    public void save() {
        SaveFileCommand cmd = new SaveFileCommand(document, this.filePath);
        history.executeCommand(cmd);
        history.resetModified();
    }

    public void printTree() {
        IrrevocableCommand cmd = new PrintTreeCommand(document, showId);
        history.executeCommand(cmd);
    }

    public void spellCheck() {
        IrrevocableCommand cmd = new SpellCheckCommand(document);
        history.executeCommand(cmd);
    }
}