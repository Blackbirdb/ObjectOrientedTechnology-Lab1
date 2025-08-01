package org.example.editor;

import org.example.commands.Command;
import org.example.commands.IrrevocableCommand;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Stack;

// invoker
@Component
@Scope("prototype")
public class CommandHistory {
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();
    private int modifiedCount = 0;

    public boolean undoStackIsEmpty() {
        return undoStack.isEmpty();
    }

    public boolean redoStackIsEmpty() {
        return redoStack.isEmpty();
    }

    public boolean isModified(){
        return modifiedCount > 0;
    }

    public void resetModified(){
        modifiedCount = 0;
    }

    public void executeCommand(Command cmd) {
        cmd.execute();
        undoStack.push(cmd);
        redoStack.clear();
        modifiedCount++;
    }

    public void executeCommand(IrrevocableCommand cmd) {
        cmd.execute();
    }

    public void undo() {
        if (undoStackIsEmpty()) {
            throw new IllegalStateException("No commands to undo.");
        }
        Command cmd = undoStack.pop();
        cmd.undo();
        redoStack.push(cmd);
        modifiedCount--;
    }

    public void redo() {
        if (redoStackIsEmpty()) {
            throw new IllegalStateException("No commands to redo.");
        }
        Command cmd = redoStack.pop();
        cmd.execute();
        undoStack.push(cmd);
        modifiedCount++;
    }
}