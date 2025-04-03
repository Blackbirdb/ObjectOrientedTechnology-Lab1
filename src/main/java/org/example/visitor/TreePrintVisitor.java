package org.example.visitor;

import org.example.document.HtmlElement;
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
        boolean isLast = true;
        if (!element.getTagName().equals("html")) {
            isLast = Boolean.TRUE.equals(isLastStack.peek());
        }
//        String connector = currentIndent > 0 ? (isLast ? "└── " : "├── ") : "";
        String connector = getConnectorString(isLast, currentIndent);

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
        List<HtmlElement> children = element.getChildren();
        currentIndent++;

        for (int i = 0; i < children.size(); i++) {
            isLastStack.push(i == children.size() - 1); // 判断当前子节点是否是最后一个
            children.get(i).accept(this);
            isLastStack.pop(); // 处理完当前子节点，恢复上一级状态
        }

        currentIndent--;
    }

    private String getIndentString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < currentIndent - 1; i++) {
            sb.append(Boolean.TRUE.equals(isLastStack.peek()) ? "    " : "│   ");
        }
        return sb.toString();
    }

    public String getOutput() {
        return output.toString();
    }

    private String getConnectorString(boolean isLast, int currentIndent) {
        return currentIndent > 0 ? (isLast ? "└── " : "├── ") : "";
    }

    // 在元素访问结束后调用（需要在外部框架支持）
    public void afterVisit(HtmlElement element) {
        if (!element.getChildren().isEmpty()) {
            currentIndent--;
            isLastStack.pop();
        }

        // 标记父节点的当前子节点是否为最后一个
        if (!isLastStack.isEmpty()) {
            isLastStack.pop();
            isLastStack.push(true); // 下一个节点将是最后一个
        }
    }

}