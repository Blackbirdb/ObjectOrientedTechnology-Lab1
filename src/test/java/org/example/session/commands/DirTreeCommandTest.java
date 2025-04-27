package org.example.session.commands;

import org.example.commands.DirTreeCommand;
import org.example.session.Session;
import org.example.tools.filesys.Filesys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Set;

import static org.mockito.Mockito.*;

class DirTreeCommandTest {
    private Session session;
    private DirTreeCommand command;
    private Filesys filesys;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        session = mock(Session.class);
        filesys = mock(Filesys.class);
        command = new DirTreeCommand(session, filesys);
    }

    @Test
    void executePrintsDirTreeWhenSessionHasOpenFiles() {
        when(session.getCwd()).thenReturn(tempDir.toString());
        when(session.getOpenEditorPaths()).thenReturn(Set.of(tempDir.resolve("file1.html").toString(), tempDir.resolve("file2.html").toString()));

        command.execute();

        verify(session).getCwd();
        verify(session).getOpenEditorPaths();
    }

    @Test
    void executePrintsDirTreeWhenSessionHasNoOpenFiles() {
        when(session.getCwd()).thenReturn(tempDir.toString());
        when(session.getOpenEditorPaths()).thenReturn(Set.of());

        command.execute();

        verify(session).getCwd();
        verify(session).getOpenEditorPaths();
    }

}