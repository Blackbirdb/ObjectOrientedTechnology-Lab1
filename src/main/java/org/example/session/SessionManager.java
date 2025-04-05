package org.example.session;

import org.example.editor.HtmlEditor;

import java.io.IOException;


public class SessionManager {
    private final Session session = new Session();

    /**
     * prints the directory tree of the current working directory.
     */
    public void dirTree() throws IOException {
        SessionCommand cmd = new DirTreeCommand(session);
        cmd.execute();
    }

    /**
     * loads file from cwd using filename.
     * If fileName does not exist, create one and init with default.html
     */
    public void loadFile(String fileName) throws IOException {
        SessionCommand cmd = new LoadFileCommand(session, fileName);
        cmd.execute();
    }


     /**
     * saves active file to the file specified by fileName
     */
    public void saveFile(String fileName) throws IOException {
        SessionCommand cmd = new SaveFileCommand(session, fileName);
        cmd.execute();
    }

    /**
     * closes the active editor. will ask whether to save file if modified.
     * saves to the same file as opened.
     */
    public void close() throws IOException {
        SessionCommand cmd = new CloseCommand(session);
        cmd.execute();
    }

    public void editorList() throws IOException {
        SessionCommand cmd = new EditorListCommand(session);
        cmd.execute();
    }

    public void switchEditor(String fileName) throws IOException {
        SessionCommand cmd = new SwitchEditorCommand(session, fileName);
        cmd.execute();
    }

    public void setShowId(boolean showId) throws IOException {
        SessionCommand cmd = new SetShowIdCommand(session, showId);
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