package org.example.command;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class HtmlEditorTest {

    @Mock private HtmlDocument document;
    @Mock private HtmlElement element;
    @Mock private CommandHistory history;
    @Mock private Command command;

    private HtmlEditor editor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        editor = new HtmlEditor(history, document);
    }

    @Test
    void getElementById_shouldReturnElementWhenIdExists() {
        when(document.getElementById("existing-id")).thenReturn(element);
        assertEquals(element, editor.getElementById("existing-id"));
    }

    @Test
    void getElementById_shouldReturnNullWhenIdDoesNotExist() {
        when(document.getElementById("nonexistent-id")).thenReturn(null);
        assertNull(editor.getElementById("nonexistent-id"));
    }

    @Test
    void insertElement_shouldExecuteInsertElementCommand() {
        editor.insertElement("div", "new-id", "location", "content");
        verify(history).executeCommand(any(InsertElementCommand.class));
    }

    @Test
    void appendElement_shouldExecuteAppendElementCommand() {
        editor.appendElement("div", "new-id", "parent-id", "content");
        verify(history).executeCommand(any(AppendElementCommand.class));
    }

    @Test
    void editId_shouldExecuteEditIdCommand() {
        editor.editId("old-id", "new-id");
        verify(history).executeCommand(any(EditIdCommand.class));
    }

    @Test
    void editText_shouldExecuteEditTextCommand() {
        editor.editText("element-id", "new content");
        verify(history).executeCommand(any(EditTextCommand.class));
    }

    @Test
    void deleteElement_shouldExecuteDeleteElementCommand() {
        editor.deleteElement("element-id");
        verify(history).executeCommand(any(DeleteElementCommand.class));
    }

    @Test
    void undo_shouldCallUndoOnHistory() {
        editor.undo();
        verify(history).undo();
    }

    @Test
    void redo_shouldCallRedoOnHistory() {
        editor.redo();
        verify(history).redo();
    }

    @Test
    void setDocument_shouldUpdateDocument() {
        HtmlDocument newDocument = mock(HtmlDocument.class);
        editor.setDocument(newDocument);
        assertEquals(newDocument, editor.getDocument());
    }
}