package org.example.session;

import lombok.Getter;
import lombok.Setter;
import org.example.command.HtmlEditor;

import java.io.IOException;
import java.util.*;

public class SessionManager {
    private final Map<String, HtmlEditor> openEditors = new LinkedHashMap<>();
    @Getter
    private HtmlEditor activeEditor = null;
    @Setter
    @Getter
    private String cwd = null;

    public boolean isActive(){
        return activeEditor != null;
    }

    public boolean cwdIsSet() {
        return cwd != null;
    }

    public void loadFile(String filePath) throws IOException {
        if (openEditors.containsKey(filePath)) {
            activeEditor = openEditors.get(filePath);
        }
        else {
            HtmlEditor editor = new HtmlEditor(filePath);
            openEditors.put(filePath, editor);
            activeEditor = editor;
        }
    }

    /**
     * Saves the filePath specified
     * @param filePath
     * @throws IOException
     */
    public void saveFile(String filePath) throws IOException {
        if (openEditors.containsKey(filePath)) {
            HtmlEditor editor = openEditors.get(filePath);
            editor.saveToFile(filePath);
        } else {
            throw new IllegalArgumentException("File not loaded: " + filePath);
        }
    }

//    public void close() {
//
//    }



//
//    public void saveFile(String fileName) {
//        if (openEditors.containsKey(fileName)) {
//            openEditors.get(fileName).save();
//        }
//    }
//
//    public void closeActiveEditor() {
//        if (activeEditor == null) return;
//        if (activeEditor.isModified()) {
//            // 询问用户是否保存（CLI 交互）
//            System.out.println("文件已修改，是否保存？(y/n)");
//            Scanner scanner = new Scanner(System.in);
//            String input = scanner.nextLine();
//            if (input.equalsIgnoreCase("y")) {
//                activeEditor.save();
//            }
//        }
//        openEditors.remove(activeEditor.getFileName());
//        activeEditor = openEditors.isEmpty() ? null : openEditors.values().iterator().next();
//    }
//
//    public void listEditors() {
//        for (HtmlEditor editor : openEditors.values()) {
//            String marker = editor == activeEditor ? "> " : "  ";
//            String modified = editor.isModified() ? "*" : "";
//            System.out.println(marker + editor.getFileName() + modified);
//        }
//    }
//
//    public void switchEditor(String fileName) {
//        if (openEditors.containsKey(fileName)) {
//            activeEditor = openEditors.get(fileName);
//        }
//    }

}