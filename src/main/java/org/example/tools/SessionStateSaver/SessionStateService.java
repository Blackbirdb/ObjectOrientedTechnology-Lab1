package org.example.tools.SessionStateSaver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;

@Component
public class SessionStateService {
    private final JsonParser jsonParser;
    private final String sessionFilePath;

    @Autowired
    public SessionStateService(JsonParser jsonParser, @Value("${session.file.path}") String sessionFilePath) {
        this.jsonParser = jsonParser;
        this.sessionFilePath = sessionFilePath;
    }

    public void saveSession(SessionState state) {
        try (Writer writer = new FileWriter(sessionFilePath)) {
            String json = jsonParser.toJson(state);
            writer.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SessionState loadSession() {
        try {
            String json = new String(Files.readAllBytes(Paths.get(sessionFilePath)));
            return jsonParser.fromJson(json);
        } catch (IOException e) {
            return null;
        }
    }

    public boolean sessionFileExists() {
        Path path = Paths.get(sessionFilePath);
        return Files.exists(path) && !Files.isDirectory(path);  // 增加目录检查
    }
}