package org.example.commands;

import org.example.session.Session;
import org.example.tools.SessionStateSaver.SessionState;
import org.example.tools.SessionStateSaver.SessionStateService;
import org.example.tools.utils.PathUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoadSessionCommandTest {

    @Mock
    private Session mockSession;

    @Mock
    private SessionStateService mockSessionStateService;

    private LoadSessionCommand command;

    @BeforeEach
    void setUp() {
        command = new LoadSessionCommand(mockSession, mockSessionStateService);
    }

    @Test
    void execute_shouldDoNothingWhenSessionFileNotExists() {
        when(mockSessionStateService.sessionFileExists()).thenReturn(false);

        command.execute();

        verify(mockSessionStateService).sessionFileExists();
        verifyNoMoreInteractions(mockSessionStateService);
        verifyNoInteractions(mockSession);
    }

    @Test
    void execute_shouldDoNothingWhenLoadedStateIsNull() {
        when(mockSessionStateService.sessionFileExists()).thenReturn(true);
        when(mockSessionStateService.loadSession()).thenReturn(null);

        command.execute();

        verify(mockSessionStateService).sessionFileExists();
        verify(mockSessionStateService).loadSession();
        verifyNoMoreInteractions(mockSessionStateService);
        verifyNoInteractions(mockSession);
    }

    @Test
    void execute_shouldLoadSessionStateCorrectly() {
        // Prepare test data
        String testCwd = "/test/directory";
        Set<String> testFiles = Set.of("file1.txt", "file2.txt");
        String activeEditor = "file2.txt";
        Map<String, Boolean> showIdMap = new HashMap<>();
        showIdMap.put("file1.txt", true);
        showIdMap.put("file2.txt", false);

        SessionState testState = new SessionState();
        testState.cwd = testCwd;
        testState.openFiles = testFiles;
        testState.activeEditorName = activeEditor;
        testState.showIdMap = showIdMap;

        // Mock behavior
        when(mockSessionStateService.sessionFileExists()).thenReturn(true);
        when(mockSessionStateService.loadSession()).thenReturn(testState);

        command.execute();

        // Verify interactions
        verify(mockSessionStateService).sessionFileExists();
        verify(mockSessionStateService).loadSession();
        verify(mockSession).setCwd(testCwd);

        // Verify file loading
        verify(mockSession).loadEditor(testCwd + "/file1.txt", "file1.txt", true);
        verify(mockSession).loadEditor(testCwd + "/file2.txt", "file2.txt", false);

        // Verify active editor setting
        verify(mockSession).setActiveEditorByName(activeEditor);

        // Verify no more unexpected interactions
        verifyNoMoreInteractions(mockSessionStateService, mockSession);
    }

    @Test
    void execute_shouldHandleMissingShowIdMapEntries() {
        // Prepare test data where showIdMap is missing some entries
        String testCwd = "/test/directory";
        Set<String> testFiles = Set.of("file1.txt", "file2.txt");
        String activeEditor = "file1.txt";
        Map<String, Boolean> showIdMap = new HashMap<>();
        showIdMap.put("file1.txt", false); // file2.txt is missing

        SessionState testState = new SessionState();
        testState.cwd = testCwd;
        testState.openFiles = testFiles;
        testState.activeEditorName = activeEditor;
        testState.showIdMap = showIdMap;

        // Mock behavior
        when(mockSessionStateService.sessionFileExists()).thenReturn(true);
        when(mockSessionStateService.loadSession()).thenReturn(testState);

        command.execute();

        // Verify interactions
        verify(mockSessionStateService).sessionFileExists();
        verify(mockSessionStateService).loadSession();
        verify(mockSession).setCwd(testCwd);

        // Verify file loading with default true for missing showIdMap entries
        verify(mockSession).loadEditor(testCwd + "/file1.txt", "file1.txt", false);
        verify(mockSession).loadEditor(testCwd + "/file2.txt", "file2.txt", true);

        // Verify active editor setting
        verify(mockSession).setActiveEditorByName(activeEditor);

        verifyNoMoreInteractions(mockSessionStateService, mockSession);
    }
}