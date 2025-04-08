package org.example.session;

import org.example.session.commands.*;

public class SessionManager {
    private final Session session;

    public SessionManager() {
        this.session = new Session();
        loadSession();
    }


/********************************* Session Level Operations *********************************/

    // prints the directory tree of the current working directory.
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

/********************************* Active Editor Operations *********************************/

    public void insertElement(String tagName, String idValue, String insertLocation, String textContent) {
        session.insertElement(tagName, idValue, insertLocation, textContent);
    }

    public void appendElement(String tagName, String idValue, String insertLocation, String textContent) {
        session.appendElement(tagName, idValue, insertLocation, textContent);
    }

    public void editId(String oldId, String newId) {
        session.editId(oldId, newId);
    }

    public void editText(String element, String newTextContent) {
        session.editText(element, newTextContent);
    }

    public void deleteElement(String elementId) {
        session.deleteElement(elementId);
    }

    public void undo() {
        session.undo();
    }

    public void redo() {
        session.redo();
    }

    public void printTree() {
        session.printTree();
    }

    public void spellCheck() {
        session.spellCheck();
    }


/********************************* Basic Session Operations *********************************/

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