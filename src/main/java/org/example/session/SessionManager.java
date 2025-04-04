package org.example.session;

import lombok.Getter;
import lombok.Setter;
import org.example.command.HtmlEditor;
import org.example.utils.PathValidateUtils;

import java.io.IOException;
import java.nio.file.Files;
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
     * If fileName does not exist, create one and init with default.html
     */
    public void loadFile(String fileName) throws IOException {
        if (openEditors.containsKey(fileName)) {
            throw new IllegalArgumentException(fileName + " is already opened");
        }

        String filePath = getPathFromName(fileName);

        if (!PathValidateUtils.fileExists(filePath)) {
            initNewHtmlFileAt(filePath);
        }
        HtmlEditor editor = new HtmlEditor(filePath);

        openEditors.put(fileName, editor);
        activeEditor = editor;
    }


    public static void initNewHtmlFileAt(String filePath) {
        Path sourcePath = Paths.get("src/main/resources/default.html");
        Path targetPath = Paths.get(filePath);

        try {
            Files.copy(sourcePath, targetPath);
        } catch (Exception e) {
            System.err.println("Failed to copy html file: " + filePath);
        }
    }

     /**
     * saves active file to the file specified by fileName
     */
    public void saveFile(String fileName) throws IOException {
        activeEditor.saveToFile(getPathFromName(fileName));
    }

    /**
     * closes the active editor. will ask whether to save file if modified.
     * saves to the same file as opened.
     */
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

        if (openEditors.isEmpty()) {
            System.out.println("No editors loaded. ");
        }

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

        System.out.print(sb);
    }

    public void switchEditor(String fileName) {
        if (openEditors.containsKey(fileName)) {
            activeEditor = openEditors.get(fileName);
        } else {
            System.out.println("File not loaded: " + fileName);
        }
    }
}