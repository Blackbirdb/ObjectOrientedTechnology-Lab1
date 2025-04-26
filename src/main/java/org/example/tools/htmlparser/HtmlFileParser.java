package org.example.tools.htmlparser;

import org.example.document.HtmlDocument;

public interface HtmlFileParser {
    HtmlDocument readHtmlFromFile(String filePath);
    void saveHtmlDocumentToFile(HtmlDocument document, String filePath);
}
