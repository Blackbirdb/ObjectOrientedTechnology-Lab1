package org.example.tools.utils;

import java.util.Stack;

public class PrintTreeUtils {

    public static String getIndentString(Stack<Boolean> isLastStack, int depth) {
        StringBuilder sb = new StringBuilder();
        int level = 1;

        while (level <= depth - 1) {
            boolean isLast = isLastStack.get(level);
            String appendString = isLast ? "    " : "│   ";
            sb.append(appendString);
            level++;
        }
        return sb.toString();
    }

    public static String getConnectorString(boolean isLast, int depth) {
        if (depth == 0) { return ""; }
        else {
            return isLast ? "└── " : "├── ";
        }
    }
}
