package org.example.cli;

import org.example.command.HtmlEditor;
import org.example.document.HtmlDocument;
import org.example.service.HtmlFileReader;
import org.example.service.SpellChecker;
import org.example.service.TreePrinter;
import org.example.visitor.SpellCheckVisitor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class CommandLineInterface {
    private HtmlEditor editor;
    private HtmlFileReader reader;
    private final Map<String, String> usageMap = new HashMap<>();
    private boolean initialized = false;

    public CommandLineInterface() {
        initializeUsageMap();
        this.editor = null;
    }

    public CommandLineInterface(HtmlEditor editor) {
        this.editor = editor;
        initializeUsageMap();
    }

    private void initializeUsageMap() {
        usageMap.put("insert", "insert <tagName> <idValue> <insertLocation> [textContent]");
        usageMap.put("append", "append <tagName> <idValue> <parentElement> [textContent]");
        usageMap.put("edit-id", "edit-id <oldId> <newId>");
        usageMap.put("edit-text", "edit-text <element> [newTextContent]");
        usageMap.put("delete", "delete <elementId>");
        usageMap.put("read", "read <filePath>");
        usageMap.put("save", "save <filePath>");
        // 添加其他命令的用法说明
    }

    public void start() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the html editor!");
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
            System.out.println("Error processing command: " + e.getMessage());
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

        if (!initialized && !command.equals("init") && !command.equals("read")) {
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
                editor.insertElement(tagName, idValue, insertLocation, textContent);
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
                editor.appendElement(tagName, idValue, parentElement, textContent);
            }
            case "edit-id" -> {
                if (parts.length != 3) {
                    printWrongUsage("edit-id");
                    return;
                }
                String oldId = parts[1];
                String newId = parts[2];
                editor.editId(oldId, newId);
            }
            case "edit-text" -> {
                if (parts.length < 2) {
                    printWrongUsage("edit-text");
                    return;
                }
                String element = parts[1];
                String newTextContent = parts.length > 2 ? command.substring(command.indexOf(element) + element.length()).trim() : null;
                editor.editText(element, newTextContent);
            }
            case "delete" -> {
                if (parts.length != 2) {
                    printWrongUsage("delete");
                    return;
                }
                String elementId = parts[1];
                editor.deleteElement(elementId);
            }
            case "undo" -> editor.undo();
            case "redo" -> editor.redo();
            case "read" -> {
                if (parts.length != 2) {
                    printWrongUsage("read");
                    return;
                }
                String filePath = parts[1];

                reader = new HtmlFileReader(new HtmlDocument());
                editor = new HtmlEditor(reader.readHtmlFromFile(filePath));
                initialized = true;
            }
            case "init" -> {
                reader = new HtmlFileReader(new HtmlDocument());
                editor = new HtmlEditor(reader.readHtmlFromFile("src/main/resources/default.html"));
                initialized = true;
            }
            case "print-tree" -> {
                TreePrinter treePrinter = new TreePrinter(editor.getDocument());
                treePrinter.print();
            }
            case "spell-check" -> {
                SpellChecker spellChecker = new SpellChecker(editor.getDocument());
                spellChecker.printErrorMap();
            }
            case "save" -> {
                if (parts.length != 2) {
                    printWrongUsage("save");
                    return;
                }
                String filePath = parts[1];
                reader.saveHtmlDocumentToFile(editor.getDocument(), filePath);
            }
            default -> {
                System.out.println("Unknown Command. Please try again.");
                return;
            }
        }
        printSuccess(parts[0]);
    }

    private void printWrongUsage(String command) {
        String usage = usageMap.get(command);
        System.out.println("Wrong usage for command: " + command + ". \nUsage: " + usage);
    }

    private void printSuccess(String command) {
        System.out.println("Command executed successfully: " + command);
    }
}