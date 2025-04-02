package org.example.parser;

import org.example.document.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {
    private final HtmlElementFactory elementFactory;

    public HtmlParser(HtmlElementFactory factory) {
        this.elementFactory = factory;
    }

    public HtmlDocument parse(String html) {
        Document jsoupDoc = Jsoup.parse(html);
        HtmlDocument htmlDocument = new HtmlDocument();

        Element jsoupHtml = jsoupDoc.select("html").first();

        if (jsoupHtml == null) {
            throw new IllegalArgumentException("No <html> tag found in the document");
        }

        HtmlElement htmlElement = parseElement(jsoupHtml, htmlDocument);
        htmlDocument.setRoot(htmlElement);

        return htmlDocument;
    }

    private HtmlElement parseElement(Element jsoupElement, HtmlDocument document) {
        // 获取标签名和ID
        String tagName = jsoupElement.tagName();
        String id = jsoupElement.id();

        if (id.isEmpty()) {
            if (!isSpecialTag(tagName)) {
                throw new IllegalArgumentException("Tag <" + tagName + "> is not a special tag, and no id is given.");
            }
            id = tagName;
        }

        HtmlElement element = elementFactory.createElement(tagName, id, jsoupElement.ownText());

        // 递归处理子元素
        for (Element child : jsoupElement.children()) {
            HtmlNode childNode;
            if (child.tagName().equals("#text")) {
                childNode = new HtmlTextNode(child.text());
            } else {
                childNode = parseElement(child, document);
            }
            element.addChild(childNode);
        }

        return element;
    }

    private boolean isSpecialTag(String tagName) {
        return tagName.equals("html") || tagName.equals("head")
                || tagName.equals("title") || tagName.equals("body");
    }
}