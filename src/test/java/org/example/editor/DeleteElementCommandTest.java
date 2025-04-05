package org.example.editor;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DeleteElementCommandTest {

    @Mock private HtmlDocument document;
    @Mock private HtmlElement element;
    @Mock private HtmlElement parent;

    private DeleteElementCommand command;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(document.getElementById("element-id")).thenReturn(element);
        when(element.getParent()).thenReturn(parent);
        when(parent.getChildIndex(element)).thenReturn(0);
        command = new DeleteElementCommand(document, "element-id");
    }

    @Test
    void execute_shouldRemoveElementFromParent() {
        command.execute();
        verify(parent).removeChild(element);
        verify(document).unregisterElement(element);
    }

    @Test
    void execute_shouldThrowExceptionWhenElementDoesNotExist() {
        when(document.getElementById("nonexistent-id")).thenReturn(null);
        DeleteElementCommand invalidCommand = new DeleteElementCommand(document, "nonexistent-id");
        assertThrows(IllegalArgumentException.class, invalidCommand::execute);
    }

    @Test
    void undo_shouldReinsertElementAtOriginalIndex() {
        command.execute();
        command.undo();
        verify(parent).insertAtIndex(0, element);
    }

    @Test
    void undo_shouldThrowExceptionWhenParentIsNull() {
        DeleteElementCommand invalidCommand = new DeleteElementCommand(document, "element-id");
        assertThrows(IllegalStateException.class, invalidCommand::undo);
    }
}