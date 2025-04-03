package org.example.visitor;

import org.example.document.HtmlElement;
import org.example.document.HtmlNode;
import org.example.document.HtmlTextNode;
import org.example.service.SpellChecker;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class TreePrintVisitor implements HtmlVisitor {
    private final StringBuilder output = new StringBuilder();
    private final SpellChecker spellChecker = new SpellChecker();
    private int currentIndent = 0;
    private final Deque<Boolean> isLastStack = new ArrayDeque<>();

    public String getTreeOutput() {
        return output.toString();
    }

    @Override
    public void visit(HtmlElement element) {
        // 生成缩进
        String indent = getIndentString();
        boolean isLast = Boolean.TRUE.equals(isLastStack.peek()); // 简化判断
        String connector = getConnectorString(isLast);

        // 打印当前元素
        output.append(indent).append(connector).append(element.getTagName());

        // 处理 ID 选择器 (例如 h1#title)
        if (element.getId() != null && !element.getId().isEmpty()) {
            output.append("#").append(element.getId());
        }
        output.append("\n");

        // 打印文本内容（如果有）
        if (!element.getTextContent().isEmpty()) {
            output.append(indent)
                    .append(isLast ? "    " : "│   ") // 保持正确的缩进
                    .append("└── ") // 额外缩进
                    .append(element.getTextContent())
                    .append("\n");
        }

        // 处理子元素
        List<HtmlNode> children = element.getChildren();
        currentIndent++;

        for (int i = 0; i < children.size(); i++) {
            isLastStack.push(i == children.size() - 1); // 判断当前子节点是否是最后一个
            children.get(i).accept(this);
            isLastStack.pop(); // 处理完当前子节点，恢复上一级状态
        }

        currentIndent--;
    }

    @Override
    public void visit(HtmlTextNode textNode) {
        // 处理文本节点
        String indent = getIndentString();
        boolean isLast = Boolean.TRUE.equals(isLastStack.peek());
        String connector = getConnectorString(isLast);

        output.append(indent).append(connector).append(textNode.getText()).append("\n");
    }

    private String getIndentString() {
        StringBuilder sb = new StringBuilder();
        int level = 0;
        for (boolean isLast : isLastStack) {  // 遍历整个栈，决定每层的缩进
            if (level < currentIndent - 1) {
                sb.append(isLast ? "    " : "│   ");
            }
            level++;
        }
        return sb.toString();
    }

    private String getConnectorString(boolean isLast) {
        return isLast ? "└── " : "├── ";
    }
}