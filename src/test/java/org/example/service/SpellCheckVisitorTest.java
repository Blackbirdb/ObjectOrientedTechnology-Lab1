package org.example.service;

import org.example.document.HtmlElement;
import org.example.document.HtmlNode;
import org.example.document.HtmlTextNode;
import org.example.visitor.SpellCheckUtils;
import org.example.visitor.SpellCheckVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpellCheckVisitorTest {
    private SpellCheckVisitor visitor = new SpellCheckVisitor();

    @Test
    void visitHtmlElement_shouldCheckAllChildren() {
        HtmlElement element = mock(HtmlElement.class);
        HtmlNode child1 = mock(HtmlNode.class);
        HtmlNode child2 = mock(HtmlNode.class);
        when(element.getChildren()).thenReturn(List.of(child1, child2));

        visitor.visit(element);

        verify(child1).accept(visitor);
        verify(child2).accept(visitor);
    }

    @Test
    void visitHtmlTextNode_shouldAddErrorsToMap() {
        HtmlTextNode textNode = mock(HtmlTextNode.class);
        when(textNode.getText()).thenReturn("hello world");

        visitor.visit(textNode);

        Map<HtmlNode, List<String>> errorMap = visitor.getErrorMap();
        assertTrue(errorMap.containsKey(textNode));
        assertEquals(List.of("Hello"), errorMap.get(textNode));
    }

    @Test
    void visitHtmlTextNode_shouldNotAddErrorsToMapIfNoErrors() {
        HtmlTextNode textNode = mock(HtmlTextNode.class);
        when(textNode.getText()).thenReturn("Correct text");

        visitor.visit(textNode);

        Map<HtmlNode, List<String>> errorMap = visitor.getErrorMap();
        assertFalse(errorMap.containsKey(textNode));
    }

}
