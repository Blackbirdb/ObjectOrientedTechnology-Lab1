package org.example.commands;

import org.example.session.Session;
import org.example.tools.utils.PathUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoadFileCommandTest {

    private Session mockSession;
    private LoadFileCommand command;
    private static final String TEST_FILE_NAME = "test.txt";

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        mockSession = mock(Session.class);
    }

    @Test
    void execute_shouldLoadFileWhenValid() throws IOException {
        // 创建临时测试文件
        Path testFile = tempDir.resolve(TEST_FILE_NAME);
        Files.writeString(testFile, "test content");

        when(mockSession.existEditorByName(TEST_FILE_NAME)).thenReturn(false);
        when(mockSession.getPathFromName(TEST_FILE_NAME)).thenReturn(testFile.toString());

        command = new LoadFileCommand(mockSession, TEST_FILE_NAME);
        command.execute();

        verify(mockSession).existEditorByName(TEST_FILE_NAME);
        verify(mockSession).getPathFromName(TEST_FILE_NAME);
        verify(mockSession).loadEditor(testFile.toString(), TEST_FILE_NAME, true);
        verify(mockSession).setActiveEditorByName(TEST_FILE_NAME);
    }

    @Test
    void execute_shouldThrowExceptionWhenFileAlreadyOpened() {
        when(mockSession.existEditorByName(TEST_FILE_NAME)).thenReturn(true);

        command = new LoadFileCommand(mockSession, TEST_FILE_NAME);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> command.execute());
        assertEquals(TEST_FILE_NAME + " is already opened", exception.getMessage());

        verify(mockSession).existEditorByName(TEST_FILE_NAME);
        verifyNoMoreInteractions(mockSession);
    }

    @Test
    void execute_shouldThrowExceptionWhenFileNotFound() {
        String nonExistentPath = "/path/to/nonexistent/file.txt";

        when(mockSession.existEditorByName(TEST_FILE_NAME)).thenReturn(false);
        when(mockSession.getPathFromName(TEST_FILE_NAME)).thenReturn(nonExistentPath);

        command = new LoadFileCommand(mockSession, TEST_FILE_NAME);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> command.execute());
        assertEquals("File not found: " + nonExistentPath, exception.getMessage());

        verify(mockSession).existEditorByName(TEST_FILE_NAME);
        verify(mockSession).getPathFromName(TEST_FILE_NAME);
        verifyNoMoreInteractions(mockSession);
    }

}