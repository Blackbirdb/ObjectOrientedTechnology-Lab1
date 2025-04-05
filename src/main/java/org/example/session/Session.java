package org.example.session;

import lombok.Getter;
import lombok.Setter;
import org.example.editor.HtmlEditor;
import org.example.utils.PathUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Session {
    private final Map<String, HtmlEditor> openEditors = new LinkedHashMap<>();
    @Getter @Setter private HtmlEditor activeEditor = null;
    @Getter @Setter private String cwd = null;

    public boolean isActive(){
        return activeEditor != null;
    }

    public boolean cwdIsSet() {
        return cwd != null;
    }

    public Set<String> getOpenEditorPaths() {
        return openEditors.keySet();
    }

    public boolean containsEditorName(String fileName){
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

}
