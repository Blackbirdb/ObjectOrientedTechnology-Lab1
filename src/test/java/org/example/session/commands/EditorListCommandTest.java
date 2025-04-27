package org.example.session.commands;

import org.example.commands.EditorListCommand;
import org.example.session.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EditorListCommandTest {
    private Session session;
    private EditorListCommand command;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;


    @BeforeEach
    void setUp() {
        session = mock(Session.class);
        command = new EditorListCommand(session);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void executePrintsEditorListWithActiveEditor() {
        when(session.existActivateEditor()).thenReturn(true);
        when(session.getOpenEditorNames()).thenReturn(Set.of("file1.html", "file2.html"));
        when(session.getActiveEditorName()).thenReturn("file1.html");
        when(session.editorIsModified("file1.html")).thenReturn(false);
        when(session.editorIsModified("file2.html")).thenReturn(true);

        command.execute();

        verify(session).getOpenEditorNames();
        verify(session, atLeast(1)).getActiveEditorName();
        verify(session).editorIsModified("file1.html");
        verify(session).editorIsModified("file2.html");
    }

    @Test
    void executePrintsNoEditorsLoadedWhenNoActiveEditor() {
        when(session.existActivateEditor()).thenReturn(false);

        command.execute();

        assertEquals("No editors loaded.\n", outContent.toString());
        verify(session, never()).getOpenEditorNames();
    }

}