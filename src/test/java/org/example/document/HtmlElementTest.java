package org.example.document;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HtmlElementTest {

    private HtmlElement parentElement;
    private HtmlElement element;
    private HtmlElement childElement;
    private HtmlTextNode textNode;

    @BeforeEach
    void setUp() {
        parentElement = new HtmlElement("div", "parent", null);
        element = new HtmlElement("div", "test", parentElement);
        childElement = new HtmlElement("span", "child", element);
        textNode = new HtmlTextNode("Sample text", element);
    }

    @Test
    void constructor_initializesPropertiesCorrectly() {
        assertEquals("div", element.getTagName());
        assertEquals("test", element.getId());
        assertEquals(parentElement, element.getParent());
        assertTrue(element.getChildren().isEmpty());
    }

    @Test
    void toString_returnsCorrectFormatWithoutId() {
        HtmlElement noIdElement = new HtmlElement("p", null, null);
        String result = noIdElement.toString();
        assertTrue(result.startsWith("<p>"));
        assertTrue(result.endsWith("</p>\n"));
    }

    @Test
    void toString_returnsCorrectFormatWithId() {
        String result = element.toString();
        assertTrue(result.contains("id=\"test\""));
    }

    @Test
    void toString_includesChildElements() {
        element.insertAtLast(childElement);
        element.insertAtLast(textNode);

        String result = element.toString();
        assertTrue(result.contains("child: span"));
        assertTrue(result.contains("text: Sample text"));
    }

    @Test
    void accept_passesVisitorToElement() {
        HtmlVisitor visitor = mock(HtmlVisitor.class);
        element.accept(visitor);
        verify(visitor).visit(element);
    }

    @Test
    void getTextContent_returnsTextFromFirstTextNode() {
        element.insertAtLast(textNode);
        assertEquals("Sample text", element.getTextContent());
    }

    @Test
    void getTextContent_returnsNullWhenNoTextNode() {
        assertNull(element.getTextContent());
    }

    @Test
    void setTextContent_updatesExistingTextNode() {
        element.insertAtLast(textNode);
        element.setTextContent("Updated text");
        assertEquals("Updated text", element.getTextContent());
    }

    @Test
    void setTextContent_createsNewTextNodeWhenNoneExists() {
        element.setTextContent("New text");
        assertEquals("New text", element.getTextContent());
    }

    @Test
    void setTextContent_handlesNullValue() {
        element.setTextContent(null);
        assertNull(element.getTextContent());
    }

    @Test
    void insertBefore_addsElementAtCorrectPosition() {
        HtmlElement refElement = new HtmlElement("p", "ref", element);
        HtmlElement newElement = new HtmlElement("a", "new", null);

        element.insertAtLast(refElement);
        element.insertBefore(newElement, refElement);

        assertEquals(0, element.getChildIndex(newElement));
        assertEquals(1, element.getChildIndex(refElement));
        assertEquals(element, newElement.getParent());
    }

    @Test
    void insertAtLast_appendsElement() {
        element.insertAtLast(childElement);
        assertEquals(0, element.getChildIndex(childElement));
        assertEquals(element, childElement.getParent());
    }

    @Test
    void insertAtIndex_addsElementAtSpecifiedPosition() {
        HtmlElement first = new HtmlElement("p", "first", element);
        HtmlElement second = new HtmlElement("p", "second", element);

        element.insertAtLast(first);
        element.insertAtIndex(1, second);

        assertEquals(0, element.getChildIndex(first));
        assertEquals(1, element.getChildIndex(second));
    }

    @Test
    void insertAtIndex_throwsForInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            element.insertAtIndex(-1, childElement);
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            element.insertAtIndex(1, childElement);
        });
    }

    @Test
    void removeChild_removesElementAndResetsParent() {
        element.insertAtLast(childElement);
        element.removeChild(childElement);

        assertTrue(element.getChildren().isEmpty());
        assertNull(childElement.getParent());
    }

    @Test
    void getChildIndex_returnsCorrectIndex() {
        HtmlElement first = new HtmlElement("p", "first", element);
        HtmlElement second = new HtmlElement("p", "second", element);

        element.insertAtLast(first);
        element.insertAtLast(second);

        assertEquals(0, element.getChildIndex(first));
        assertEquals(1, element.getChildIndex(second));
    }

    @Test
    void getChildIndex_throwsForWrongParent() {
        HtmlElement unrelated = new HtmlElement("p", "unrelated", null);
        assertThrows(AssertionError.class, () -> {
            element.getChildIndex(unrelated);
        });
    }

    @Test
    void setId_updatesId() {
        element.setId("new-id");
        assertEquals("new-id", element.getId());
        assertTrue(element.toString().contains("id=\"new-id\""));
    }

    @Test
    void setId_handlesNullValue() {
        element.setId(null);
        assertNull(element.getId());
        assertFalse(element.toString().contains("id="));
    }
}