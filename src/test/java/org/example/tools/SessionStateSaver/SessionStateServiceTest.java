package org.example.tools.SessionStateSaver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionStateServiceTest {

    @Mock
    private JsonParser mockJsonParser;

    private SessionStateService service;
    private SessionState testState;

    @TempDir
    Path tempDir;
    private String sessionFilePath;

    @BeforeEach
    void setUp() {
        testState = new SessionState();
        testState.cwd = "/test/directory";
        sessionFilePath = tempDir.resolve("session.json").toString();

        service = new SessionStateService(mockJsonParser, sessionFilePath);
    }

    @Test
    void saveSession_shouldWriteJsonToFile() throws IOException {
        String expectedJson = "{\"cwd\":\"/test/directory\"}";
        when(mockJsonParser.toJson(testState)).thenReturn(expectedJson);

        service.saveSession(testState);

        assertTrue(Files.exists(Path.of(sessionFilePath)));
        String actualContent = Files.readString(Path.of(sessionFilePath));
        assertEquals(expectedJson, actualContent);

        verify(mockJsonParser).toJson(testState);
    }

    @Test
    void saveSession_shouldThrowRuntimeExceptionOnIOException() {
        Path dirPath = tempDir.resolve("dummy_dir");
        try {
            Files.createDirectory(dirPath);
        } catch (IOException e) {
            fail("Test setup failed");
        }

        SessionStateService service = new SessionStateService(mockJsonParser, dirPath.toString());

        assertThrows(RuntimeException.class, () -> {
            service.saveSession(testState);
        });
    }

    @Test
    void loadSession_shouldReturnStateWhenFileExists() throws IOException {
        String jsonContent = "{\"cwd\":\"/test/directory\"}";
        Files.writeString(Path.of(sessionFilePath), jsonContent);

        when(mockJsonParser.fromJson(jsonContent)).thenReturn(testState);

        SessionState loadedState = service.loadSession();

        assertEquals(testState, loadedState);
        verify(mockJsonParser).fromJson(jsonContent);
    }

    @Test
    void loadSession_shouldReturnNullWhenFileNotExists() {
        SessionState loadedState = service.loadSession();

        assertNull(loadedState);
        verifyNoInteractions(mockJsonParser);
    }

    @Test
    void loadSession_shouldReturnNullOnIOException() {
        // Create a directory with the session file name to force IOException
        try {
            Files.createDirectory(Path.of(sessionFilePath));
        } catch (IOException e) {
            fail("Test setup failed");
        }

        SessionState loadedState = service.loadSession();

        assertNull(loadedState);
        verifyNoInteractions(mockJsonParser);
    }

    @Test
    void sessionFileExists_shouldReturnTrueWhenFileExists() throws IOException {
        Files.createFile(Path.of(sessionFilePath));

        assertTrue(service.sessionFileExists());
    }

    @Test
    void sessionFileExists_shouldReturnFalseWhenFileNotExists() {
        assertFalse(service.sessionFileExists());
    }

    @Test
    void sessionFileExists_shouldReturnFalseForDirectory() throws IOException {
        Files.createDirectory(Path.of(sessionFilePath));

        assertFalse(service.sessionFileExists());
    }
}