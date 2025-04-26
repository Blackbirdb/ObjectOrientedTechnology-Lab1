package org.example.editor;

import lombok.Getter;
import lombok.Setter;
import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.example.document.HtmlTreeVisitor;
import org.example.editor.commands.*;
import org.example.tools.htmlparser.FileParserService;
import org.example.tools.htmlparser.HtmlFileParser;
import org.example.tools.htmlparser.JsoupFileParser;
import org.example.tools.spellchecker.SpellChecker;
import org.example.tools.spellchecker.SpellCheckerService;
import org.example.tools.treeprinter.Visitor;

// invoker
public class HtmlEditor {
    @Getter @Setter private HtmlDocument document;
    private final CommandHistory history;
    @Getter @Setter private String filePath;
    @Getter @Setter private Boolean showId;
    private final FileParserService parser;
    private final SpellCheckerService spellChecker;
    private final HtmlTreeVisitor visitor;

    public HtmlEditor(SpellCheckerService spellChecker, FileParserService fileParser, String filePath,
                      Boolean showId, HtmlTreeVisitor visitor, CommandHistory history, HtmlDocument document) {
        this.spellChecker = spellChecker;
        this.parser = fileParser;
        this.filePath = filePath;
        this.document = document;
        this.history = history;
        this.showId = showId;
        this.visitor = visitor;
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
        IrrevocableCommand cmd = new PrintTreeCommand(document, showId, visitor);
        history.executeCommand(cmd);
    }

    public void spellCheck() {
        IrrevocableCommand cmd = new SpellCheckCommand(document, spellChecker);
        history.executeCommand(cmd);
    }
}