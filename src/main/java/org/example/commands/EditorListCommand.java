package org.example.commands;

import org.example.session.Session;

public class EditorListCommand implements IrrevocableCommand {
    private final Session session;

    public EditorListCommand(Session session) {
        this.session = session;
    }

    @Override
    public void execute() {
        if (!session.existActivateEditor()) {
            System.out.println("No editors loaded.");
            return;
        }

        assert !session.openEditorIsEmpty();

        StringBuilder sb = new StringBuilder();

        for (String fileName : session.getOpenEditorNames()) {

            if (fileName.equals(session.getActiveEditorName())) {
                sb.append("> ");
            } else {
                sb.append("  ");
            }
            sb.append(fileName);
            if (session.editorIsModified(fileName)) {
                sb.append("*");
            }
            sb.append("\n");
        }
        System.out.print(sb);
    }
}

