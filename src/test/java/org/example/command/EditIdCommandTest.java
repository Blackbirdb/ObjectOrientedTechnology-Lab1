package org.example.command;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class EditIdCommandTest {

    @Mock private HtmlDocument document;
    @Mock private HtmlElement element;

    private EditIdCommand command;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(document.getElementById("old-id")).thenReturn(element);
        command = new EditIdCommand(document, "old-id", "new-id");
    }

    @Test
    void execute_shouldUpdateElementId() {
        when(document.getElementById("new-id")).thenReturn(null);
        command.execute();
        verify(element).setId("new-id");
        verify(document).unregisterElement(element);
        verify(document).registerElement(element);
    }

    @Test
    void execute_shouldThrowExceptionWhenNewIdExists() {
        when(document.getElementById("new-id")).thenReturn(mock(HtmlElement.class));
        assertThrows(IllegalArgumentException.class, command::execute);
    }

    @Test
    void undo_shouldRevertElementId() {
        command.execute();
        when(document.getElementById("new-id")).thenReturn(element);
        when(document.getElementById("old-id")).thenReturn(null);
        command.undo();
        verify(element).setId("old-id");
        verify(document, times(2)).unregisterElement(element);
        verify(document, times(2)).registerElement(element);
    }

    @Test
    void undo_shouldThrowExceptionWhenOldIdExists() {
        when(document.getElementById("new-id")).thenReturn(element);
        when(document.getElementById("old-id")).thenReturn(mock(HtmlElement.class));
        assertThrows(IllegalArgumentException.class, command::undo);
    }
}