package org.example.session;

public class SwitchEditorCommand implements SessionCommand{
    private final Session session;
    private final String fileName;

    public SwitchEditorCommand(Session session, String fileName) {
        this.session = session;
        this.fileName = fileName;
    }

    @Override
    public void execute() {
        if (session.existEditorByName(fileName)) {
            session.setActiveEditorByName(fileName);
        } else {
            System.out.println("File not loaded: " + fileName);
        }
    }
}
