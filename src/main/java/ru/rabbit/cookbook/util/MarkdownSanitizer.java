package ru.rabbit.cookbook.util;

import java.util.Locale;

import lombok.val;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.HtmlBlock;
import org.commonmark.node.HtmlInline;
import org.commonmark.node.Image;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.markdown.MarkdownRenderer;
import org.springframework.stereotype.Component;

/**
 * Санитайзер Markdown-контента.
 * Удаляет встроенный HTML (XSS-вектор) и блокирует опасные URL-схемы
 * в ссылках и изображениях (javascript:, data:, vbscript:).
 */
@Component
public class MarkdownSanitizer {

    private static final Parser PARSER = Parser.builder().build();

    private static final MarkdownRenderer RENDERER = MarkdownRenderer.builder().build();

    public String sanitize(final String markdown) {
        if (markdown == null || markdown.isBlank()) {
            return markdown == null ? "" : markdown;
        }
        val document = PARSER.parse(markdown);
        sanitizeNode(document);
        return RENDERER.render(document);
    }

    private void sanitizeNode(final Node root) {
        root.accept(new AbstractVisitor() {

            @Override
            public void visit(final HtmlBlock htmlBlock) {
                htmlBlock.unlink();
            }

            @Override
            public void visit(final HtmlInline htmlInline) {
                htmlInline.unlink();
            }

            @Override
            public void visit(final Link link) {
                if (!isSafeUrl(link.getDestination())) {
                    link.setDestination("#");
                }
                visitChildren(link);
            }

            @Override
            public void visit(final Image image) {
                if (isSafeUrl(image.getDestination())) {
                    visitChildren(image);
                } else {
                    image.unlink();
                }
            }
        });
    }

    private boolean isSafeUrl(final String url) {
        if (url == null || url.isBlank()) {
            return true;
        }
        val lower = url.toLowerCase(Locale.ROOT).trim();
        if (!lower.contains(":")) {
            return true;
        }
        return lower.startsWith("http://")
            || lower.startsWith("https://")
            || lower.startsWith("ftp://")
            || lower.startsWith("mailto:");
    }
}
