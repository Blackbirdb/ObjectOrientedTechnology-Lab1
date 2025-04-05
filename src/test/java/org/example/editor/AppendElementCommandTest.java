package org.example.editor;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.example.document.HtmlElementFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AppendElementCommandTest {

    @Mock private HtmlDocument document;
    @Mock private HtmlElement parent;
    @Mock private HtmlElement element;
    @Mock private HtmlElementFactory factory;

    private AppendElementCommand command;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(document.getElementById("parent-id")).thenReturn(parent);
        when(document.getFactory()).thenReturn(factory);
        when(factory.createElement("div", "new-id", "content", parent)).thenReturn(element);
        command = new AppendElementCommand(document, "div", "new-id", "parent-id", "content");
    }

    @Test
    void execute_shouldAppendElementToParent() {
        command.execute();
        verify(parent).insertAtLast(element);
    }

    @Test
    void execute_shouldThrowExceptionWhenParentDoesNotExist() {
        when(document.getElementById("nonexistent-parent")).thenReturn(null);
        AppendElementCommand invalidCommand = new AppendElementCommand(document, "div", "new-id", "nonexistent-parent", "content");
        assertThrows(IllegalArgumentException.class, invalidCommand::execute);
    }

    @Test
    void undo_shouldRemoveElementFromParent() {
        when(document.getElementById("new-id")).thenReturn(element);
        command.undo();
        verify(parent).removeChild(element);
        verify(document).unregisterElement(element);
    }

    @Test
    void undo_shouldThrowExceptionWhenElementDoesNotExist() {
        when(document.getElementById("nonexistent-id")).thenReturn(null);
        AppendElementCommand invalidCommand = new AppendElementCommand(document, "div", "nonexistent-id", "parent-id", "content");
        assertThrows(IllegalArgumentException.class, invalidCommand::undo);
    }
}