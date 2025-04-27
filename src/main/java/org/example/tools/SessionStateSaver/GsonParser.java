package org.example.tools.SessionStateSaver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

@Component
public class GsonParser implements JsonParser{
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public String toJson(SessionState state) {
        return gson.toJson(state);
    }

    @Override
    public SessionState fromJson(String json) {
        return gson.fromJson(json, SessionState.class);
    }
}
