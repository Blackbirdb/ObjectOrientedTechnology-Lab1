package org.example.session.commands;

import org.example.commands.SaveSessionCommand;
import org.example.session.Session;
import org.example.tools.SessionStateSaver.SessionState;
import org.example.tools.SessionStateSaver.SessionStateService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;

class SaveIrrevocableCommandTest {
    private Session session;
    private SessionStateService service;
    private SaveSessionCommand command;
    private SessionState state;

    @BeforeEach
    void setUp() {
        session = mock(Session.class);
        service = mock(SessionStateService.class);
        state = mock(SessionState.class);
        command = new SaveSessionCommand(session, service, state);
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

        verify(service).saveSession(any(SessionState.class));
    }

    @Test
    void executeDoesNothingWhenSessionIsNotActive() {
        when(session.isActive()).thenReturn(false);

        command.execute();

        verify(service, never()).saveSession(any(SessionState.class));
    }

    @Test
    void executeHandlesNullOpenFilesGracefully() {
        when(session.isActive()).thenReturn(true);
        when(session.getOpenEditorNames()).thenReturn(null);
        when(session.getShowIdMap()).thenReturn(Map.of());
        when(session.getActiveEditorName()).thenReturn(null);
        when(session.getCwd()).thenReturn("/test/cwd");

        command.execute();

        verify(service).saveSession(any(SessionState.class));
    }

    @Test
    void executeHandlesEmptyShowIdMapGracefully() {
        when(session.isActive()).thenReturn(true);
        when(session.getOpenEditorNames()).thenReturn(Set.of("file1.html"));
        when(session.getShowIdMap()).thenReturn(Map.of());
        when(session.getActiveEditorName()).thenReturn("file1.html");
        when(session.getCwd()).thenReturn("/test/cwd");

        command.execute();

        verify(service).saveSession(any(SessionState.class));
    }
}