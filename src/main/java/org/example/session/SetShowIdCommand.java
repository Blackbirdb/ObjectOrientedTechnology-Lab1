package org.example.session;

public class SetShowIdCommand implements SessionCommand{
    private Session session;
    private boolean showId;

    public SetShowIdCommand(Session session, boolean showId) {
        this.session = session;
        this.showId = showId;
    }

    @Override
    public void execute() {
        session.setShowId(showId);
    }
}
