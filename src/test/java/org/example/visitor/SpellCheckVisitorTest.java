package org.example.visitor;

import org.example.document.HtmlElement;
import org.example.document.HtmlNode;
import org.example.document.HtmlTextNode;
import org.example.tools.spellcheck.SpellCheckVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.languagetool.rules.RuleMatch;
import org.example.tools.spellcheck.SpellCheckUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpellCheckVisitorTest {
    private SpellCheckVisitor visitor;
    private SpellCheckUtils spellCheckUtils;
    private HtmlElement element;
    private HtmlTextNode textNode;
    private HtmlElement parent;

    @BeforeEach
    void setUp() {
        spellCheckUtils = mock(SpellCheckUtils.class);
        visitor = new SpellCheckVisitor(spellCheckUtils);
        element = mock(HtmlElement.class);
        textNode = mock(HtmlTextNode.class);
        parent = mock(HtmlElement.class);
    }

    @Test
    void visitElement_shouldCheckAllChildren() {
        HtmlNode child1 = mock(HtmlNode.class);
        HtmlNode child2 = mock(HtmlNode.class);
        when(element.getChildren()).thenReturn(List.of(child1, child2));

        visitor.visit(element);

        verify(child1).accept(visitor);
        verify(child2).accept(visitor);
    }

    @Test
    void visitTextNode_shouldAddErrorsToErrorMap() {
        when(textNode.getText()).thenReturn("some text with errors");
        when(spellCheckUtils.hasErrors("some text with errors")).thenReturn(true);
        when(spellCheckUtils.checkText("some text with errors")).thenReturn(List.of(mock(RuleMatch.class), mock(RuleMatch.class)));
        when(textNode.getParent()).thenReturn(parent);
        when(parent.getId()).thenReturn("parentId");

        visitor.visit(textNode);

        Map<String, List<RuleMatch>> errorMap = visitor.getErrorMap();
        assertTrue(errorMap.containsKey("parentId"));
        assertEquals(2, errorMap.get("parentId").size());
    }

    @Test
    void visitTextNode_shouldNotAddErrorsIfNoErrorsFound() {
        when(textNode.getText()).thenReturn("Some correct text");
        when(textNode.getParent()).thenReturn(parent);
        when(parent.getId()).thenReturn("parentId");
        when(spellCheckUtils.hasErrors("Some correct text")).thenReturn(false);

        visitor.visit(textNode);

        Map<String, List<RuleMatch>> errorMap = visitor.getErrorMap();
        assertFalse(errorMap.containsKey("parentId"));
    }

    @Test
    void hasErrors_shouldReturnTrueIfErrorsExist() {
        when(textNode.getText()).thenReturn("some text with errors");
        when(spellCheckUtils.hasErrors("some text with errors")).thenReturn(true);
        HtmlElement parent = mock(HtmlElement.class);
        when(textNode.getParent()).thenReturn(parent);
        when(parent.getId()).thenReturn("parentId");

        visitor.visit(textNode);

        assertTrue(visitor.hasErrors());
    }

    @Test
    void hasErrors_shouldReturnFalseIfNoErrorsExist() {
        assertFalse(visitor.hasErrors());
    }

    @Test
    void getErrorMap_shouldReturnUnmodifiableMap() {
        Map<String, List<RuleMatch>> errorMap = visitor.getErrorMap();
        assertThrows(UnsupportedOperationException.class, () -> errorMap.put("key", Collections.emptyList()));
    }
}
