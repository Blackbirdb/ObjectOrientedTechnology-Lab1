package org.example.tools.utils;

import java.util.ArrayList;
import java.util.List;

public class CommandTable{
    public record CliCommand(String name, String usage, String description) {}
    private List<CliCommand> commands = new ArrayList<>();

    public CommandTable(){
        addCommand("insert", "insert <tagName> <idValue> <insertLocation> [textContent]",
                "Insert a new HTML element with the specified tag name and ID before the given location. " +
                        "Optionally, add text content.");
        addCommand("append", "append <tagName> <idValue> <parentElement> [textContent]",
                "Append a new HTML element with the specified tag name and ID as the last child to the " +
                        "given parent element. Optionally, add text content.");
        addCommand("edit-id", "edit-id <oldId> <newId>",
                "Edit the ID of an existing HTML element. The old ID must exist, and the new ID must be unique.");
        addCommand("edit-text", "edit-text <element> [newTextContent]",
                "Edit the text content of an existing HTML element. The element must exist.");
        addCommand("delete", "delete <elementId>",
                "Delete an existing HTML element by its ID. The element must exist.");
        addCommand("load", "load <fileName>",
                "Read the HTML file specified by fileName from cwd. The file must exist.");
        addCommand("save", "save <fileName>",
                "Save the current HTML document to the file specified by fileName under cwd.");
        addCommand("undo", "undo",
                "Undo the last command executed. This will revert the last change made to the HTML document.");
        addCommand("redo", "redo",
                "Redo the last undone command. This will reapply the last change that was undone.");
        addCommand("help", "help",
                "Display the list of available commands and their usage.");
        addCommand("exit", "exit",
                "Exit the HTML editor.");
        addCommand("spell-check", "spell-check",
                "Check the spelling of the text content in the HTML document. ");
        addCommand("print-tree", "print-tree",
                "Print the HTML document as a tree structure.");
        addCommand("close", "close",
                "Close the current HTML document. If there are unsaved changes, prompt to save.");
        addCommand("editor-list", "editor-list",
                "List all open HTML documents in the editor. For modified files, a \"*\" is displayed " +
                        "at the end of the file, and a \">\" is displayed at the front of the active file that is " +
                        "currently edited.");
        addCommand("show-id", "show-id <true|false>",
                "Set whether to show the ID of HTML elements in the tree view. " +
                        "true to show IDs, false to hide them. Default is true.");
        addCommand("edit", "edit <fileName>",
                "Edit the HTML file specified by fileName. The file must exist.");
        addCommand("dir-tree", "dir-tree",
                "Display the directory tree of the current working directory.");

    }

    private void addCommand(String name, String usage, String description){
        commands.add(new CliCommand(name, usage, description));
    }

    public List<CliCommand> getCommands() {
        return commands;
    }

    public void printCommands() {
        System.out.println("Available commands:\n");
        for (CliCommand command : commands) {
            System.out.printf("%s: %s\n", command.name(), command.usage());
            System.out.printf("Description: %s\n\n", command.description());
        }
    }

    public void printCommandUsage(String commandName) {
        for (CliCommand command : commands) {
            if (command.name().equals(commandName)) {
                System.out.printf("Usage: %s\n", command.usage());
                System.out.printf("Description: %s\n", command.description());
                return;
            }
        }
        System.out.println("Command not found.");
    }
}