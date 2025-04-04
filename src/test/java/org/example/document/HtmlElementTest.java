package org.example.document;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HtmlElementTest {
    private HtmlElement element;
    private HtmlElement parent;
    private HtmlTextNode textNode;
    private HtmlElement child;

    @BeforeEach
    void setUp() {
        parent = mock(HtmlElement.class);
        element = new HtmlElement("div", "content", parent);
        child = new HtmlElement("span", "child", element);
        textNode = new HtmlTextNode("initial text", element);
    }

    @Test
    void toString_shouldReturnCorrectHtmlString() {
        element.insertAtLast(textNode);
        String expected = "<div id=\"content\">\n" +
                            "text: initial text\n" +
                            "</div>\n";
        assertEquals(expected, element.toString());
    }

    @Test
    void getTextContent_shouldReturnTextContentOfFirstChild() {
        element.insertAtLast(textNode);
        assertEquals("initial text", element.getTextContent());
    }

    @Test
    void getTextContent_shouldReturnNullWhenNoTextChild() {
        assertNull(element.getTextContent());
    }

    @Test
    void setTextContent_shouldUpdateTextContentOfFirstChild() {
        element.insertAtLast(textNode);
        element.setTextContent("new text");
        assertEquals("new text", element.getTextContent());
    }

    @Test
    void setTextContent_shouldAddTextNodeWhenNoChildren() {
        element.setTextContent("new text");
        assertEquals("new text", element.getTextContent());
    }

    @Test
    void insertBefore_shouldInsertChildBeforeReferenceChild() {
        HtmlElement newChild = new HtmlElement("span", "newChild", element);
        element.insertAtLast(child);
        element.insertBefore(newChild, child);
        assertEquals(0, element.getChildIndex(newChild));
    }

    @Test
    void insertAtLast_shouldAddChildAtEnd() {
        HtmlElement newChild = new HtmlElement("span", "newChild", element);
        element.insertAtLast(newChild);
        assertEquals(0, element.getChildIndex(newChild));
    }

    @Test
    void insertAtIndex_shouldInsertChildAtSpecifiedIndex() {
        HtmlElement newChild = new HtmlElement("span", "newChild", element);
        element.insertAtIndex(0, newChild);
        assertEquals(0, element.getChildIndex(newChild));
    }

    @Test
    void insertAtIndex_shouldThrowExceptionForInvalidIndex() {
        HtmlElement newChild = new HtmlElement("span", "newChild", element);
        assertThrows(IndexOutOfBoundsException.class, () -> element.insertAtIndex(1, newChild));
    }

    @Test
    void removeChild_shouldRemoveSpecifiedChild() {
        element.insertAtLast(textNode);
        element.removeChild(textNode);
        assertNull(element.getTextContent());
    }
}