package org.example.session.commands;

import org.junit.jupiter.api.Test;

import org.example.editor.HtmlEditor;
import org.example.session.Session;
import org.example.tools.SessionStateSaver.SessionState;
import org.example.tools.SessionStateSaver.SessionStateSaver;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;

class LoadSessionCommandTest {
    private Session session;
    private SessionStateSaver sessionStateSaver;
    private LoadSessionCommand command;

    @BeforeEach
    void setUp() {
        session = mock(Session.class);
        sessionStateSaver = mock(SessionStateSaver.class);
        command = new LoadSessionCommand(session, sessionStateSaver);
    }

    @Test
    void executeLoadsSessionStateWhenFileExists() throws Exception {
        SessionState state = new SessionState();
        state.cwd = "src/test/resources/testFiles";
        state.openFiles = Set.of("nested.html", "spellcheck.html");
        state.showIdMap = Map.of("nested.html", true, "spellcheck.html", false);
        state.activeEditorName = "spellcheck.html";

        when(sessionStateSaver.sessionFileExists()).thenReturn(true);
        when(sessionStateSaver.loadSession()).thenReturn(state);

        command.execute();

        verify(session).setCwd("src/test/resources/testFiles");
        verify(session).loadEditor("src/test/resources/testFiles/nested.html", "nested.html", true);
        verify(session).loadEditor("src/test/resources/testFiles/spellcheck.html", "spellcheck.html", false);
        verify(session).setActiveEditorByName("spellcheck.html");
    }

    @Test
    void executeDoesNothingWhenSessionFileDoesNotExist() {
        when(sessionStateSaver.sessionFileExists()).thenReturn(false);

        command.execute();

        verify(session, never()).setCwd(anyString());
        verify(session, never()).addEditor(anyString(), any(HtmlEditor.class));
        verify(session, never()).setActiveEditor(any(HtmlEditor.class));
    }

    @Test
    void executeDoesNothingWhenSessionStateIsNull() {
        when(sessionStateSaver.sessionFileExists()).thenReturn(true);
        when(sessionStateSaver.loadSession()).thenReturn(null);

        command.execute();

        verify(session, never()).setCwd(anyString());
        verify(session, never()).addEditor(anyString(), any(HtmlEditor.class));
        verify(session, never()).setActiveEditor(any(HtmlEditor.class));
    }
}