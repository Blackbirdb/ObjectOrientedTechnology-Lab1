package org.example.tools.SessionStateSaver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SessionState {
    public Set<String> openFiles = new HashSet<>();
    public String activeEditorName;
    public Map<String, Boolean> showIdMap = new HashMap<>();
    public String cwd;
}