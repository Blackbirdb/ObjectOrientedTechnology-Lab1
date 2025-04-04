package org.example.document;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HtmlElementFactoryTest {
    private HtmlElementFactory factory;
    private HtmlDocument document;

    @BeforeEach
    void setUp() {
        document = mock(HtmlDocument.class);
        factory = new HtmlElementFactory(document);
    }

    @Test
    void createElement_shouldCreateElementWithTextContent() {
        HtmlElement parent = mock(HtmlElement.class);
        HtmlElement element = factory.createElement("div", "content", "Text", parent);

        assertNotNull(element);
        assertEquals("div", element.getTagName());
        assertEquals("content", element.getId());
        assertEquals("Text", element.getTextContent());
        assertEquals(parent, element.getParent());
    }

    @Test
    void createElement_shouldThrowExceptionForDuplicateId() {
        HtmlElement existingElement = mock(HtmlElement.class);
        when(document.getElementById("content")).thenReturn(existingElement);

        assertThrows(IllegalArgumentException.class, () -> factory.createElement("div", "content", null));
    }

    @Test
    void createElement_shouldThrowExceptionForMissingIdOnNonSpecialTag() {
        assertThrows(IllegalArgumentException.class, () -> factory.createElement("div", "", null));
    }

    @Test
    void createElement_shouldCreateElementWithIdForSpecialTag() {
        HtmlElement element = factory.createElement("html", "", null);

        assertNotNull(element);
        assertEquals("html", element.getTagName());
        assertEquals("html", element.getId());
    }

    @Test
    void createElement_shouldRegisterElementInDocument() {
        HtmlElement element = factory.createElement("div", "content", null);

        verify(document).registerElement(element);
    }

    @Test
    void createElement_shouldCreateElementWithoutTextContent() {
        HtmlElement parent = mock(HtmlElement.class);
        HtmlElement element = factory.createElement("div", "content", parent);

        assertNotNull(element);
        assertEquals("div", element.getTagName());
        assertEquals("content", element.getId());
        assertNull(element.getTextContent());
        assertEquals(parent, element.getParent());
    }
}