package org.example.tools.SessionStateSaver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;

@Component
public class GsonStateService implements SessionStateService {
    private static final String SESSION_FILE = "session.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void saveSession(SessionState state) {
        try (Writer writer = new FileWriter(SESSION_FILE)) {
            gson.toJson(state, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SessionState loadSession() {
        try (Reader reader = new FileReader(SESSION_FILE)) {
            return gson.fromJson(reader, SessionState.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean sessionFileExists() {
        return Files.exists(Paths.get(SESSION_FILE));
    }
}