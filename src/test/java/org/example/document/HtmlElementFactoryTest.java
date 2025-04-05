package org.example.document;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HtmlElementFactoryTest {

    private HtmlDocument document;
    private HtmlElementFactory factory;
    private HtmlElement parentElement;

    @BeforeEach
    void setUp() {
        document = mock(HtmlDocument.class);
        factory = new HtmlElementFactory(document);
        parentElement = new HtmlElement("div", "parent", null);
    }

    @Test
    void createElement_withTextContent_createsElementWithText() {
        when(document.getElementById("test")).thenReturn(null);

        HtmlElement element = factory.createElement("div", "test", "Sample text", parentElement);

        assertNotNull(element);
        assertEquals("div", element.getTagName());
        assertEquals("test", element.getId());
        assertEquals("Sample text", element.getTextContent());
        verify(document).registerElement(element);
    }

    @Test
    void createElement_withoutTextContent_createsElementWithoutText() {
        when(document.getElementById("child")).thenReturn(null);

        HtmlElement element = factory.createElement("span", "child", parentElement);

        assertNotNull(element);
        assertEquals("span", element.getTagName());
        assertEquals("child", element.getId());
        assertNull(element.getTextContent());
        verify(document).registerElement(element);
    }

    @Test
    void createElement_specialTagWithoutId_usesTagNameAsId() {
        when(document.getElementById("html")).thenReturn(null);

        HtmlElement element = factory.createElement("html", null, parentElement);

        assertEquals("html", element.getId());
    }

    @Test
    void createElement_nonSpecialTagWithoutId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createElement("div", null, parentElement);
        });
    }

    @Test
    void createElement_duplicateId_throwsException() {
        HtmlElement existing = new HtmlElement("div", "duplicate", null);
        when(document.getElementById("duplicate")).thenReturn(existing);

        assertThrows(IllegalArgumentException.class, () -> {
            factory.createElement("span", "duplicate", parentElement);
        });
    }

    @Test
    void createElement_nullParent_allowed() {
        when(document.getElementById("orphan")).thenReturn(null);

        HtmlElement element = factory.createElement("div", "orphan", null);

        assertNull(element.getParent());
    }

    @Test
    void createElement_emptyIdForNonSpecialTag_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createElement("div", "", parentElement);
        });
    }

    @Test
    void createElement_specialTagWithCustomId_usesCustomId() {
        when(document.getElementById("custom-body")).thenReturn(null);

        HtmlElement element = factory.createElement("body", "custom-body", parentElement);

        assertEquals("custom-body", element.getId());
    }

    @Test
    void createElement_textContentNull_createsElementWithoutText() {
        when(document.getElementById("paragraph")).thenReturn(null);

        HtmlElement element = factory.createElement("p", "paragraph", null, parentElement);

        assertNull(element.getTextContent());
    }
}