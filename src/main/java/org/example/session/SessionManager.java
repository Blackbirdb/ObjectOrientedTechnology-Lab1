package org.example.session;

import lombok.Getter;
import lombok.Setter;
import org.example.command.HtmlEditor;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private String getPathFromName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }
        return cwd + "/" + fileName;
    }

    public static boolean isPathInCwd(String cwd, String path) {
        Path cwdPath = Paths.get(cwd).toAbsolutePath().normalize();
        Path targetPath = Paths.get(path).toAbsolutePath().normalize();
        return targetPath.startsWith(cwdPath);
    }

    private String getNameFromPath(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return null;
        }
        if (isPathInCwd(cwd, filePath)) {
            String[] parts = filePath.split("/");
            return parts[parts.length - 1];
        }
        return filePath;
    }

    /**
     * loads file from cwd using filename.
     */
    public void loadFile(String fileName) throws IOException {
        if (openEditors.containsKey(fileName)) {
            throw new IllegalArgumentException(fileName + " is already opened");
        }

        HtmlEditor editor = new HtmlEditor(getPathFromName(fileName));
        openEditors.put(fileName, editor);
        activeEditor = editor;
    }

    /**
     * loads files that are not in cwd
     */
    public void loadFileNotInCwd(String filePath) throws IOException {
        if (openEditors.containsKey(filePath)) {
            throw new IllegalArgumentException(filePath + " is already opened");
        }

        HtmlEditor editor = new HtmlEditor(filePath);
        openEditors.put(filePath, editor);
        activeEditor = editor;
    }

     /**
     * saves opened files to themselves. Both files in cwd and not can be saved.
     */
    public void saveFile(String fileName) throws IOException {
        if (openEditors.containsKey(fileName)) {
            HtmlEditor editor = openEditors.get(fileName);
            editor.save();
        } else {
            throw new IllegalArgumentException("File not loaded: " + fileName);
        }
    }


    public void close() {
        if (activeEditor == null) return;
        else if (activeEditor.isModified()) {
            System.out.println("File is modified, do you want to save file? (y/n): ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            while (!input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n")) {
                System.out.println("Invalid input, please enter 'y' or 'n': ");
                input = scanner.nextLine();
            }
            if (input.equalsIgnoreCase("y")) {
                try {
                    activeEditor.save();
                    System.out.println("File saved successfully.");
                } catch (IOException e) {
                    System.out.println("Error saving file: " + e.getMessage());
                }
            }
            else if (input.equalsIgnoreCase("n")) {
                System.out.println("File not saved.");
            }
        }

        String editorKey = activeEditor.getFilePath();
        if (isPathInCwd(cwd, editorKey)) {
            editorKey = getNameFromPath(editorKey);
        }
        openEditors.remove(editorKey);
        if (openEditors.isEmpty()) {
            activeEditor = null;
        } else {
            activeEditor = openEditors.entrySet().iterator().next().getValue();
        }
    }

    public void editorList(){
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, HtmlEditor> entry : openEditors.entrySet()) {

            if (entry.getValue() == activeEditor) {
                sb.append("> ");
            } else {
                sb.append("  ");
            }
            sb.append(entry.getKey());
            if (entry.getValue().isModified()) {
                sb.append("*");
            }
            sb.append("\n");
        }
        System.out.print(sb.toString());
    }

    public void switchEditor(String fileName) {
        if (openEditors.containsKey(fileName)) {
            activeEditor = openEditors.get(fileName);
        } else {
            System.out.println("File not loaded: " + fileName);
        }
    }



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