package org.example.tools.SessionStateSaver;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;

@Component
public class SessionStateService {
    private static final String SESSION_FILE = "session.json";
    private final JsonParser jsonParser;

    public SessionStateService(JsonParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    public void saveSession(SessionState state) {
        try (Writer writer = new FileWriter(SESSION_FILE)) {
            String json = jsonParser.toJson(state);
            writer.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SessionState loadSession() {
        try {
            String json = new String(Files.readAllBytes(Paths.get(SESSION_FILE)));
            return jsonParser.fromJson(json);
        } catch (IOException e) {
            return null;
        }
    }

    public boolean sessionFileExists() {
        return Files.exists(Paths.get(SESSION_FILE));
    }
}