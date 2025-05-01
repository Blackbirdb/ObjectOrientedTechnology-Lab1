package org.example.commands;

import org.example.commands.SetShowIdCommand;
import org.example.session.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class SetShowIdCommandTest {
    private Session session;
    private SetShowIdCommand command;

    @BeforeEach
    void setUp() {
        session = mock(Session.class);
    }

    @Test
    void executeSetsShowIdToTrue() {
        command = new SetShowIdCommand(session, true);

        command.execute();

        verify(session).setShowId(true);
    }

    @Test
    void executeSetsShowIdToFalse() {
        command = new SetShowIdCommand(session, false);

        command.execute();

        verify(session).setShowId(false);
    }
}