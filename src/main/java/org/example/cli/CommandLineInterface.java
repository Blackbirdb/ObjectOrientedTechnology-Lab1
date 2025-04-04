package org.example.cli;

import org.example.command.HtmlEditor;
import org.example.session.SessionManager;
import org.example.utils.CommandTable;
import org.example.utils.PathValidateUtils;

import java.io.IOException;
import java.util.Scanner;


public class CommandLineInterface {
//    private HtmlEditor editor = null;
//    private boolean initialized = false;
    private final CommandTable commandTable = new CommandTable();
    private SessionManager sessionManager = new SessionManager();

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=============================================================================");
        System.out.println("Welcome to the html editor!");
        if (!sessionManager.cwdIsSet()){
            System.out.print("Please specify current working directory: ");
            String cwd = new Scanner(System.in).nextLine();
            sessionManager.setCwd(cwd);
        }
        System.out.println("Current working directory: " + sessionManager.getCwd());
        System.out.println("Please initialize the editor by using 'init' or 'read <filePath>' command.");
        System.out.println("Type \"help\" to see the available commands.");
        System.out.println("Type \"exit\" to exit the editor.");
        System.out.println("=============================================================================");

        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine();
            if (command.equals("exit")) break;
            processCommandWithExceptionHandling(command);
        }
        scanner.close();
    }

    void processCommandWithExceptionHandling(String command) {
        try {
            processCommand(command);
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid argument: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Null pointer exception: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Illegal State Exception: " + e.getMessage());
        }
    }

     void processCommand(String command) throws IOException {
        String[] parts = command.split(" ");

        if (!sessionManager.isActive() && !command.equals("init") && !parts[0].equals("read") && !command.equals("help")) {
            System.out.println("Please initialize the editor first by using 'init' or 'read <filePath>' command.");
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
                sessionManager.getActiveEditor().insertElement(tagName, idValue, insertLocation, textContent);
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
                sessionManager.getActiveEditor().appendElement(tagName, idValue, parentElement, textContent);
            }
            case "edit-id" -> {
                if (parts.length != 3) {
                    printWrongUsage("edit-id");
                    return;
                }
                String oldId = parts[1];
                String newId = parts[2];
                sessionManager.getActiveEditor().editId(oldId, newId);
            }
            case "edit-text" -> {
                if (parts.length < 2) {
                    printWrongUsage("edit-text");
                    return;
                }
                String element = parts[1];
                String newTextContent = parts.length > 2 ? command.substring(command.indexOf(element) + element.length()).trim() : null;
                sessionManager.getActiveEditor().editText(element, newTextContent);
            }
            case "delete" -> {
                if (parts.length != 2) {
                    printWrongUsage("delete");
                    return;
                }
                String elementId = parts[1];
                sessionManager.getActiveEditor().deleteElement(elementId);
            }
            case "undo" -> sessionManager.getActiveEditor().undo();
            case "redo" -> sessionManager.getActiveEditor().redo();
            case "read" -> {
                if (parts.length != 2) {
                    printWrongUsage("read");
                    return;
                }
                String filePath = parts[1];

                if (!PathValidateUtils.isValidHtmlFilePath(filePath)) {
                    System.out.println("Invalid file path: " + filePath);
                    return;
                }
                sessionManager.loadFile(filePath);
            }
            case "init" -> {
                sessionManager.loadFile("src/main/resources/default.html");
            }
            case "print-tree" -> {
                sessionManager.getActiveEditor().printTree();
            }
            case "spell-check" -> {
                sessionManager.getActiveEditor().spellCheck();
            }
            case "save" -> {
                if (parts.length != 2) {
                    printWrongUsage("save");
                    return;
                }
                String filePath = parts[1];
                if (!PathValidateUtils.isValidHtmlFilePath(filePath)) {
                    System.out.println("Invalid file path: " + filePath);
                    return;
                }
                sessionManager.saveFile(filePath);
            }
            case "help" -> {
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