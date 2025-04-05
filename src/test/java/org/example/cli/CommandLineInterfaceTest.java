package org.example.cli;

import org.example.editor.HtmlEditor;
import org.example.document.HtmlDocument;
import org.example.service.HtmlFileParser;
import org.example.service.HtmlTreePrinter;
import org.example.service.SpellChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.*;

class CommandLineInterfaceTest {

    @Mock private HtmlEditor editor;
    @Mock private HtmlFileParser reader;
    @Mock private HtmlTreePrinter treePrinter;
    @Mock private SpellChecker spellChecker;

    private CommandLineInterface cli;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cli = new CommandLineInterface();
        when(editor.getDocument())
                .thenReturn(mock(HtmlDocument.class));
    }

    @Test
    void insertCommand_shouldInsertElement() throws IOException {
        cli.processCommand("insert div new-id location content");
        verify(editor).insertElement("div", "new-id", "location", "content");
    }

    @Test
    void insertCommand_shouldPrintWrongUsageWhenArgumentsAreMissing() throws IOException {
        cli.processCommand("insert div new-id");
        verify(editor, never()).insertElement(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void appendCommand_shouldAppendElement() throws IOException {
        cli.processCommand("append div new-id parent content");
        verify(editor).appendElement("div", "new-id", "parent", "content");
    }

    @Test
    void appendCommand_shouldPrintWrongUsageWhenArgumentsAreMissing() throws IOException {
        cli.processCommand("append div new-id");
        verify(editor, never()).appendElement(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void editIdCommand_shouldEditElementId() throws IOException {
        cli.processCommand("edit-id old-id new-id");
        verify(editor).editId("old-id", "new-id");
    }

    @Test
    void editIdCommand_shouldPrintWrongUsageWhenArgumentsAreMissing() throws IOException {
        cli.processCommand("edit-id old-id");
        verify(editor, never()).editId(anyString(), anyString());
    }

    @Test
    void editTextCommand_shouldEditElementText() throws IOException {
        cli.processCommand("edit-text element new text");
        verify(editor).editText("element", "new text");
    }

    @Test
    void editTextCommand_shouldPrintWrongUsageWhenArgumentsAreMissing() throws IOException {
        cli.processCommand("edit-text");
        verify(editor, never()).editText(anyString(), anyString());
    }

    @Test
    void deleteCommand_shouldDeleteElement() throws IOException {
        cli.processCommand("delete element-id");
        verify(editor).deleteElement("element-id");
    }

    @Test
    void deleteCommand_shouldPrintWrongUsageWhenArgumentsAreMissing() throws IOException {
        cli.processCommand("delete");
        verify(editor, never()).deleteElement(anyString());
    }

    @Test
    void readCommand_shouldReadHtmlFromFile() throws IOException {
        cli.processCommand("read filePath");
        verify(reader).readHtmlFromFile("filePath");
    }

    @Test
    void readCommand_shouldPrintWrongUsageWhenArgumentsAreMissing() throws IOException {
        cli.processCommand("read");
        verify(reader, never()).readHtmlFromFile(anyString());
    }

    @Test
    void saveCommand_shouldSaveHtmlToFile() throws IOException {
        cli.processCommand("save filePath");
        verify(reader).saveHtmlDocumentToFile(any(), eq("filePath"));
    }

    @Test
    void saveCommand_shouldPrintWrongUsageWhenArgumentsAreMissing() throws IOException {
        cli.processCommand("save");
        verify(reader, never()).saveHtmlDocumentToFile(any(), anyString());
    }

    @Test
    void undoCommand_shouldUndoLastCommand() throws IOException {
        cli.processCommand("undo");
        verify(editor).undo();
    }

    @Test
    void redoCommand_shouldRedoLastUndoneCommand() throws IOException {
        cli.processCommand("redo");
        verify(editor).redo();
    }
}