package org.example.document;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HtmlNodeTest {
    private HtmlNode node;
    private HtmlElement parent;
    private HtmlElement sibling;

    @BeforeEach
    void setUp() {
        parent = mock(HtmlElement.class);
        sibling = mock(HtmlElement.class);
        node = new HtmlTextNode("text", parent);
    }

    @Test
    void isLastChild_shouldReturnTrueWhenNoParent() {
        HtmlNode nodeWithoutParent = new HtmlTextNode("text", null);
        assertTrue(nodeWithoutParent.isLastChild());
    }

    @Test
    void isLastChild_shouldReturnTrueWhenLastChild() {
        when(parent.getChildren()).thenReturn(List.of(node));
        assertTrue(node.isLastChild());
    }

    @Test
    void isLastChild_shouldReturnFalseWhenNotLastChild() {
        when(parent.getChildren()).thenReturn(List.of(node, sibling));
        assertFalse(node.isLastChild());
    }

    @Test
    void isLastChild_shouldReturnTrueWhenOnlyChild() {
        when(parent.getChildren()).thenReturn(List.of(node));
        assertTrue(node.isLastChild());
    }

    @Test
    void isLastChild_shouldThrowExceptionWhenParentHasNoChildren() {
        when(parent.getChildren()).thenReturn(List.of());
        assertThrows(IllegalStateException.class, node::isLastChild);
    }
}