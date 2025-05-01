package org.example.commands;

import org.example.commands.CloseCommand;
import org.example.session.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.mockito.Mockito.*;

class CloseCommandTest {
    private Session session;
    private CloseCommand command;

    @BeforeEach
    void setUp() {
        session = mock(Session.class);
        command = new CloseCommand(session);
    }

    @Test
    void executePrintsMessageWhenNoActiveEditor() {
        when(session.existActivateEditor()).thenReturn(false);

        command.execute();

        verify(session, never()).isActiveEditorModified();
    }

    @Test
    void executeSavesFileWhenModifiedAndUserChoosesYes() {
        when(session.existActivateEditor()).thenReturn(true);
        when(session.isActiveEditorModified()).thenReturn(true);
        System.setIn(new ByteArrayInputStream("y\n".getBytes()));

        command.execute();

        verify(session).saveActiveEditor();
    }

    @Test
    void executeDoesNotSaveFileWhenModifiedAndUserChoosesNo() {
        when(session.existActivateEditor()).thenReturn(true);
        when(session.isActiveEditorModified()).thenReturn(true);
        System.setIn(new ByteArrayInputStream("n\n".getBytes()));

        command.execute();

        verify(session, never()).saveActiveEditor();
    }

    @Test
    void executeRemovesActiveEditor() {
        when(session.existActivateEditor()).thenReturn(true);
        when(session.isActiveEditorModified()).thenReturn(false);
        when(session.getActiveEditorName()).thenReturn("file.html");

        command.execute();

        verify(session).removeEditor("file.html");
    }

    @Test
    void executeSetsNextActiveEditorWhenEditorsRemain() {
        when(session.existActivateEditor()).thenReturn(true);
        when(session.isActiveEditorModified()).thenReturn(false);
        when(session.getActiveEditorName()).thenReturn("file.html");
        when(session.openEditorIsEmpty()).thenReturn(true);

        command.execute();

        verify(session).setActiveEditor(any());
    }

    @Test
    void executeSetsActiveEditorToNullWhenNoEditorsRemain() {
        when(session.existActivateEditor()).thenReturn(true);
        when(session.isActiveEditorModified()).thenReturn(false);
        when(session.getActiveEditorName()).thenReturn("file.html");
        when(session.openEditorIsEmpty()).thenReturn(true);

        command.execute();

        verify(session).setActiveEditor(null);
    }
}