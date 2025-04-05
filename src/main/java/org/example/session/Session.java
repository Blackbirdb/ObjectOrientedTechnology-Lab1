package org.example.session;

import lombok.Getter;
import lombok.Setter;
import org.example.editor.HtmlEditor;
import org.example.utils.PathUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Getter
public class Session {
    private final Map<String, HtmlEditor> openEditors = new LinkedHashMap<>();
    @Setter private HtmlEditor activeEditor = null;
    @Setter private String cwd = null;

    public boolean isActive(){
        return activeEditor != null;
    }

    public boolean cwdIsSet() {
        return cwd != null;
    }

    public Set<String> getOpenEditorPaths() {
        return openEditors.keySet();
    }

    public boolean existEditorByName(String fileName){
        return openEditors.containsKey(fileName);
    }

    public String getPathFromName(String fileName){
        return PathUtils.getPathFromName(fileName, this.cwd);
    }

    public void addEditor(String fileName, HtmlEditor editor) {
        openEditors.put(fileName, editor);
    }

    public void removeEditor(String fileName) {
        openEditors.remove(fileName);
    }

    public String getActiveEditorName() {
        if (activeEditor != null) {
            return PathUtils.getNameFromPath(activeEditor.getFilePath(), cwd);
        }
        return null;
    }

    public void closeActiveEditor(){
        String fileName = getActiveEditorName();
        openEditors.remove(fileName);

        if (openEditors.isEmpty()) {
            activeEditor = null;
        } else {
            activeEditor = openEditors.entrySet().iterator().next().getValue();
        }
    }

    public void saveActiveEditor() throws IOException { activeEditor.save(); }

    public boolean existActivateEditor(){ return activeEditor != null; }

    public boolean isActiveEditorModified() {
        if (existActivateEditor()) {
            return activeEditor.isModified();
        }
        return false;
    }

    public void saveActiveEditorAs(String fileName) throws IOException {
        if (activeEditor != null) {
            activeEditor.saveToFile(getPathFromName(fileName));
        }
        else {
            throw new IllegalStateException("No active editor found");
        }
    }
}
