package org.example.session.commands;

import org.example.editor.HtmlEditor;
import org.example.session.Session;
import org.example.tools.utils.PathUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoadFileCommandTest {
    private Session session;
    private LoadFileCommand command;

    @BeforeEach
    void setUp() {
        session = mock(Session.class);
    }

    @Test
    void executeThrowsExceptionWhenFileAlreadyOpened() {
        when(session.existEditorByName("file.html")).thenReturn(true);
        command = new LoadFileCommand(session, "file.html");

        assertThrows(IllegalArgumentException.class, () -> command.execute());
    }

    @Test
    void executeCreatesNewHtmlFileWhenFileDoesNotExist() throws IOException {
        when(session.existEditorByName("mytest.html")).thenReturn(false);
        when(session.getPathFromName("mytest.html")).thenReturn("src/main/resources/mytest.html");
        command = new LoadFileCommand(session, "mytest.html");

        command.execute();

        assert PathUtils.fileExists("src/main/resources/mytest.html");
        verify(session).addEditor(eq("mytest.html"), any(HtmlEditor.class));
        verify(session).setActiveEditor(any(HtmlEditor.class));

        Files.deleteIfExists(Paths.get("src/main/resources/mytest.html"));
    }

    @Test
    void executeAddsExistingHtmlFileToSession() {
        when(session.existEditorByName("default.html")).thenReturn(false);
        when(session.getPathFromName("default.html")).thenReturn("src/main/resources/testFiles/default.html");
        command = new LoadFileCommand(session, "default.html");

        command.execute();

        assert PathUtils.fileExists("src/main/resources/testFiles/default.html");
        verify(session).addEditor(eq("default.html"), any(HtmlEditor.class));
        verify(session).setActiveEditor(any(HtmlEditor.class));
    }

}