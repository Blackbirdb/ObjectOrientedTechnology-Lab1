package org.example.editor;


public interface Command {
    void execute();
    void undo();
}