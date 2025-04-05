package org.example.SessionStateSaver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionState {
    public List<String> openFiles = new ArrayList<>();
    public String activeEditorName;
    public Map<String, Boolean> showIdMap = new HashMap<>();
    public String cwd;
}