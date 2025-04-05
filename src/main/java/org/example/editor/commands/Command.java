package org.example.editor.commands;


public interface Command {
    void execute();
    void undo();
}