package org.example.session;

import lombok.Getter;
import lombok.Setter;
import org.example.editor.HtmlEditor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Session {
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

    public Set<String> getOpenEditorPaths() {
        return openEditors.keySet();
    }
}
