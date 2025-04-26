package org.example.document;

import static org.junit.jupiter.api.Assertions.*;

import org.example.tools.spellchecker.SpellChecker;
import org.example.tools.spellchecker.SpellCheckerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HtmlTreeVisitorTest {

    @Mock
    private SpellCheckerService spellChecker;

    private HtmlTreeVisitor visitor;

    @BeforeEach
    void setUp() {
        visitor = new HtmlTreeVisitor(spellChecker);
    }

    @Test
    void visitInnerNode_shouldFormatCorrectly() {
        // 准备测试数据
        HtmlElement element = new HtmlElement("html", "container", null);
        element.setTextContent("Sample content");

        // 模拟拼写检查行为
        when(spellChecker.hasErrors("Sample content")).thenReturn(false);

        // 执行测试
        visitor.visit(element);

        String result = visitor.getOutput();
        assertEquals("html#container\n" +
                "└── Sample content", result.trim());
    }

    @Test
    void visitInnerNode_withSpellingError_shouldMarkWithX() {
        HtmlElement element = new HtmlElement("html", "container", null);
        element.setTextContent("Errror here");

        when(spellChecker.hasErrors("Errror here")).thenReturn(true);

        visitor.visit(element);
        String result = visitor.getOutput();
        assertTrue(result.contains("[X]html"));
    }

    @Test
    void visitInnerNode_showIdFalse_shouldHideId() {
        HtmlElement element = new HtmlElement("html", "container", null);
        element.setId("name");

        visitor.setShowId(false);
        visitor.visit(element);

        String result = visitor.getOutput();
        assertFalse(result.contains("#name"));
    }

    @Test
    void visitLeafNode_shouldDisplayText() {
        HtmlTextNode textNode = new HtmlTextNode("Hello world", null);

        visitor.visit(textNode);

        String result = visitor.getOutput();
        assertEquals("Hello world", result.trim());
    }

    @Test
    void visitLeafNode_emptyText_shouldSkip() {
        HtmlTextNode emptyNode = new HtmlTextNode("", null);

        visitor.visit(emptyNode);

        String result = visitor.getOutput();
        assertTrue(result.isEmpty());
    }

    @Test
    void visitLeafNode_nullText_shouldSkip() {
        HtmlTextNode nullNode = new HtmlTextNode(null, null);

        visitor.visit(nullNode);

        String result = visitor.getOutput();
        assertTrue(result.isEmpty());
    }



}