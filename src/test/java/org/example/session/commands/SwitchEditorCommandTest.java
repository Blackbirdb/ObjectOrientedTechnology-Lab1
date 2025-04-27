package org.example.session.commands;

import org.example.commands.SwitchEditorCommand;
import org.example.session.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SwitchEditorCommandTest {
    private Session session;
    private SwitchEditorCommand command;

    @BeforeEach
    void setUp() {
        session = mock(Session.class);
    }

    @Test
    void executeSetsActiveEditorWhenFileExists() {
        when(session.existEditorByName("file.html")).thenReturn(true);
        command = new SwitchEditorCommand(session, "file.html");

        command.execute();

        verify(session).setActiveEditorByName("file.html");
    }

    @Test
    void executeThrowsExceptionWhenFileDoesNotExist() {
        when(session.existEditorByName("file.html")).thenReturn(false);
        command = new SwitchEditorCommand(session, "file.html");

        assertThrows(IllegalStateException.class, () -> command.execute());
    }
}