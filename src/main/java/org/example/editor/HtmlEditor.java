package org.example.editor;

import lombok.Getter;
import lombok.Setter;
import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.example.editor.commands.*;
import org.example.tools.htmlparser.HtmlFileParser;
import org.example.tools.htmlparser.JsoupFileParser;
import org.example.tools.spellchecker.SpellChecker;

// invoker
public class HtmlEditor {
    @Getter @Setter private HtmlDocument document;
    private final CommandHistory history;
    @Getter @Setter private String filePath;
    @Getter @Setter private Boolean showId;
    private final HtmlFileParser parser;
    private final SpellChecker spellChecker;

//    public HtmlEditor(String filePath, SpellChecker spellChecker) {
//        this.spellChecker = spellChecker;
//        this.parser = new JsoupFileParser();
//        this.document = parser.readHtmlFromFile(filePath);
//        this.filePath = filePath;
//        this.history = new CommandHistory();
//        this.showId = true;
//    }

//    public HtmlEditor(SpellChecker spellChecker) {
//        this.spellChecker = spellChecker;
//        document = new HtmlDocument();
//        document.init();
//
//        this.filePath = null;
//        this.history = new CommandHistory();
//        this.showId = true;
//        this.parser = new JsoupFileParser();
//    }
//
//    public HtmlEditor(HtmlDocument document, CommandHistory history, String filePath, Boolean showId, SpellChecker spellChecker) {
//        this.document = document;
//        this.history = history;
//        this.filePath = filePath;
//        this.showId = showId;
//        this.spellChecker = spellChecker;
//        this.parser =  new JsoupFileParser();
//    }
//
    public HtmlEditor(SpellChecker spellChecker, HtmlFileParser fileParser, String filePath, Boolean showId) {
        this.spellChecker = spellChecker;
        this.parser = fileParser;
        this.filePath = filePath;
        this.document = parser.readHtmlFromFile(filePath);
        this.history = new CommandHistory();
        this.showId = showId;
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
        IrrevocableCommand cmd = new SaveFileCommand(document, filePath, parser);
        history.executeCommand(cmd);
        history.resetModified();
    }

    public void save() {
        SaveFileCommand cmd = new SaveFileCommand(document, this.filePath, parser);
        history.executeCommand(cmd);
        history.resetModified();
    }

    public void printTree() {
        IrrevocableCommand cmd = new PrintTreeCommand(document, showId);
        history.executeCommand(cmd);
    }

    public void spellCheck() {
        IrrevocableCommand cmd = new SpellCheckCommand(document, spellChecker);
        history.executeCommand(cmd);
    }
}