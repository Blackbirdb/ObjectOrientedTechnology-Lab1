package org.example.commands;

import org.example.session.Session;
import org.example.tools.SessionStateSaver.SessionState;
import org.example.tools.SessionStateSaver.SessionStateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveSessionCommandTest {

    @Mock
    private Session mockSession;

    @Mock
    private SessionStateService mockSessionStateService;

    private SessionState testState;
    private SaveSessionCommand command;

    @BeforeEach
    void setUp() {
        testState = new SessionState();
        command = new SaveSessionCommand(mockSession, mockSessionStateService, testState);
    }

    @Test
    void execute_shouldDoNothingWhenSessionNotActive() {
        when(mockSession.isActive()).thenReturn(false);

        command.execute();

        verify(mockSession).isActive();
        verifyNoMoreInteractions(mockSession);
        verifyNoInteractions(mockSessionStateService);
    }

    @Test
    void execute_shouldSaveSessionStateWhenActive() {
        // Setup test data
        String testCwd = "/test/directory";
        Set<String> testFiles = Set.of("file1.txt", "file2.txt");
        String activeEditor = "file1.txt";
        Map<String, Boolean> showIdMap = new HashMap<>();
        showIdMap.put("file1.txt", true);
        showIdMap.put("file2.txt", false);

        // Mock behavior
        when(mockSession.isActive()).thenReturn(true);
        when(mockSession.getOpenEditorNames()).thenReturn(testFiles);
        when(mockSession.getShowIdMap()).thenReturn(showIdMap);
        when(mockSession.getActiveEditorName()).thenReturn(activeEditor);
        when(mockSession.getCwd()).thenReturn(testCwd);

        command.execute();

        // Verify state was populated correctly
        assertEquals(testFiles, testState.openFiles);
        assertEquals(showIdMap, testState.showIdMap);
        assertEquals(activeEditor, testState.activeEditorName);
        assertEquals(testCwd, testState.cwd);

        // Verify interactions
        verify(mockSession).isActive();
        verify(mockSession).getOpenEditorNames();
        verify(mockSession).getShowIdMap();
        verify(mockSession).getActiveEditorName();
        verify(mockSession).getCwd();
        verify(mockSessionStateService).saveSession(testState);

        verifyNoMoreInteractions(mockSession, mockSessionStateService);
    }

    @Test
    void execute_shouldHandleEmptyOpenFiles() {
        // Setup test data with empty open files
        String testCwd = "/test/directory";
        Map<String, Boolean> emptyShowIdMap = new HashMap<>();

        // Mock behavior
        when(mockSession.isActive()).thenReturn(true);
        when(mockSession.getOpenEditorNames()).thenReturn(Set.of());
        when(mockSession.getShowIdMap()).thenReturn(emptyShowIdMap);
        when(mockSession.getActiveEditorName()).thenReturn(null);
        when(mockSession.getCwd()).thenReturn(testCwd);

        command.execute();

        // Verify state was populated correctly
        assertEquals(Set.of(), testState.openFiles);
        assertEquals(emptyShowIdMap, testState.showIdMap);
        assertNull(testState.activeEditorName);
        assertEquals(testCwd, testState.cwd);

        // Verify interactions
        verify(mockSession).isActive();
        verify(mockSession).getOpenEditorNames();
        verify(mockSession).getShowIdMap();
        verify(mockSession).getActiveEditorName();
        verify(mockSession).getCwd();
        verify(mockSessionStateService).saveSession(testState);
    }

    @Test
    void execute_shouldHandleNullActiveEditor() {
        // Setup test data with null active editor
        String testCwd = "/test/directory";
        Set<String> testFiles = Set.of("file1.txt", "file2.txt");
        Map<String, Boolean> showIdMap = new HashMap<>();
        showIdMap.put("file1.txt", true);

        // Mock behavior
        when(mockSession.isActive()).thenReturn(true);
        when(mockSession.getOpenEditorNames()).thenReturn(testFiles);
        when(mockSession.getShowIdMap()).thenReturn(showIdMap);
        when(mockSession.getActiveEditorName()).thenReturn(null);
        when(mockSession.getCwd()).thenReturn(testCwd);

        command.execute();

        // Verify state was populated correctly
        assertEquals(testFiles, testState.openFiles);
        assertEquals(showIdMap, testState.showIdMap);
        assertNull(testState.activeEditorName);
        assertEquals(testCwd, testState.cwd);

        // Verify interactions
        verify(mockSession).isActive();
        verify(mockSession).getOpenEditorNames();
        verify(mockSession).getShowIdMap();
        verify(mockSession).getActiveEditorName();
        verify(mockSession).getCwd();
        verify(mockSessionStateService).saveSession(testState);
    }

    @Test
    void execute_shouldHandleEmptyShowIdMap() {
        // Setup test data with empty showIdMap
        String testCwd = "/test/directory";
        Set<String> testFiles = Set.of("file1.txt");
        Map<String, Boolean> emptyShowIdMap = new HashMap<>();

        // Mock behavior
        when(mockSession.isActive()).thenReturn(true);
        when(mockSession.getOpenEditorNames()).thenReturn(testFiles);
        when(mockSession.getShowIdMap()).thenReturn(emptyShowIdMap);
        when(mockSession.getActiveEditorName()).thenReturn("file1.txt");
        when(mockSession.getCwd()).thenReturn(testCwd);

        command.execute();

        // Verify state was populated correctly
        assertEquals(testFiles, testState.openFiles);
        assertEquals(emptyShowIdMap, testState.showIdMap);
        assertEquals("file1.txt", testState.activeEditorName);
        assertEquals(testCwd, testState.cwd);

        // Verify interactions
        verify(mockSession).isActive();
        verify(mockSession).getOpenEditorNames();
        verify(mockSession).getShowIdMap();
        verify(mockSession).getActiveEditorName();
        verify(mockSession).getCwd();
        verify(mockSessionStateService).saveSession(testState);
    }
}