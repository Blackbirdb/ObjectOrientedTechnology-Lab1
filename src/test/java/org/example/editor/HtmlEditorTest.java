package org.example.editor;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.example.document.HtmlTreeVisitor;
import org.example.editor.commands.*;
import org.example.tools.htmlparser.FileParserService;
import org.example.tools.htmlparser.HtmlFileParser;
import org.example.tools.spellchecker.SpellChecker;
import org.example.tools.spellchecker.SpellCheckerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HtmlEditorTest {

    private HtmlEditor editor;
    private HtmlDocument mockDocument;
    private CommandHistory mockHistory;
    private HtmlElement mockElement;
    private static final String TEST_FILE_PATH = "test.html";

    @BeforeEach
    void setUp() {
        mockDocument = mock(HtmlDocument.class);
        mockHistory = mock(CommandHistory.class);
        mockElement = mock(HtmlElement.class);
        SpellCheckerService mockSpellChecker = mock(SpellCheckerService.class);
        FileParserService mockFileParser = mock(FileParserService.class);
        HtmlTreeVisitor mockVisitor = mock(HtmlTreeVisitor.class);

        editor = new HtmlEditor(mockSpellChecker, mockFileParser, TEST_FILE_PATH,
                true, mockVisitor, mockHistory, mockDocument);
    }

    @Test
    void constructor_initializesFieldsCorrectly() {
        assertEquals(mockDocument, editor.getDocument());
        assertEquals(TEST_FILE_PATH, editor.getFilePath());
        assertTrue(editor.getShowId());
    }

    @Test
    void getElementById_delegatesToDocument() {
        when(mockDocument.getElementById("testId")).thenReturn(mockElement);
        HtmlElement result = editor.getElementById("testId");
        assertEquals(mockElement, result);
    }

    @Test
    void isModified_delegatesToHistory() {
        when(mockHistory.isModified()).thenReturn(true);
        assertTrue(editor.isModified());
    }

    @Test
    void insertElement_executesInsertCommand() {
        editor.insertElement("div", "id1", "after", "text");
        verify(mockHistory).executeCommand((Command) argThat(cmd ->
                cmd instanceof InsertElementCommand));
    }

    @Test
    void appendElement_executesAppendCommand() {
        editor.appendElement("span", "id2", "parent", "content");
        verify(mockHistory).executeCommand((Command) argThat(cmd ->
                cmd instanceof AppendElementCommand));
    }

    @Test
    void editId_executesEditIdCommand() {
        editor.editId("old", "new");
        verify(mockHistory).executeCommand((Command) argThat(cmd ->
                cmd instanceof EditIdCommand));
    }

    @Test
    void editText_executesEditTextCommand() {
        editor.editText("element", "new text");
        verify(mockHistory).executeCommand((Command) argThat(cmd ->
                cmd instanceof EditTextCommand));
    }

    @Test
    void deleteElement_executesDeleteCommand() {
        editor.deleteElement("toDelete");
        verify(mockHistory).executeCommand((Command) argThat(cmd ->
                cmd instanceof DeleteElementCommand));
    }

    @Test
    void undo_delegatesToHistory() {
        editor.undo();
        verify(mockHistory).undo();
    }

    @Test
    void redo_delegatesToHistory() {
        editor.redo();
        verify(mockHistory).redo();
    }

    @Test
    void saveToFile_executesSaveCommand() {
        editor.saveToFile("newpath.html");
        verify(mockHistory).executeCommand((IrrevocableCommand) argThat(cmd ->
                cmd instanceof SaveFileCommand));
        verify(mockHistory).resetModified();
    }

    @Test
    void save_executesSaveCommandWithOriginalPath() {
        editor.save();
        verify(mockHistory).executeCommand((IrrevocableCommand) argThat(cmd ->
                cmd instanceof SaveFileCommand));
        verify(mockHistory).resetModified();
    }

    @Test
    void printTree_executesPrintCommand() {
        editor.setShowId(false);
        editor.printTree();
        verify(mockHistory).executeCommand((IrrevocableCommand) argThat(cmd ->
                cmd instanceof PrintTreeCommand));
    }

    @Test
    void spellCheck_executesSpellCheckCommand() {
        editor.spellCheck();
        verify(mockHistory).executeCommand((IrrevocableCommand) argThat(cmd ->
                cmd instanceof SpellCheckCommand));
    }

    @Test
    void showId_getterAndSetter_workCorrectly() {
        editor.setShowId(false);
        assertFalse(editor.getShowId());

        editor.setShowId(true);
        assertTrue(editor.getShowId());
    }

    @Test
    void document_getterAndSetter_workCorrectly() {
        HtmlDocument newDoc = mock(HtmlDocument.class);
        editor.setDocument(newDoc);
        assertEquals(newDoc, editor.getDocument());
    }

    @Test
    void filePath_isImmutable() {
        assertEquals(TEST_FILE_PATH, editor.getFilePath());
    }

}