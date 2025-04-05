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

class InsertElementCommandTest {

    @Mock private HtmlDocument document;
    @Mock private HtmlElementFactory factory;
    @Mock private HtmlElement parentElement;
    @Mock private HtmlElement referenceElement;
    @Mock private HtmlElement newElement;

    private InsertElementCommand command;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(document.getFactory()).thenReturn(factory);
        when(document.getElementById("reference-id")).thenReturn(referenceElement);
        when(referenceElement.getParent()).thenReturn(parentElement);

        command = new InsertElementCommand(
                document, "div", "new-element-id", "reference-id", "Sample content"
        );
    }

    @Test
    void execute_shouldCreateElementAndInsertBeforeReference() {
        when(factory.createElement("div", "new-element-id", "Sample content", parentElement))
                .thenReturn(newElement);

        command.execute();

        verify(factory).createElement("div", "new-element-id", "Sample content", parentElement);
        verify(parentElement).insertBefore(newElement, referenceElement);
    }

    @Test
    void undo_shouldRemoveElementFromParentAndUnregister() {
        when(document.getElementById("new-element-id")).thenReturn(newElement);
        when(newElement.getParent()).thenReturn(parentElement);

        command.undo();

        verify(parentElement).removeChild(newElement);
        verify(document).unregisterElement(newElement);
    }

    @Test
    void execute_shouldThrowExceptionIfReferenceElementNotFound() {
        when(document.getElementById("reference-id")).thenReturn(null);

        Exception exception = assertThrows(NullPointerException.class, command::execute);
        assertNotNull(exception);
    }

    @Test
    void undo_shouldHandleMissingElement() {
        when(document.getElementById("new-element-id")).thenReturn(null);

        Exception exception = assertThrows(NullPointerException.class, command::undo);
        assertNotNull(exception);
    }

    @Test
    void constructor_shouldThrowExceptionForNullDocument() {
        assertThrows(NullPointerException.class, () ->
                new InsertElementCommand(null, "div", "new-element-id", "reference-id", "Sample content")
        );
    }
}
