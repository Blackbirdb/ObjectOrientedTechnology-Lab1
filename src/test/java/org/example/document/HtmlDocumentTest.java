package org.example.document;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HtmlDocumentTest {

    private HtmlDocument document;
    private HtmlElement rootElement;
    private HtmlElement childElement;

    @BeforeEach
    void setUp() {
        rootElement = mock(HtmlElement.class);
        when(rootElement.getId()).thenReturn("root");
        when(rootElement.getTagName()).thenReturn("parent");

        childElement = mock(HtmlElement.class);
        when(childElement.getId()).thenReturn("child");
        when(childElement.getTagName()).thenReturn("div");

        document = new HtmlDocument(rootElement);
    }

    @Test
    void appendElement_successfullyAppendsElement() {
        document.registerElement(rootElement);
        when(rootElement.getTagName()).thenReturn("body");

        document.appendElement("div", "child", "root", "Sample Text");

        HtmlElement appendedElement = document.getElementById("child");
        assertNotNull(appendedElement);
        assertEquals("child", appendedElement.getId());
    }

    @Test
    void appendElement_throwsWhenParentNotFound() {
        assertThrows(IllegalArgumentException.class, () ->
                document.appendElement("div", "child", "nonexistent", "Sample Text"));
    }

    @Test
    void insertElement_successfullyInsertsElement() {
        document.registerElement(rootElement);
        when(rootElement.getTagName()).thenReturn("body");

        document.insertElement("div", "child", "root", "Sample Text", 0);

        HtmlElement insertedElement = document.getElementById("child");
        assertNotNull(insertedElement);
        assertEquals("child", insertedElement.getId());
    }

    @Test
    void insertElement_throwsWhenParentNotFound() {
        assertThrows(IllegalArgumentException.class, () ->
                document.insertElement("div", "child", "nonexistent", "Sample Text", 0));
    }

    @Test
    void removeElement_successfullyRemovesElement() {
        document.registerElement(childElement);
        when(rootElement.getTagName()).thenReturn("body");
        when(childElement.getParent()).thenReturn(rootElement);

        document.removeElement("child");

        assertNull(document.getElementById("child"));
    }

    @Test
    void removeElement_throwsWhenElementNotFound() {
        assertThrows(IllegalArgumentException.class, () ->
                document.removeElement("nonexistent"));
    }

    @Test
    void editId_throwsWhenOldIdNotFound() {
        assertThrows(IllegalArgumentException.class, () ->
                document.editId("nonexistent", "newId"));
    }

    @Test
    void editId_throwsWhenNewIdAlreadyExists() {
        HtmlElement anotherElement = mock(HtmlElement.class);
        when(anotherElement.getId()).thenReturn("newId");

        document.registerElement(childElement);
        document.registerElement(anotherElement);

        assertThrows(IllegalArgumentException.class, () ->
                document.editId("child", "newId"));
    }

    @Test
    void editText_successfullyEditsText() {
        document.registerElement(childElement);
        when(childElement.getTextContent()).thenReturn("Old Text");

        String oldText = document.editText("child", "New Text");

        assertEquals("Old Text", oldText);
        verify(childElement).setTextContent("New Text");
    }

    @Test
    void editText_throwsWhenElementNotFound() {
        assertThrows(IllegalArgumentException.class, () ->
                document.editText("nonexistent", "New Text"));
    }
}