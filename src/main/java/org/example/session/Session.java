package org.example.session;

import lombok.Getter;
import lombok.Setter;
import org.example.SessionStateSaver.SessionState;
import org.example.SessionStateSaver.SessionStateSaver;
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
        return PathUtils.getNameFromPath(activeEditor.getFilePath(), cwd);
    }

    public void saveActiveEditor() { activeEditor.save(); }

    public boolean existActivateEditor(){ return activeEditor != null; }

    // assert exists activeEditor
    public boolean isActiveEditorModified() {
        return activeEditor.isModified();
    }

    public void saveActiveEditorAs(String fileName) {
        activeEditor.saveToFile(getPathFromName(fileName));
    }

    public void setActiveEditorByName(String fileName) {
        activeEditor = openEditors.get(fileName);
    }

    public void setShowId(boolean showId) { activeEditor.setShowId(showId); }

}