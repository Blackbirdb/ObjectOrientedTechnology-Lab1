package org.example.editor;

import org.example.editor.commands.Command;
import org.example.editor.commands.IrrevocableCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandHistoryTest {

    private CommandHistory commandHistory;
    private Command mockCommand;
    private IrrevocableCommand mockIrrevocableCommand;

    @BeforeEach
    void setUp() {
        commandHistory = new CommandHistory();
        mockCommand = mock(Command.class);
        mockIrrevocableCommand = mock(IrrevocableCommand.class);
    }

    @Test
    void undoStackIsEmpty_returnsTrueWhenEmpty() {
        assertTrue(commandHistory.undoStackIsEmpty());
    }

    @Test
    void undoStackIsEmpty_returnsFalseWhenNotEmpty() {
        commandHistory.executeCommand(mockCommand);
        assertFalse(commandHistory.undoStackIsEmpty());
    }

    @Test
    void redoStackIsEmpty_returnsTrueWhenEmpty() {
        assertTrue(commandHistory.redoStackIsEmpty());
    }

    @Test
    void redoStackIsEmpty_returnsFalseWhenNotEmpty() {
        commandHistory.executeCommand(mockCommand);
        commandHistory.undo();
        assertFalse(commandHistory.redoStackIsEmpty());
    }

    @Test
    void isModified_returnsFalseInitially() {
        assertFalse(commandHistory.isModified());
    }

    @Test
    void isModified_returnsTrueAfterCommandExecution() {
        commandHistory.executeCommand(mockCommand);
        assertTrue(commandHistory.isModified());
    }

    @Test
    void resetModified_setsModifiedCountToZero() {
        commandHistory.executeCommand(mockCommand);
        commandHistory.resetModified();
        assertFalse(commandHistory.isModified());
    }

    @Test
    void executeCommand_withCommand_executesAndAddsToUndoStack() {
        commandHistory.executeCommand(mockCommand);

        verify(mockCommand).execute();
        assertFalse(commandHistory.undoStackIsEmpty());
        assertTrue(commandHistory.redoStackIsEmpty());
        assertTrue(commandHistory.isModified());
    }

    @Test
    void executeCommand_withCommand_clearsRedoStack() {
        // 先执行并撤销一个命令，使redo栈不为空
        commandHistory.executeCommand(mockCommand);
        commandHistory.undo();

        // 执行新命令
        Command newCommand = mock(Command.class);
        commandHistory.executeCommand(newCommand);

        assertTrue(commandHistory.redoStackIsEmpty());
    }

    @Test
    void executeCommand_withIrrevocableCommand_onlyExecutes() {
        commandHistory.executeCommand(mockIrrevocableCommand);

        verify(mockIrrevocableCommand).execute();
        assertTrue(commandHistory.undoStackIsEmpty());
        assertTrue(commandHistory.redoStackIsEmpty());
        assertFalse(commandHistory.isModified()); // Irrevocable命令不应影响modifiedCount
    }

    @Test
    void undo_executesUndoAndMovesToRedoStack() {
        commandHistory.executeCommand(mockCommand);
        commandHistory.undo();

        verify(mockCommand).undo();
        assertTrue(commandHistory.undoStackIsEmpty());
        assertFalse(commandHistory.redoStackIsEmpty());
        assertFalse(commandHistory.isModified());
    }

    @Test
    void undo_throwsWhenNoCommandsToUndo() {
        assertThrows(IllegalStateException.class, () -> {
            commandHistory.undo();
        });
    }

    @Test
    void redo_executesRedoAndMovesToUndoStack() {
        commandHistory.executeCommand(mockCommand);
        commandHistory.undo();
        commandHistory.redo();

        verify(mockCommand, times(2)).execute(); // 初始执行+redo执行
        assertFalse(commandHistory.undoStackIsEmpty());
        assertTrue(commandHistory.redoStackIsEmpty());
        assertTrue(commandHistory.isModified());
    }

    @Test
    void redo_throwsWhenNoCommandsToRedo() {
        assertThrows(IllegalStateException.class, () -> {
            commandHistory.redo();
        });
    }

    @Test
    void modifiedCount_incrementsAndDecrementsCorrectly() {
        // 初始状态
        assertEquals(0, commandHistory.isModified() ? 1 : 0);

        // 执行命令
        commandHistory.executeCommand(mockCommand);
        assertEquals(1, commandHistory.isModified() ? 1 : 0);

        // 撤销命令
        commandHistory.undo();
        assertEquals(0, commandHistory.isModified() ? 1 : 0);

        // 重做命令
        commandHistory.redo();
        assertEquals(1, commandHistory.isModified() ? 1 : 0);

        // 重置
        commandHistory.resetModified();
        assertEquals(0, commandHistory.isModified() ? 1 : 0);
    }

    @Test
    void multipleCommands_maintainCorrectOrder() {
        Command cmd1 = mock(Command.class);
        Command cmd2 = mock(Command.class);

        commandHistory.executeCommand(cmd1);
        commandHistory.executeCommand(cmd2);

        // 撤销应该是最后执行的命令先撤销
        commandHistory.undo();
        verify(cmd2).undo();

        commandHistory.undo();
        verify(cmd1).undo();

        // 重做应该是先撤销的命令后重做
        commandHistory.redo();
        verify(cmd1, times(2)).execute(); // 初始执行+redo执行

        commandHistory.redo();
        verify(cmd2, times(2)).execute(); // 初始执行+redo执行
    }
}