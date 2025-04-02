package org.example.cli;

import org.example.command.*;
import org.example.editor.HtmlEditor;

import java.util.Scanner;


public class CommandLineInterface {
    private HtmlEditor editor = new HtmlEditor("<html><body></body></html>");

    public void start() {
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

    private void processCommand(String command) {
        if (command.startsWith("insert ")) {
            String text = command.substring(7);
            new InsertTextCommand(editor, text).execute();
        } else if (command.equals("undo")) {
            editor.undo();
        } else if (command.equals("redo")) {
            editor.redo();
        } else if (command.equals("print")) {
            System.out.println(editor.getHtmlContent());
        } else {
            System.out.println("未知命令！");
        }
    }
}