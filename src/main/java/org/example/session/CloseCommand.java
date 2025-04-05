package org.example.session;

import org.example.editor.Command;
import org.example.utils.PathUtils;

import java.util.Scanner;

public class CloseCommand implements SessionCommand {
    private final Session session;

    public CloseCommand(Session session) {
        this.session = session;
    }

    @Override
    public void execute() {
        if (!session.existActivateEditor()) {
            System.out.println("No active editor to close.");
            return;
        }

        if (session.isActiveEditorModified()) {
            System.out.println("File is modified, do you want to save file? (y/n): ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            while (!input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n")) {
                System.out.println("Invalid input, please enter 'y' or 'n': ");
                input = scanner.nextLine();
            }
            if (input.equalsIgnoreCase("y")) {
                try {
                    session.saveActiveEditor();
                    System.out.println("File saved successfully.");
                } catch (Exception e) {
                    System.out.println("Error saving file: " + e.getMessage());
                }
            } else if (input.equalsIgnoreCase("n")) {
                System.out.println("File not saved.");
            }
        }

        String fileName = session.getActiveEditorName();
        session.removeEditor(fileName);

        if (session.getOpenEditors().isEmpty()) {
            session.setActiveEditor(null);
        }
        else {
            session.setActiveEditor(session.getOpenEditors().entrySet().iterator().next().getValue());
        }
    }
}
