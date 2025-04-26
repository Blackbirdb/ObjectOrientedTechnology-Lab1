package org.example.session.commands;

import org.junit.jupiter.api.Test;

import org.example.editor.HtmlEditor;
import org.example.session.Session;
import org.example.tools.SessionStateSaver.SessionState;
import org.example.tools.SessionStateSaver.GsonStateService;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;

class LoadSessionCommandTest {
    private Session session;
    private GsonStateService gsonStateService;
    private LoadSessionCommand command;

    @BeforeEach
    void setUp() {
        session = mock(Session.class);
        gsonStateService = mock(GsonStateService.class);
        command = new LoadSessionCommand(session, gsonStateService);
    }

    @Test
    void executeLoadsSessionStateWhenFileExists() throws Exception {
        SessionState state = new SessionState();
        state.cwd = "src/test/resources/testFiles";
        state.openFiles = Set.of("nested.html", "spellcheck.html");
        state.showIdMap = Map.of("nested.html", true, "spellcheck.html", false);
        state.activeEditorName = "spellcheck.html";

        when(gsonStateService.sessionFileExists()).thenReturn(true);
        when(gsonStateService.loadSession()).thenReturn(state);

        command.execute();

        verify(session).setCwd("src/test/resources/testFiles");
        verify(session).loadEditor("src/test/resources/testFiles/nested.html", "nested.html", true);
        verify(session).loadEditor("src/test/resources/testFiles/spellcheck.html", "spellcheck.html", false);
        verify(session).setActiveEditorByName("spellcheck.html");
    }

    @Test
    void executeDoesNothingWhenSessionFileDoesNotExist() {
        when(gsonStateService.sessionFileExists()).thenReturn(false);

        command.execute();

        verify(session, never()).setCwd(anyString());
        verify(session, never()).addEditor(anyString(), any(HtmlEditor.class));
        verify(session, never()).setActiveEditor(any(HtmlEditor.class));
    }

    @Test
    void executeDoesNothingWhenSessionStateIsNull() {
        when(gsonStateService.sessionFileExists()).thenReturn(true);
        when(gsonStateService.loadSession()).thenReturn(null);

        command.execute();

        verify(session, never()).setCwd(anyString());
        verify(session, never()).addEditor(anyString(), any(HtmlEditor.class));
        verify(session, never()).setActiveEditor(any(HtmlEditor.class));
    }
}