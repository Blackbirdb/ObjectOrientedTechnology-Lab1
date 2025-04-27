package org.example.commands;


public interface Command {
    void execute();
    void undo();
}