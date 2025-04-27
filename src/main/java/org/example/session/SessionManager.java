package org.example.session;

import jakarta.annotation.PostConstruct;
import org.example.commands.*;
import org.example.commands.SaveActiveEditor;
import org.example.tools.SessionStateSaver.SessionState;
import org.example.tools.SessionStateSaver.SessionStateService;
import org.example.tools.filesys.Filesys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SessionManager {
    private final Session session;
    private final SessionStateService sessionStateService;
    private final Filesys filesys;
    private final SessionState state;

    @Autowired
    public SessionManager(Session session, SessionStateService sessionStateService, Filesys filesys, SessionState state) {
        this.session = session;
        this.sessionStateService = sessionStateService;
        this.filesys = filesys;
        this.state = state;
    }

    @PostConstruct
    public void init() {
        loadSession();
    }

/********************************* Session Level Operations *********************************/

    // prints the directory tree of the current working directory.
    public void dirTree() {
        IrrevocableCommand cmd = new DirTreeCommand(session, filesys);
        cmd.execute();
    }

    /**
     * loads file from cwd using filename.
     * If fileName does not exist, create one and init with default.html
     */
    public void loadFile(String fileName) {
        IrrevocableCommand cmd = new LoadFileCommand(session, fileName);
        cmd.execute();
    }


     /**
     * saves active file to the file specified by fileName
     */
    public void saveFile(String fileName) {
        IrrevocableCommand cmd = new SaveActiveEditor(session, fileName);
        cmd.execute();
    }

    /**
     * closes the active editor. will ask whether to save file if modified.
     * saves to the same file as opened.
     */
    public void close() {
        IrrevocableCommand cmd = new CloseCommand(session);
        cmd.execute();
    }

    public void editorList() {
        IrrevocableCommand cmd = new EditorListCommand(session);
        cmd.execute();
    }

    public void switchEditor(String fileName) {
        IrrevocableCommand cmd = new SwitchEditorCommand(session, fileName);
        cmd.execute();
    }

    public void setShowId(boolean showId) {
        IrrevocableCommand cmd = new SetShowIdCommand(session, showId);
        cmd.execute();
    }

    public void saveSession() {
        IrrevocableCommand cmd = new SaveSessionCommand(session, sessionStateService, state);
        cmd.execute();
    }

    public void loadSession() {
        IrrevocableCommand cmd = new LoadSessionCommand(session, sessionStateService);
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