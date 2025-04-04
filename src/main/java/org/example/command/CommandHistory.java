package org.example.command;

import java.util.Stack;

// 命令历史管理
public class CommandHistory {
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    public boolean undoStackIsEmpty() {
        return undoStack.isEmpty();
    }

    public boolean redoStackIsEmpty() {
        return redoStack.isEmpty();
    }

    public void executeCommand(Command cmd) {
        cmd.execute();
        undoStack.push(cmd);
        redoStack.clear();
    }

    public void undo() {
        if (undoStackIsEmpty()) {
            throw new IllegalStateException("No commands to undo.");
        }
        Command cmd = undoStack.pop();
        cmd.undo();
        redoStack.push(cmd);
    }

    public void redo() {
        if (redoStackIsEmpty()) {
            throw new IllegalStateException("No commands to redo.");
        }
        Command cmd = redoStack.pop();
        cmd.execute();
        undoStack.push(cmd);
    }
}