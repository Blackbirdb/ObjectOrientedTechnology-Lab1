package org.example.session.commands;

import org.example.session.Session;

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

        if (session.openEditorIsEmpty()) {
            session.setActiveEditor(null);
        }
        else {
            session.setActiveEditor(session.getFirstOpenEditor());
        }
    }
}
