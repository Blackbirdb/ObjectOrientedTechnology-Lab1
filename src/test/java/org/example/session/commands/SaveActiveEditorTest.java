package org.example.session.commands;

import org.example.commands.SaveActiveEditor;
import org.example.session.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaveActiveEditorTest {
    private Session session;
    private SaveActiveEditor command;

    @BeforeEach
    void setUp() {
        session = mock(Session.class);
    }

    @Test
    void executeSavesActiveEditorAsGivenFileNameWhenActiveEditorExists() {
        when(session.existActivateEditor()).thenReturn(true);
        command = new SaveActiveEditor(session, "file.html");

        command.execute();

        verify(session).saveActiveEditorAs("file.html");
    }

    @Test
    void executeThrowsExceptionWhenNoActiveEditorExists() {
        when(session.existActivateEditor()).thenReturn(false);
        command = new SaveActiveEditor(session, "file.html");

        assertThrows(IllegalStateException.class, () -> command.execute());
    }
}