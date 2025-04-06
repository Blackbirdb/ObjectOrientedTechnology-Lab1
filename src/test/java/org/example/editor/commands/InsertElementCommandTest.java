package org.example.editor.commands;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.example.document.HtmlElementFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InsertElementCommandTest {
    private HtmlDocument document;
    private HtmlElement parentElement;
    private HtmlElement insertLocationElement;
    private InsertElementCommand command;
    private HtmlElementFactory factory = mock(HtmlElementFactory.class);


    @BeforeEach
    void setUp() {
        document = mock(HtmlDocument.class);
        parentElement = mock(HtmlElement.class);
        insertLocationElement = mock(HtmlElement.class);

        when(document.getElementById("insertLocation")).thenReturn(insertLocationElement);
        when(insertLocationElement.getParent()).thenReturn(parentElement);
        when(parentElement.getTagName()).thenReturn("div");
        when(document.getFactory()).thenReturn(factory);


        command = new InsertElementCommand(document, "div", "newElement", "insertLocation", "textContent");
    }

    @Test
    void executeInsertsElementSuccessfully() {
        HtmlElement newElement = mock(HtmlElement.class);
        when(document.getFactory().createElement("div", "newElement", "textContent", parentElement)).thenReturn(newElement);

        command.execute();

        verify(parentElement).insertBefore(newElement, insertLocationElement);
    }

    @Test
    void executeThrowsExceptionForSpecialTag() {
        command = new InsertElementCommand(document, "title", "newElement", "insertLocation", "textContent");

        assertThrows(IllegalArgumentException.class, command::execute);
    }

    @Test
    void executeThrowsExceptionForNonExistentInsertLocation() {
        when(document.getElementById("insertLocation")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, command::execute);
    }

    @Test
    void executeThrowsExceptionForNullParent() {
        when(insertLocationElement.getParent()).thenReturn(null);

        assertThrows(NullPointerException.class, command::execute);
    }

    @Test
    void executeThrowsExceptionForHtmlParent() {
        when(parentElement.getTagName()).thenReturn("html");

        assertThrows(IllegalArgumentException.class, command::execute);
    }

    @Test
    void undoRemovesElementSuccessfully() {
        HtmlElement elementToRemove = mock(HtmlElement.class);
        when(document.getElementById("newElement")).thenReturn(elementToRemove);
        when(elementToRemove.getParent()).thenReturn(parentElement);

        command.undo();

        verify(parentElement).removeChild(elementToRemove);
        verify(document).unregisterElement(elementToRemove);
    }

    @Test
    void undoThrowsExceptionForNonExistentElement() {
        when(document.getElementById("newElement")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, command::undo);
    }

    @Test
    void undoThrowsExceptionForSpecialTag() {
        command = new InsertElementCommand(document, "special", "newElement", "insertLocation", "textContent");

        assertThrows(IllegalArgumentException.class, command::undo);
    }

    @Test
    void undoThrowsExceptionForNullParent() {
        HtmlElement elementToRemove = mock(HtmlElement.class);
        when(document.getElementById("newElement")).thenReturn(elementToRemove);
        when(elementToRemove.getParent()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, command::undo);
    }

    @Test
    void undoThrowsExceptionForHtmlParent() {
        HtmlElement elementToRemove = mock(HtmlElement.class);
        when(document.getElementById("newElement")).thenReturn(elementToRemove);
        when(elementToRemove.getParent()).thenReturn(parentElement);
        when(parentElement.getTagName()).thenReturn("html");

        assertThrows(IllegalArgumentException.class, command::undo);
    }
}