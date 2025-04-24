package org.example.session.commands;

import lombok.Setter;
import org.example.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoadFileCommand implements SessionCommand{
    private final Session session;
    @Setter private String fileName;

    @Autowired
    public LoadFileCommand(Session session) {
        this.session = session;
    }

    @Override
    public void execute() {
        if (session.existEditorByName(fileName)) {
            throw new IllegalArgumentException(fileName + " is already opened");
        }
        String filePath = session.getPathFromName(fileName);

        session.loadEditor(filePath, fileName, true);

        session.setActiveEditorByName(fileName);
    }

}
