package org.example.session;

import org.example.editor.HtmlEditor;

public class SessionManager {
    private final Session session;

    public SessionManager() {
        this.session = new Session();
        loadSession();
    }

    /**
     * prints the directory tree of the current working directory.
     */
    public void dirTree() {
        SessionCommand cmd = new DirTreeCommand(session);
        cmd.execute();
    }

    /**
     * loads file from cwd using filename.
     * If fileName does not exist, create one and init with default.html
     */
    public void loadFile(String fileName) {
        SessionCommand cmd = new LoadFileCommand(session, fileName);
        cmd.execute();
    }


     /**
     * saves active file to the file specified by fileName
     */
    public void saveFile(String fileName) {
        SessionCommand cmd = new SaveFileCommand(session, fileName);
        cmd.execute();
    }

    /**
     * closes the active editor. will ask whether to save file if modified.
     * saves to the same file as opened.
     */
    public void close() {
        SessionCommand cmd = new CloseCommand(session);
        cmd.execute();
    }

    public void editorList() {
        SessionCommand cmd = new EditorListCommand(session);
        cmd.execute();
    }

    public void switchEditor(String fileName) {
        SessionCommand cmd = new SwitchEditorCommand(session, fileName);
        cmd.execute();
    }

    public void setShowId(boolean showId) {
        SessionCommand cmd = new SetShowIdCommand(session, showId);
        cmd.execute();
    }

    public void saveSession() {
        SessionCommand cmd = new SaveSessionCommand(session);
        cmd.execute();
    }

    public void loadSession() {
        SessionCommand cmd = new LoadSessionCommand(session);
        cmd.execute();
    }


/********************************* Basic Session Operations *********************************/
    public HtmlEditor getActiveEditor() {
        return session.getActiveEditor();
    }

    public boolean isActive(){
        return session.isActive();
    }

    public boolean cwdIsSet() {
        return session.cwdIsSet();
    }

    public void setCwd(String cwd) {
        session.setCwd(cwd);
    }

    public String getCwd() {
        return session.getCwd();
    }
}