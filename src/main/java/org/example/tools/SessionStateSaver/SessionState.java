package org.example.tools.SessionStateSaver;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SessionState {
    public Set<String> openFiles = new HashSet<>();
    public String activeEditorName;
    public Map<String, Boolean> showIdMap = new HashMap<>();
    public String cwd;
}