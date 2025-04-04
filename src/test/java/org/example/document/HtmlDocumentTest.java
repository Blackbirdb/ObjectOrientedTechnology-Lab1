package org.example.document;

import org.example.visitor.HtmlVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HtmlDocumentTest {
    private HtmlDocument document;
    private HtmlElement root;
    private HtmlElement element;

    @BeforeEach
    void setUp() {
        root = mock(HtmlElement.class);
        element = mock(HtmlElement.class);
        document = new HtmlDocument(root);
    }

    @Test
    void getElementById_shouldReturnElementWhenIdExists() {
        when(element.getId()).thenReturn("content");
        document.registerElement(element);
        assertEquals(element, document.getElementById("content"));
    }

    @Test
    void getElementById_shouldReturnNullWhenIdDoesNotExist() {
        assertNull(document.getElementById("nonexistent"));
    }

    @Test
    void registerElement_shouldAddElementToMap() {
        when(element.getId()).thenReturn("content");
        document.registerElement(element);
        assertEquals(element, document.getElementById("content"));
    }

    @Test
    void unregisterElement_shouldRemoveElementFromMap() {
        when(element.getId()).thenReturn("content");
        document.registerElement(element);
        document.unregisterElement(element);
        assertNull(document.getElementById("content"));
    }

    @Test
    void accept_shouldCallVisitorOnRoot() {
        HtmlVisitor visitor = mock(HtmlVisitor.class);
        document.accept(visitor);
        verify(root).accept(visitor);
    }

}