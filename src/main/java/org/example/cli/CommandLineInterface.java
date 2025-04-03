package org.example.cli;

import org.example.command.*;
import org.example.command.HtmlEditor;
import org.example.service.HtmlFileReader;
import org.example.service.SpellChecker;
import org.example.service.TreePrinter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class CommandLineInterface {
    private final HtmlEditor editor = new HtmlEditor();
    private final Map<String, String> usageMap = new HashMap<>();
    private final HtmlFileReader reader = new HtmlFileReader();
    private final TreePrinter treePrinter = new TreePrinter(editor.getDocument());
    private final SpellChecker spellChecker = new SpellChecker();

    public CommandLineInterface() {
        initializeUsageMap();
    }

    private void initializeUsageMap() {
        usageMap.put("insert", "insert <tagName> <idValue> <insertLocation> [textContent]");
        usageMap.put("append", "append <tagName> <idValue> <parentElement> [textContent]");
        usageMap.put("edit-id", "edit-id <oldId> <newId>");
        usageMap.put("edit-text", "edit-text <element> [newTextContent]");
        usageMap.put("delete", "delete <elementId>");
        usageMap.put("read", "read <filePath>");
        // 添加其他命令的用法说明
    }

    public void start() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("欢迎使用 HTML 命令行编辑器！");
        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine();
            if (command.equals("exit")) break;
            processCommand(command);
        }
        scanner.close();
    }

    private void processCommand(String command) throws IOException {
        String[] parts = command.split(" ");

        if (parts[0].equals("insert")) {
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
        else if (parts[0].equals("append")) {
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
        else if (parts[0].equals("edit-id")) {
            if (parts.length != 3) {
                printWrongUsage("edit-id");
                return;
            }
            String oldId = parts[1];
            String newId = parts[2];
            editor.editId(oldId, newId);
        }
        else if (parts[0].equals("edit-text")) {
            if (parts.length < 2) {
                printWrongUsage("edit-text");
                return;
            }
            String element = parts[1];
            String newTextContent = parts.length > 2 ? command.substring(command.indexOf(element) + element.length()).trim() : null;
            editor.editText(element, newTextContent);
        }
        else if (parts[0].equals("delete")) {
            if (parts.length != 2) {
                printWrongUsage("delete");
                return;
            }
            String elementId = parts[1];
            editor.deleteElement(elementId);
        }
        else if (parts[0].equals("undo")) {
            editor.undo();
        }
        else if (parts[0].equals("redo")) {
            editor.redo();
        }
        else if (parts[0].equals("read")) {
            if (parts.length != 2) {
                printWrongUsage("read");
                return;
            }
            String filePath = parts[1];
            editor.setDocument(reader.readHtmlFromFile(filePath));
        }
        else if (parts[0].equals("init")) {
            editor.setDocument(reader.readHtmlFromFile("src/main/resources/default.html"));
        }
        else if (parts[0].equals("print-tree")){
            treePrinter.print();
        }
        else if (parts[0].equals("spell-check")){
            spellChecker.hasErrors(editor.getDocument());
        }
        else {
            System.out.println("Unknown Command. Please try again.");
            return;
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