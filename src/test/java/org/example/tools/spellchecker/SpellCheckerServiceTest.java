package org.example.tools.spellchecker;

import static org.junit.jupiter.api.Assertions.*;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.languagetool.rules.RuleMatch;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpellCheckerServiceTest {

    @Mock
    private SpellChecker mockSpellChecker;

    @InjectMocks
    private SpellCheckerService spellCheckerService;

    @Mock
    private HtmlDocument mockDocument;

    @Mock
    private HtmlElement mockElement;

    @Test
    void hasErrors_withNullText_shouldReturnFalse() {
        assertFalse(spellCheckerService.hasErrors(null));
    }

    @Test
    void hasErrors_withEmptyText_shouldReturnFalse() {
        assertFalse(spellCheckerService.hasErrors(""));
        assertFalse(spellCheckerService.hasErrors("   "));
    }

    @Test
    void hasErrors_withErrors_shouldReturnTrue() {
        when(mockSpellChecker.checkText("error text"))
                .thenReturn(List.of(mock(RuleMatch.class)));

        assertTrue(spellCheckerService.hasErrors("error text"));
    }

    @Test
    void hasErrors_withoutErrors_shouldReturnFalse() {
        when(mockSpellChecker.checkText("correct text"))
                .thenReturn(Collections.emptyList());

        assertFalse(spellCheckerService.hasErrors("correct text"));
    }

    @Test
    void checkSpelling_withErrors_shouldPrintFormattedReport() {
        // 准备测试数据
        when(mockDocument.getIdToElementMap())
                .thenReturn(Collections.singletonMap("elem1", mockElement));
        when(mockElement.getTextContent()).thenReturn("text with errror");
        when(mockElement.getId()).thenReturn("elem1");

        // 模拟拼写错误
        RuleMatch mockError = mock(RuleMatch.class);
        when(mockError.getMessage()).thenReturn("Possible spelling mistake");
        when(mockError.getFromPos()).thenReturn(5);
        when(mockError.getToPos()).thenReturn(11);
        when(mockSpellChecker.checkText("text with errror"))
                .thenReturn(List.of(mockError));

        // 重定向System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // 执行测试
        spellCheckerService.checkSpelling(mockDocument);

        // 验证输出
        String output = outContent.toString();
        assertTrue(output.contains("Errors found:"));
        assertTrue(output.contains("ElementId: elem1"));
        assertTrue(output.contains("Possible spelling mistake"));
        assertTrue(output.contains("5:11"));

        // 恢复System.out
        System.setOut(System.out);
    }

    @Test
    void checkSpelling_withoutErrors_shouldPrintNoErrors() {
        when(mockDocument.getIdToElementMap())
                .thenReturn(Collections.singletonMap("elem1", mockElement));
        when(mockElement.getTextContent()).thenReturn("correct text");
        when(mockSpellChecker.checkText("correct text"))
                .thenReturn(Collections.emptyList());

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        spellCheckerService.checkSpelling(mockDocument);

        String output = outContent.toString();
        assertTrue(output.contains("No errors found."));

        System.setOut(System.out);
    }

    @Test
    void checkSpelling_withMultipleElements_shouldCheckAll() {
        HtmlElement element1 = mock(HtmlElement.class);
        HtmlElement element2 = mock(HtmlElement.class);

        when(mockDocument.getIdToElementMap())
                .thenReturn(Map.of(
                        "elem1", element1,
                        "elem2", element2
                ));
        when(element1.getTextContent()).thenReturn("text with errror");
        when(element1.getId()).thenReturn("elem1");
        when(element2.getTextContent()).thenReturn("correct text");

        RuleMatch mockError = mock(RuleMatch.class);
        when(mockSpellChecker.checkText("text with errror"))
                .thenReturn(List.of(mockError));
        when(mockSpellChecker.checkText("correct text"))
                .thenReturn(Collections.emptyList());

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        spellCheckerService.checkSpelling(mockDocument);

        String output = outContent.toString();
        assertTrue(output.contains("elem1"));
        assertFalse(output.contains("elem2")); // elem2没有错误不应出现

        System.setOut(System.out);
    }

    @Test
    void checkSpelling_withEmptyDocument_shouldNotFail() {
        when(mockDocument.getIdToElementMap()).thenReturn(Collections.emptyMap());

        assertDoesNotThrow(() -> spellCheckerService.checkSpelling(mockDocument));
    }

    @Test
    void checkSpelling_withNullTextContent_shouldNotFail() {
        when(mockDocument.getIdToElementMap())
                .thenReturn(Collections.singletonMap("elem1", mockElement));
        when(mockElement.getTextContent()).thenReturn(null);

        assertDoesNotThrow(() -> spellCheckerService.checkSpelling(mockDocument));
    }
}