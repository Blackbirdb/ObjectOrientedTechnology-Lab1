package org.example.session.commands;

import org.example.session.Session;
import org.example.tools.SessionStateSaver.SessionState;
import org.example.tools.SessionStateSaver.SessionStateSaver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;

class SaveSessionCommandTest {
    private Session session;
    private SessionStateSaver sessionStateSaver;
    private SaveSessionCommand command;

    @BeforeEach
    void setUp() {
        session = mock(Session.class);
        sessionStateSaver = mock(SessionStateSaver.class);
        command = new SaveSessionCommand(session, sessionStateSaver);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get("session.json"));
    }

    @Test
    void executeSavesSessionStateWhenSessionIsActive() {
        when(session.isActive()).thenReturn(true);
        when(session.getOpenEditorNames()).thenReturn(Set.of("file1.html", "file2.html"));
        when(session.getShowIdMap()).thenReturn(Map.of("file1.html", true, "file2.html", false));
        when(session.getActiveEditorName()).thenReturn("file1.html");
        when(session.getCwd()).thenReturn("/test/cwd");

        command.execute();

        verify(sessionStateSaver).saveSession(any(SessionState.class));
    }

    @Test
    void executeDoesNothingWhenSessionIsNotActive() {
        when(session.isActive()).thenReturn(false);

        command.execute();

        verify(sessionStateSaver, never()).saveSession(any(SessionState.class));
    }

    @Test
    void executeHandlesNullOpenFilesGracefully() {
        when(session.isActive()).thenReturn(true);
        when(session.getOpenEditorNames()).thenReturn(null);
        when(session.getShowIdMap()).thenReturn(Map.of());
        when(session.getActiveEditorName()).thenReturn(null);
        when(session.getCwd()).thenReturn("/test/cwd");

        command.execute();

        verify(sessionStateSaver).saveSession(any(SessionState.class));
    }

    @Test
    void executeHandlesEmptyShowIdMapGracefully() {
        when(session.isActive()).thenReturn(true);
        when(session.getOpenEditorNames()).thenReturn(Set.of("file1.html"));
        when(session.getShowIdMap()).thenReturn(Map.of());
        when(session.getActiveEditorName()).thenReturn("file1.html");
        when(session.getCwd()).thenReturn("/test/cwd");

        command.execute();

        verify(sessionStateSaver).saveSession(any(SessionState.class));
    }
}