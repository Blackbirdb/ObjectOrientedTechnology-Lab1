package org.example.tools.SessionStateSaver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;



class SessionStateServiceTest {

    private final SessionStateService saver = new SessionStateService(new GsonParser());

    @AfterEach
    void tearDown() {
        File sessionFile = new File("session.json");
        if (sessionFile.exists()) {
            sessionFile.delete();
        }
    }

    @Test
    void saveSessionCreatesFileWithCorrectContent() {
        SessionState state = new SessionState();
        state.cwd = "/path/to/cwd";
        state.openFiles = Set.of("file1.html", "file2.html");
        state.activeEditorName = "file1.html";
        state.showIdMap = new HashMap<>(Map.of("file1.html", true, "file2.html", false));

        saver.saveSession(state);

        assertTrue(Files.exists(Paths.get("session.json")));
        SessionState loadedState = saver.loadSession();
        assertEquals(state.cwd, loadedState.cwd);
        assertEquals(state.openFiles, loadedState.openFiles);
        assertEquals(state.activeEditorName, loadedState.activeEditorName);
        assertEquals(state.showIdMap, loadedState.showIdMap);
    }

    @Test
    void loadSessionReturnsNullWhenFileDoesNotExist() {
        File sessionFile = new File("session.json");
        if (sessionFile.exists()) {
            sessionFile.delete();
        }

        assertNull(saver.loadSession());
    }

    @Test
    void sessionFileExistsReturnsTrueWhenFileExists() {
        SessionState state = new SessionState();
        saver.saveSession(state);

        assertTrue(saver.sessionFileExists());
    }

    @Test
    void sessionFileExistsReturnsFalseWhenFileDoesNotExist() {
        File sessionFile = new File("session.json");
        if (sessionFile.exists()) {
            sessionFile.delete();
        }

        assertFalse(saver.sessionFileExists());
    }
}