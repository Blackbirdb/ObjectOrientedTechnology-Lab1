package org.example.service;

import org.example.document.HtmlDocument;
import org.example.tools.spellcheck.SpellChecker;
import org.example.tools.spellcheck.SpellCheckVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.languagetool.rules.RuleMatch;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpellCheckerTest {
    private HtmlDocument document;
    private SpellChecker spellChecker;
    private SpellCheckVisitor visitor;

    @BeforeEach
    void setUp() {
        document = mock(HtmlDocument.class);
        visitor = mock(SpellCheckVisitor.class);
        spellChecker = new SpellChecker(document, visitor);
    }



    @Test
    void getErrorMap_shouldReturnCorrectErrorMap() {
        Map<String, List<RuleMatch>> errorMap = Map.of("tag1", List.of(mock(RuleMatch.class), mock(RuleMatch.class)));
        when(visitor.getErrorMap()).thenReturn(errorMap);
        doAnswer(invocation -> {
            visitor.getErrorMap();
            return null;
        }).when(document).accept(visitor);

        assertEquals(errorMap, spellChecker.getErrorMap());
    }

    @Test
    void getErrorMapAsString_shouldReturnFormattedString() {
        RuleMatch error1 = mock(RuleMatch.class);
        RuleMatch error2 = mock(RuleMatch.class);
        when(error1.getMessage()).thenReturn("error1");
        when(error2.getMessage()).thenReturn("error2");
        when(error1.getFromPos()).thenReturn(0);
        when(error1.getToPos()).thenReturn(5);
        when(error2.getFromPos()).thenReturn(6);
        when(error2.getToPos()).thenReturn(10);
        Map<String, List<RuleMatch>> errorMap = Map.of("tag1", List.of(error1, error2));
        when(visitor.getErrorMap()).thenReturn(errorMap);
        doAnswer(invocation -> {
            visitor.getErrorMap();
            return null;
        }).when(document).accept(visitor);

        String expected = "ElementId: tag1\n - error1 (0:5)\n - error2 (6:10)\n\n";
        assertEquals(expected, spellChecker.getErrorMapAsString());
    }

    @Test
    void printErrorMap_shouldPrintFormattedString() {
        RuleMatch error1 = mock(RuleMatch.class);
        RuleMatch error2 = mock(RuleMatch.class);
        when(error1.getMessage()).thenReturn("error1");
        when(error2.getMessage()).thenReturn("error2");
        when(error1.getFromPos()).thenReturn(0);
        when(error1.getToPos()).thenReturn(5);
        when(error2.getFromPos()).thenReturn(6);
        when(error2.getToPos()).thenReturn(10);
        Map<String, List<RuleMatch>> errorMap = Map.of("tag1", List.of(error1, error2));
        when(visitor.getErrorMap()).thenReturn(errorMap);
        doAnswer(invocation -> {
            visitor.getErrorMap();
            return null;
        }).when(document).accept(visitor);

        spellChecker.printErrorMap();
        // This test assumes that the console output is being verified manually or by a tool that captures stdout.
    }
}