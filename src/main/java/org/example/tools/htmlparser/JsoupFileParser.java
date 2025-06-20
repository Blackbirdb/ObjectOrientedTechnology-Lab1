package org.example.tools.htmlparser;

import org.example.document.*;
import org.example.tools.treeprinter.TreeNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.springframework.stereotype.Component;


import static org.example.document.HtmlDocument.isSpecialTag;

@Component
public class JsoupFileParser implements HtmlFileParser {

    /**
     * Parses the given HTML string and returns an HtmlDocument object.
     */
    @Override
    public void parse(String html, HtmlDocument htmlDocument) {
        Document jsoupDoc = Jsoup.parse(html, "", Parser.xmlParser());

        validateDocumentStructure(jsoupDoc);

        Element jsoupRootElement = jsoupDoc.select("html").first();
        assert jsoupRootElement != null;

        HtmlElement htmlRootElement = parseElement(jsoupRootElement, null, htmlDocument);
        htmlDocument.setRoot(htmlRootElement);

    }


    /**
     * Parses a Jsoup Element(and its children, recurrently), and creates an HtmlElement.
     * @return HtmlElement
     */
    private HtmlElement parseElement(Element jsoupElement, HtmlElement parent, HtmlDocument htmlDocument) {
        // 获取标签名和ID
        String tagName = jsoupElement.tagName();
        String id = jsoupElement.id();

        if (id.isEmpty() && !isSpecialTag(tagName)) {
            throw new IllegalArgumentException("Tag <" + tagName + "> is not a special tag, and no id is given.");
        }

        HtmlElement element;
        if (!jsoupElement.ownText().isEmpty()) {
            element = htmlDocument.createElement(tagName, id, jsoupElement.ownText(), parent);
        }
        else {
            element = htmlDocument.createElement(tagName, id, parent);
        }

        // 递归处理子元素
        for (Element child : jsoupElement.children()) {
            TreeNode childNode;
            if (child.tagName().equals("#text")) {
                childNode = new HtmlTextNode(child.text(), element);
            } else {
                childNode = parseElement(child, element, htmlDocument);
            }
            element.insertAtLast(childNode);
        }

        return element;
    }

    /**
     * Rebuilds a Jsoup Document from an HtmlDocument.
     * @return String
     */
    @Override
    public String rebuild(HtmlDocument myDocument) {
        Document jsoupDoc = new Document("");

        Element htmlElement = rebuildElement(myDocument.getRoot());
        jsoupDoc.appendChild(htmlElement);

        Document.OutputSettings settings = new Document.OutputSettings();
        settings.indentAmount(4);  // 设置缩进值为4
        settings.prettyPrint(true);  // 启用格式化输出
        jsoupDoc.outputSettings(settings);

        return jsoupDoc.html();
    }

    /**
     * Rebuilds a Jsoup Element from an HtmlElement.
     * @return Element
     */
    private static Element rebuildElement(HtmlElement myElement) {
        Element jsoupElement = new Element(myElement.getTagName());

        jsoupElement.attr("id", myElement.getId());

        for (TreeNode child : myElement.getChildren()) {
            if (child instanceof HtmlElement) {
                jsoupElement.appendChild(rebuildElement((HtmlElement) child));
            } else if (child instanceof HtmlTextNode) {
                String text = ((HtmlTextNode) child).getText();
                if (text != null && !text.isEmpty()) {
                    jsoupElement.appendText(text);
                }
            }
        }

        return jsoupElement;
    }

    /**
     * checks if a document has the correct structure.
     */
    private void validateDocumentStructure(Document jsoupDoc) {
        if (jsoupDoc.select("html").size() != 1) {
            throw new IllegalArgumentException("Document must have exactly one html element");
        }

        Element html = jsoupDoc.select("html").first();
        assert html != null;
        if (html.children().select("head").size() != 1) {
            throw new IllegalArgumentException("html must have exactly one head child");
        }
        else if (html.children().select("body").size() != 1) {
            throw new IllegalArgumentException("html must have exactly one body child");
        }

        Element head = html.children().select("head").first();
        assert head != null;
        if (head.children().select("title").size() != 1) {
            throw new IllegalArgumentException("head must have exactly one title child");
        }
    }
}