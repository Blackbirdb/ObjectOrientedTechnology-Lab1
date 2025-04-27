package org.example.tools.SessionStateSaver;


public interface JsonParser {

    String toJson(SessionState state);

    SessionState fromJson(String json);
}
