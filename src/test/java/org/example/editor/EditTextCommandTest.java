package org.example.editor;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.example.editor.commands.EditTextCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class EditTextCommandTest {

    @Mock private HtmlDocument document;
    @Mock private HtmlElement element;

    private EditTextCommand command;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(document.getElementById("element-id")).thenReturn(element);
        command = new EditTextCommand(document, "element-id", "new text");
    }

    @Test
    void execute_shouldUpdateTextContent() {
        when(element.getTextContent()).thenReturn("old text");
        command.execute();
        verify(element).setTextContent("new text");
    }

    @Test
    void execute_shouldThrowExceptionWhenElementDoesNotExist() {
        when(document.getElementById("nonexistent-id")).thenReturn(null);
        EditTextCommand invalidCommand = new EditTextCommand(document, "nonexistent-id", "new text");
        assertThrows(IllegalArgumentException.class, invalidCommand::execute);
    }

    @Test
    void undo_shouldRevertTextContent() {
        when(element.getTextContent()).thenReturn("old text");
        command.execute();
        command.undo();
        verify(element).setTextContent("old text");
    }

    @Test
    void undo_shouldThrowExceptionWhenElementDoesNotExist() {
        when(document.getElementById("nonexistent-id")).thenReturn(null);
        EditTextCommand invalidCommand = new EditTextCommand(document, "nonexistent-id", "new text");
        assertThrows(IllegalArgumentException.class, invalidCommand::undo);
    }
}