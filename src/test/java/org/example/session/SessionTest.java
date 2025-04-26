package org.example.session;

import org.example.editor.EditorFactory;
import org.example.editor.HtmlEditor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionTest {
    private Session session;
    private HtmlEditor editor;

    @BeforeEach
    void setUp() {
        EditorFactory factory = mock(EditorFactory.class);
        editor = Mockito.mock(HtmlEditor.class);

        session = new Session(factory);
    }

    @Test
    void isActiveReturnsTrueWhenActiveEditorIsSet() {
        session.setActiveEditor(editor);

        assertTrue(session.isActive());
    }

    @Test
    void isActiveReturnsFalseWhenActiveEditorIsNotSet() {
        assertFalse(session.isActive());
    }

    @Test
    void cwdIsSetReturnsTrueWhenCwdIsSet() {
        session.setCwd("/path/to/cwd");

        assertTrue(session.cwdIsSet());
    }

    @Test
    void cwdIsSetReturnsFalseWhenCwdIsNotSet() {
        assertFalse(session.cwdIsSet());
    }

    @Test
    void getOpenEditorPathsReturnsEmptySetWhenNoEditorsAreOpen() {
        assertTrue(session.getOpenEditorPaths().isEmpty());
    }

    @Test
    void getOpenEditorPathsReturnsSetOfOpenEditorPaths() {
        session.addEditor("file1.html", editor);
        session.addEditor("file2.html", editor);

        assertEquals(Set.of("file1.html", "file2.html"), session.getOpenEditorPaths());
    }

    @Test
    void existEditorByNameReturnsTrueWhenEditorExists() {
        session.addEditor("file.html", editor);

        assertTrue(session.existEditorByName("file.html"));
    }

    @Test
    void existEditorByNameReturnsFalseWhenEditorDoesNotExist() {
        assertFalse(session.existEditorByName("file.html"));
    }

    @Test
    void getPathFromNameReturnsCorrectPath() {
        session.setCwd("/path/to/cwd");

        assertEquals("/path/to/cwd/file.html", session.getPathFromName("file.html"));
    }

    @Test
    void addEditorAddsEditorToOpenEditors() {
        session.addEditor("file.html", editor);

        assertTrue(session.existEditorByName("file.html"));
    }

    @Test
    void removeEditorRemovesEditorFromOpenEditors() {
        session.addEditor("file.html", editor);
        session.removeEditor("file.html");

        assertFalse(session.existEditorByName("file.html"));
    }

    @Test
    void getActiveEditorNameReturnsCorrectName() {
        when(editor.getFilePath()).thenReturn("/path/to/cwd/file.html");
        session.setCwd("/path/to/cwd");
        session.setActiveEditor(editor);

        assertEquals("file.html", session.getActiveEditorName());
    }

    @Test
    void saveActiveEditorCallsSaveOnActiveEditor() {
        session.setActiveEditor(editor);

        session.saveActiveEditor();

        verify(editor).save();
    }

    @Test
    void existActivateEditorReturnsTrueWhenActiveEditorIsSet() {
        session.setActiveEditor(editor);

        assertTrue(session.existActivateEditor());
    }

    @Test
    void isActiveEditorModifiedReturnsFalseWhenActiveEditorIsNotModified() {
        when(editor.isModified()).thenReturn(false);
        session.setActiveEditor(editor);

        assertFalse(session.isActiveEditorModified());
    }

    @Test
    void existActivateEditorReturnsFalseWhenActiveEditorIsNotSet() {
        assertFalse(session.existActivateEditor());
    }

    @Test
    void isActiveEditorModifiedReturnsTrueWhenActiveEditorIsModified() {
        when(editor.isModified()).thenReturn(true);
        session.setCwd("/path/to/cwd");
        session.setActiveEditor(editor);

        session.saveActiveEditorAs("newFile.html");

        verify(editor).saveToFile("/path/to/cwd/newFile.html");
    }

    @Test
    void setActiveEditorByNameSetsActiveEditor() {
        session.addEditor("file.html", editor);
        session.setActiveEditorByName("file.html");

        assertEquals(editor, session.getActiveEditor());
    }

    @Test
    void setShowIdCallsSetShowIdOnActiveEditor() {
        session.setActiveEditor(editor);

        session.setShowId(true);

        verify(editor).setShowId(true);
    }
}