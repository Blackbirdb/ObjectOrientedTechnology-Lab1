package org.example.commands;

import org.example.session.Session;

public class SetShowIdCommand implements IrrevocableCommand {
    private final Session session;
    private final boolean showId;

    public SetShowIdCommand(Session session, boolean showId) {
        this.session = session;
        this.showId = showId;
    }

    @Override
    public void execute() {
        session.setShowId(showId);
    }
}
