package org.example.tools.spellcheck;

import org.example.document.HtmlElement;
import org.example.document.HtmlNode;
import org.example.document.HtmlTextNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.languagetool.rules.RuleMatch;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpellCheckVisitorTest {
    private SpellCheckVisitor spellCheckVisitor;
    private HtmlElement mockElement;
    private HtmlTextNode mockTextNode;

    @BeforeEach
    void setUp() {
        spellCheckVisitor = new SpellCheckVisitor();
        mockElement = mock(HtmlElement.class);
        mockTextNode = mock(HtmlTextNode.class);
    }

    @Test
    void visitHtmlElement_shouldVisitAllChildren() {
        HtmlNode child1 = mock(HtmlTextNode.class);
        HtmlNode child2 = mock(HtmlElement.class);
        when(mockElement.getChildren()).thenReturn(List.of(child1, child2));

        spellCheckVisitor.visit(mockElement);

        verify(child1).accept(spellCheckVisitor);
        verify(child2).accept(spellCheckVisitor);
    }
    @Test
    void visitHtmlTextNode_withErrors_shouldAddToErrorMap() {
        when(mockTextNode.getText()).thenReturn("mispelled");
        when(mockTextNode.getParent()).thenReturn(mockElement);
        when(mockElement.getId()).thenReturn("parent1");

        spellCheckVisitor.visit(mockTextNode);

        Map<String, List<RuleMatch>> errorMap = spellCheckVisitor.getErrorMap();
        assertTrue(errorMap.containsKey("parent1"));
        assertFalse(errorMap.get("parent1").isEmpty());
    }
    @Test
    void visitHtmlTextNode_withoutErrors_shouldNotAddToErrorMap() {
        when(mockTextNode.getText()).thenReturn("Correct");

        spellCheckVisitor.visit(mockTextNode);

        Map<String, List<RuleMatch>> errorMap = spellCheckVisitor.getErrorMap();
        assertTrue(errorMap.isEmpty());
    }
    @Test
    void getErrorMap_shouldReturnUnmodifiableMap() {
        Map<String, List<RuleMatch>> errorMap = spellCheckVisitor.getErrorMap();
        assertThrows(UnsupportedOperationException.class, () -> errorMap.put("key", Collections.emptyList()));
    }
    @Test
    void hasErrors_withErrors_shouldReturnTrue() {
        when(mockTextNode.getText()).thenReturn("mispelled");
        when(mockTextNode.getParent()).thenReturn(mockElement);
        when(mockElement.getId()).thenReturn("parent1");

        spellCheckVisitor.visit(mockTextNode);

        assertTrue(spellCheckVisitor.hasErrors());
    }
    @Test
    void hasErrors_withoutErrors_shouldReturnFalse() {
        when(mockTextNode.getText()).thenReturn("Correct");

        spellCheckVisitor.visit(mockTextNode);

        assertFalse(spellCheckVisitor.hasErrors());
    }
}