package org.example.editor;

import static org.junit.jupiter.api.Assertions.*;

import org.example.document.HtmlTreeVisitor;
import org.example.tools.htmlparser.FileParserService;
import org.example.tools.spellchecker.SpellCheckerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class EditorFactoryTest {

    private EditorFactory editorFactory;

    @BeforeEach
    void setUp() {
        SpellCheckerService mockSpellChecker = mock(SpellCheckerService.class);
        FileParserService mockFileParser = mock(FileParserService.class);
        HtmlTreeVisitor mockVisitor = mock(HtmlTreeVisitor.class);
        CommandHistory mockCommandHistory = mock(CommandHistory.class);

        editorFactory = new EditorFactory(mockSpellChecker, mockFileParser, mockVisitor, mockCommandHistory);
    }

    @Test
    void createEditor_shouldReturnHtmlEditorInstance() {
        HtmlEditor editor = editorFactory.createEditor("test.html", true);

        assertNotNull(editor);
        assertInstanceOf(HtmlEditor.class, editor);
    }
}