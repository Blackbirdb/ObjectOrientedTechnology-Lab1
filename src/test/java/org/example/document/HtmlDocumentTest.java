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
        when(rootElement.getTagName()).thenReturn("html");

        childElement = mock(HtmlElement.class);
        when(childElement.getId()).thenReturn("child");
        when(childElement.getTagName()).thenReturn("div");

        document = new HtmlDocument(rootElement);
    }

    @Test
    void constructor_withRootElement_setsRootAndInitializesMaps() {
        assertNotNull(document);
        assertEquals(rootElement, document.getRoot());
        assertNotNull(document.getFactory());
    }

    @Test
    void constructor_withoutRootElement_initializesEmptyDocument() {
        HtmlDocument emptyDoc = new HtmlDocument();
        assertNull(emptyDoc.getRoot());
        assertNotNull(emptyDoc.getFactory());
    }

    @Test
    void getElementById_returnsCorrectElement() {
        document.registerElement(childElement);
        HtmlElement found = document.getElementById("child");
        assertEquals(childElement, found);
    }

    @Test
    void getElementById_returnsNullForNonExistentId() {
        assertNull(document.getElementById("nonexistent"));
    }

    @Test
    void registerElement_addsElementToMap() {
        document.registerElement(childElement);
        assertTrue(document.getElementById("child") != null);
    }

    @Test
    void registerElement_overwritesExistingId() {
        HtmlElement newElement = mock(HtmlElement.class);
        when(newElement.getId()).thenReturn("child");

        document.registerElement(childElement);
        document.registerElement(newElement);

        assertEquals(newElement, document.getElementById("child"));
    }

    @Test
    void unregisterElement_removesElementFromMap() {
        document.registerElement(childElement);
        document.unregisterElement(childElement);
        assertNull(document.getElementById("child"));
    }

    @Test
    void unregisterElement_doesNothingForNonExistentElement() {
        assertDoesNotThrow(() -> document.unregisterElement(childElement));
    }

    @Test
    void isSpecialTag_identifiesSpecialTags() {
        assertTrue(document.isSpecialTag("html"));
        assertTrue(document.isSpecialTag("head"));
        assertTrue(document.isSpecialTag("title"));
        assertTrue(document.isSpecialTag("body"));
    }

    @Test
    void isSpecialTag_returnsFalseForNormalTags() {
        assertFalse(document.isSpecialTag("div"));
        assertFalse(document.isSpecialTag("p"));
        assertFalse(document.isSpecialTag("span"));
        assertFalse(document.isSpecialTag("a"));
    }

    @Test
    void isSpecialTag_isCaseSensitive() {
        assertFalse(document.isSpecialTag("HTML"));
        assertFalse(document.isSpecialTag("Head"));
    }

    @Test
    void getFactory_returnsSameInstance() {
        HtmlElementFactory factory1 = document.getFactory();
        HtmlElementFactory factory2 = document.getFactory();
        assertSame(factory1, factory2);
    }

    @Test
    void setRoot_changesRootElement() {
        HtmlElement newRoot = mock(HtmlElement.class);
        document.setRoot(newRoot);
        assertEquals(newRoot, document.getRoot());
    }
}