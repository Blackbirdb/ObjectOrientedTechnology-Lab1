package org.example.tools.spellcheck;

import org.example.document.HtmlElement;
import org.example.document.HtmlTextNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.languagetool.rules.RuleMatch;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpellCheckerTest {
    private SpellChecker spellChecker;
    private SpellCheckVisitor mockVisitor;
    private HtmlElement mockElement;

    @BeforeEach
    void setUp() {
        mockVisitor = mock(SpellCheckVisitor.class);
        spellChecker = new SpellChecker(mockVisitor);
        mockElement = mock(HtmlElement.class);
    }

    @Test
    void getErrorMap_shouldReturnErrorMap() {
        Map<String, List<RuleMatch>> expectedErrorMap = Map.of("element1", List.of(mock(RuleMatch.class)));
        when(mockVisitor.getErrorMap()).thenReturn(expectedErrorMap);

        Map<String, List<RuleMatch>> errorMap = spellChecker.getErrorMap(mockElement);

        assertEquals(expectedErrorMap, errorMap);
    }

    @Test
    void getErrorMapAsString_shouldReturnFormattedString() {
        RuleMatch mockRuleMatch = mock(RuleMatch.class);
        when(mockRuleMatch.getMessage()).thenReturn("Spelling error");
        when(mockRuleMatch.getFromPos()).thenReturn(0);
        when(mockRuleMatch.getToPos()).thenReturn(5);
        Map<String, List<RuleMatch>> errorMap = Map.of("element1", List.of(mockRuleMatch));
        when(mockVisitor.getErrorMap()).thenReturn(errorMap);

        String expectedString = "ElementId: element1\n - Spelling error (0:5)\n\n";
        String resultString = spellChecker.getErrorMapAsString(mockElement);

        assertEquals(expectedString, resultString);
    }

    @Test
    void printErrorMap_shouldPrintErrors() {
        RuleMatch mockRuleMatch = mock(RuleMatch.class);
        when(mockRuleMatch.getMessage()).thenReturn("Spelling error");
        when(mockRuleMatch.getFromPos()).thenReturn(0);
        when(mockRuleMatch.getToPos()).thenReturn(5);
        Map<String, List<RuleMatch>> errorMap = Map.of("element1", List.of(mockRuleMatch));
        when(mockVisitor.getErrorMap()).thenReturn(errorMap);

        spellChecker.printErrorMap(mockElement);

        verify(mockVisitor).getErrorMap();
    }

    @Test
    void printErrorMap_shouldPrintNoErrorsMessage() {
        when(mockVisitor.getErrorMap()).thenReturn(Collections.emptyMap());

        spellChecker.printErrorMap(mockElement);

        verify(mockVisitor).getErrorMap();
    }
}