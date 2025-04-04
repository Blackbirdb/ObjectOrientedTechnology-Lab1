package org.example.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CommandHistoryTest {

    @Mock private Command command1;
    @Mock private Command command2;

    private CommandHistory history;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        history = new CommandHistory();
    }

    @Test
    void executeCommand_shouldExecuteAndPushToUndoStack() {
        history.executeCommand(command1);
        verify(command1).execute();
        assertTrue(history.redoStackIsEmpty());
        assertFalse(history.undoStackIsEmpty());
    }

    @Test
    void executeCommand_shouldClearRedoStack() {
        history.executeCommand(command1);
        history.executeCommand(command2);
        verify(command1).execute();
        verify(command2).execute();
        assertFalse(history.undoStackIsEmpty());
        history.undo();
        assertFalse(history.redoStackIsEmpty());
        history.executeCommand(command1);
        assertTrue(history.redoStackIsEmpty());
    }

    @Test
    void undo_shouldPopFromUndoStackAndPushToRedoStack() {
        history.executeCommand(command1);
        history.undo();
        verify(command1).undo();
        assertTrue(history.undoStackIsEmpty());
        assertFalse(history.redoStackIsEmpty());
    }

    @Test
    void undo_shouldDoNothingWhenUndoStackIsEmpty() {
        history.undo();
        verify(command1, never()).undo();
        assertTrue(history.undoStackIsEmpty());
        assertTrue(history.redoStackIsEmpty());
    }

    @Test
    void redo_shouldPopFromRedoStackAndPushToUndoStack() {
        history.executeCommand(command1);
        history.undo();
        history.redo();
        verify(command1, times(2)).execute();
        assertFalse(history.undoStackIsEmpty());
        assertTrue(history.redoStackIsEmpty());
    }

    @Test
    void redo_shouldDoNothingWhenRedoStackIsEmpty() {
        history.redo();
        verify(command1, never()).execute();
        assertTrue(history.undoStackIsEmpty());
        assertTrue(history.redoStackIsEmpty());
    }
}