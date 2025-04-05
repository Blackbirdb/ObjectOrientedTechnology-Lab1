package org.example.document;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HtmlTextNodeTest {
    private HtmlTextNode textNode;
    private HtmlElement parent;

    @BeforeEach
    void setUp() {
        parent = mock(HtmlElement.class);
        textNode = new HtmlTextNode("initial text", parent);
    }

    @Test
    void getText_shouldReturnText() {
        assertEquals("initial text", textNode.getText());
    }

    @Test
    void setText_shouldUpdateText() {
        textNode.setText("new text");
        assertEquals("new text", textNode.getText());
    }

    @Test
    void getParent_shouldReturnParent() {
        assertEquals(parent, textNode.getParent());
    }

    @Test
    void setParent_shouldUpdateParent() {
        HtmlElement newParent = mock(HtmlElement.class);
        textNode.setParent(newParent);
        assertEquals(newParent, textNode.getParent());
    }

    @Test
    void accept_shouldCallVisitorVisit() {
        HtmlVisitor visitor = mock(HtmlVisitor.class);
        textNode.accept(visitor);
        verify(visitor).visit(textNode);
    }
}