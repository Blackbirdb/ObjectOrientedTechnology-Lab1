package org.example.session;

import lombok.Getter;
import lombok.Setter;
import org.example.editor.HtmlEditor;
import org.example.tools.utils.PathUtils;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Component
public class Session {
    private final Map<String, HtmlEditor> openEditors = new LinkedHashMap<>();
    @Setter private HtmlEditor activeEditor = null;
    @Getter @Setter private String cwd = null;

    public boolean isActive(){
        return activeEditor != null;
    }

    HtmlEditor getActiveEditor(){ return activeEditor; }

    public Set<String> getOpenEditorNames() {
        return openEditors.keySet();
    }

    public boolean openEditorIsEmpty() {
        return openEditors.isEmpty();
    }

    public boolean editorIsModified(String fileName) {
        HtmlEditor editor = openEditors.get(fileName);
        if (editor != null) {
            return editor.isModified();
        }
        return false;
    }

    public HtmlEditor getFirstOpenEditor() {
        return openEditors.entrySet().iterator().next().getValue();
    }

    public Map<String, Boolean> getShowIdMap() {
        Map<String, Boolean> showIdMap = new LinkedHashMap<>();
        for (Map.Entry<String, HtmlEditor> entry : openEditors.entrySet()) {
            String fileName = entry.getKey();
            Boolean showId = entry.getValue().getShowId();
            showIdMap.put(fileName, showId);
        }
        return showIdMap;
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

    private void validateActiveEditor() {
        if (activeEditor == null) {
            throw new IllegalStateException("No active editor found.");
        }
    }

    public void loadEditor(String filePath, String fileName, boolean showId){
        HtmlEditor editor;

        if (!PathUtils.fileExists(filePath)) {
            editor = new HtmlEditor();
            editor.setFilePath(filePath);
            editor.saveToFile(filePath);
        }
        else {
            editor = new HtmlEditor(filePath);
        }
        editor.setShowId(showId);
        addEditor(fileName, editor);
    }


/************************************* Active Editor Operations ************************************/

    void insertElement(String tagName, String idValue, String insertLocation, String textContent) {
        validateActiveEditor();
        activeEditor.insertElement(tagName, idValue, insertLocation, textContent);
    }

    void appendElement(String tagName, String idValue, String parentElement, String textContent) {
        validateActiveEditor();
        activeEditor.appendElement(tagName, idValue, parentElement, textContent);
    }

    void editId(String oldId, String newId) {
        validateActiveEditor();
        activeEditor.editId(oldId, newId);
    }

    void editText(String element, String newTextContent) {
        validateActiveEditor();
        activeEditor.editText(element, newTextContent);
    }

    void deleteElement(String elementId) {
        validateActiveEditor();
        activeEditor.deleteElement(elementId);
    }

    void undo(){
        validateActiveEditor();
        activeEditor.undo();
    }

    void redo(){
        validateActiveEditor();
        activeEditor.redo();
    }

    void spellCheck() {
        validateActiveEditor();
        activeEditor.spellCheck();
    }

    void printTree() {
        validateActiveEditor();
        activeEditor.printTree();
    }

}