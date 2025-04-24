package org.example.cli;

import org.example.session.SessionManager;
import org.example.tools.utils.CommandTable;
import org.example.tools.utils.PathUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandLineInterface {
    private final CommandTable commandTable;
    private final SessionManager sessionManager;
    private final Scanner scanner;
    private final List<String> initCommands = new ArrayList<>(List.of("init", "read", "load", "dir-tree","help"));

    public CommandLineInterface() {
        this.commandTable = new CommandTable();
        this.sessionManager = new SessionManager();
        this.scanner = new Scanner(System.in);
    }

    public CommandLineInterface(SessionManager sessionManager,CommandTable commandTable, Scanner scanner) {
        this.sessionManager = sessionManager;
        this.commandTable = commandTable;
        this.scanner = scanner;
    }

    public void start() {
        System.out.println("=========================================================================================");
        System.out.println("Welcome to the html editor!");
        while (!sessionManager.cwdIsSet()){
            System.out.println("Press 'Enter' to use the current directory as working directory.");
            System.out.println("Otherwise, enter the path to the working directory.");
            System.out.print("Path: ");
            String cwd = null;
            try {
                cwd = scanner.nextLine();
            } catch (Exception e) {
                break;
            }
            if (cwd.isEmpty()){
                sessionManager.setCwd(System.getProperty("user.dir"));
            }
            else if (!PathUtils.isValidFolder(cwd)) {
                System.out.println("Invalid path: " + cwd);
            }
            else {
                sessionManager.setCwd(cwd);
            }
        }
        System.out.println("Current working directory: " + sessionManager.getCwd());
        System.out.println("Please initialize the editor by using 'load <fileName>' command.");
        System.out.println("Type \"help\" to see the available commands.");
        System.out.println("Type \"exit\" to exit the editor.");
        System.out.println("=========================================================================================");

        while (true) {
            System.out.print("> ");
            String command = null;
            try {
                command = scanner.nextLine();
            } catch (Exception e) {
                break;
            }
            if (command.equals("exit")) {
                sessionManager.saveSession();
                break;
            }
            processCommandWithExceptionHandling(command);
        }
        scanner.close();
    }

    void processCommandWithExceptionHandling(String command) {
        try {
            processCommand(command);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid argument: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Null pointer exception: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Illegal State Exception: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

     void processCommand(String command) {
        String[] parts = command.split(" ");

        if (!sessionManager.isActive() && !initCommands.contains(parts[0])) {
            System.out.println("Please initialize the editor first!");
            return;
        }

        switch (parts[0]) {
            case "insert" -> {
                if (parts.length < 4) {
                    printWrongUsage("insert");
                    return;
                }
                String tagName = parts[1];
                String idValue = parts[2];
                String insertLocation = parts[3];
                String textContent = parts.length > 4 ? command.substring(command.indexOf(insertLocation) + insertLocation.length()).trim() : null;
                sessionManager.insertElement(tagName, idValue, insertLocation, textContent);
            }
            case "append" -> {
                if (parts.length < 4) {
                    printWrongUsage("append");
                    return;
                }
                String tagName = parts[1];
                String idValue = parts[2];
                String parentElement = parts[3];
                String textContent = parts.length > 4 ? command.substring(command.indexOf(parentElement) + parentElement.length()).trim() : null;
                sessionManager.appendElement(tagName, idValue, parentElement, textContent);
            }
            case "edit-id" -> {
                if (parts.length != 3) {
                    printWrongUsage("edit-id");
                    return;
                }
                String oldId = parts[1];
                String newId = parts[2];
                sessionManager.editId(oldId, newId);
            }
            case "edit-text" -> {
                if (parts.length < 2) {
                    printWrongUsage("edit-text");
                    return;
                }
                String element = parts[1];
                String newTextContent = parts.length > 2 ? command.substring(command.indexOf(element) + element.length()).trim() : null;
                sessionManager.editText(element, newTextContent);
            }
            case "delete" -> {
                if (parts.length != 2) {
                    printWrongUsage("delete");
                    return;
                }
                String elementId = parts[1];
                sessionManager.deleteElement(elementId);
            }
            case "undo" -> {
                if (parts.length != 1) {
                    printWrongUsage("undo");
                    return;
                }
                sessionManager.undo();
            }
            case "redo" -> {
                if (parts.length != 1) {
                    printWrongUsage("redo");
                    return;
                }
                sessionManager.redo();
            }
            case "load" -> {        // loads file in cwd
                if (parts.length != 2) {
                    printWrongUsage("load");
                    return;
                }
                String fileName = parts[1];
                if (!PathUtils.isHtmlFile(fileName)) {
                    System.out.println("Is not an HTML file: " + fileName);
                    return;
                }
                sessionManager.loadFile(fileName);
            }
            case "print-tree" -> {
                if (parts.length != 1) {
                    printWrongUsage("print-tree");
                    return;
                }
                sessionManager.printTree();
            }
            case "spell-check" -> {
                if (parts.length != 1) {
                    printWrongUsage("spell-check");
                    return;
                }
                sessionManager.spellCheck();
            }
            case "save" -> {
                if (parts.length != 2) {
                    printWrongUsage("save");
                    return;
                }
                String fileName = parts[1];
                if (!PathUtils.isHtmlFile(fileName)) {
                    System.out.println("Is not an HTML file: " + fileName);
                    return;
                }
                sessionManager.saveFile(fileName);
            }
            case "close" -> {
                if (parts.length != 1) {
                    printWrongUsage("close");
                    return;
                }
                sessionManager.close();
            }
            case "editor-list" -> {
                if (parts.length != 1) {
                    printWrongUsage("editor-list");
                    return;
                }
                sessionManager.editorList();
            }
            case "edit" -> {
                if (parts.length != 2) {
                    printWrongUsage("edit");
                    return;
                }
                String fileName = parts[1];
                if (!PathUtils.isHtmlFile(fileName)) {
                    System.out.println("Is not an HTML file: " + fileName);
                    return;
                }
                sessionManager.switchEditor(fileName);
            }
            case "showid" -> {
                if (parts.length != 2) {
                    printWrongUsage("showid");
                    return;
                }
                String showId = parts[1];
                if (!showId.equals("true") && !showId.equals("false")) {
                    System.out.println("Invalid argument: " + showId);
                    return;
                }
                sessionManager.setShowId(Boolean.parseBoolean(showId));
            }
            case "dir-tree" -> {
                if (parts.length != 1) {
                    printWrongUsage("dir-tree");
                    return;
                }
                sessionManager.dirTree();
            }
            case "help" -> {
                if (parts.length != 1) {
                    printWrongUsage("help");
                    return;
                }
                commandTable.printCommands();
            }
            default -> {
                System.out.println("Unknown Command. Please try again.");
                return;
            }
        }
        printSuccess(parts[0]);
    }

    private void printWrongUsage(String command) {
        System.out.println("Wrong usage for command: " + command + ". ");
        commandTable.printCommandUsage(command);
    }

    private void printSuccess(String command) {
        System.out.println("Command executed successfully: " + command);
    }
}