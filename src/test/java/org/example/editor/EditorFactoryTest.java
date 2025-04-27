package org.example.editor;

import static org.junit.jupiter.api.Assertions.*;

import org.example.document.HtmlDocument;
import org.example.document.HtmlTreeVisitor;
import org.example.tools.htmlparser.FileParserService;
import org.example.tools.spellchecker.SpellCheckerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

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
        HtmlDocument mockHtmlDocument = mock(HtmlDocument.class);

        ObjectProvider<HtmlDocument> htmlDocumentProvider = mock(ObjectProvider.class);
        when(htmlDocumentProvider.getIfAvailable()).thenReturn(mockHtmlDocument);

        ObjectProvider<CommandHistory> commandHistoryProvider = mock(ObjectProvider.class);
        when(commandHistoryProvider.getIfAvailable()).thenReturn(mockCommandHistory);

        editorFactory = new EditorFactory(mockSpellChecker, mockFileParser, mockVisitor, commandHistoryProvider, htmlDocumentProvider);
    }

    @Test
    void createEditor_shouldReturnHtmlEditorInstance() {
        HtmlEditor editor = editorFactory.createEditor("test.html", true);

        assertNotNull(editor);
        assertInstanceOf(HtmlEditor.class, editor);
    }
}