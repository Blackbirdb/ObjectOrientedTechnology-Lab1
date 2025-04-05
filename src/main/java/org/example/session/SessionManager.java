package org.example.session;

import lombok.Getter;
import lombok.Setter;
import org.example.editor.HtmlEditor;
import org.example.filesys.DirTreePrinter;
import org.example.utils.PathUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


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

//
//    public void editorList(){
//        StringBuilder sb = new StringBuilder();
//
//        if (openEditors.isEmpty()) {
//            System.out.println("No editors loaded. ");
//        }
//
//        for (Map.Entry<String, HtmlEditor> entry : openEditors.entrySet()) {
//
//            if (entry.getValue() == activeEditor) {
//                sb.append("> ");
//            } else {
//                sb.append("  ");
//            }
//            sb.append(entry.getKey());
//            if (entry.getValue().isModified()) {
//                sb.append("*");
//            }
//            sb.append("\n");
//        }
//
//        System.out.print(sb);
//    }
//
//    public void switchEditor(String fileName) {
//        if (openEditors.containsKey(fileName)) {
//            activeEditor = openEditors.get(fileName);
//        } else {
//            System.out.println("File not loaded: " + fileName);
//        }
//    }
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