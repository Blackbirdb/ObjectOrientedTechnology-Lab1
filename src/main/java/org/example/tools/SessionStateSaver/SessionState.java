package org.example.tools.SessionStateSaver;

import java.util.*;

public class SessionState {
    public Set<String> openFiles = new HashSet<>();
    public String activeEditorName;
    public Map<String, Boolean> showIdMap = new HashMap<>();
    public String cwd;
}