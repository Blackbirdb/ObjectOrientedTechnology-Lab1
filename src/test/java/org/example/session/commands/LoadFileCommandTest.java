package org.example.session.commands;

import org.example.commands.LoadFileCommand;
import org.example.session.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoadFileCommandTest {
    private Session session;
    private LoadFileCommand command;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
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
        String fileName = "newFile.html";
        Path filePath = tempDir.resolve(fileName);

        when(session.existEditorByName(fileName)).thenReturn(false);
        when(session.getPathFromName(fileName)).thenReturn(filePath.toString());

        command = new LoadFileCommand(session, fileName);
        command.execute();

        verify(session).loadEditor(filePath.toString(), fileName, true);
        verify(session).setActiveEditorByName(fileName);
    }


}