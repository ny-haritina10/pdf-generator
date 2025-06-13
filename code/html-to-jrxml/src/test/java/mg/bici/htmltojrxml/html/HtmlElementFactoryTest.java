package mg.bici.htmltojrxml.html;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class HtmlElementFactoryTest {
    @Test
    public void testCreateFromJsoupElement_ValidElement_CreatesHtmlElement() {
        Element jsoupElement = new Element("div")
                .attr("id", "test-id")
                .addClass("class1 class2")
                .attr("style", "color: blue; font-size: 12px;")
                .text("Test content");

        HtmlElement result = HtmlElementFactory.createFromJsoupElement(jsoupElement);

        assertEquals("div", result.getTagName());
        assertEquals("test-id", result.getId());
        assertEquals(Arrays.asList("class1", "class2"), result.getClasses());
        assertEquals("Test content", result.getText());
        assertEquals("blue", result.getInlineStyles().get("color"));
        assertEquals("12px", result.getInlineStyles().get("font-size"));
        assertEquals("test-id", result.getAttributes().get("id"));
    }

    @Test
    public void testCreateTextElement_ValidText_CreatesSpan() {
        HtmlElement result = HtmlElementFactory.createTextElement("Test text");

        assertEquals("span", result.getTagName());
        assertEquals("Test text", result.getText());
        assertTrue(result.getClasses().isEmpty());
        assertTrue(result.getAttributes().isEmpty());
        assertTrue(result.getInlineStyles().isEmpty());
    }

    @Test
    public void testCreateContainerElement_ValidTag_CreatesEmptyElement() {
        HtmlElement result = HtmlElementFactory.createContainerElement("div");

        assertEquals("div", result.getTagName());
        assertNull(result.getText());
        assertTrue(result.getClasses().isEmpty());
        assertTrue(result.getAttributes().isEmpty());
        assertTrue(result.getInlineStyles().isEmpty());
        assertTrue(result.getChildren().isEmpty());
    }

    @Test
    public void testCreateFromNodeList_MultipleElements_CreatesList() {
        Elements jsoupElements = new Elements();
        jsoupElements.add(new Element("p").text("Paragraph"));
        jsoupElements.add(new Element("span").text("Span"));

        List<HtmlElement> result = HtmlElementFactory.createFromNodeList(jsoupElements);

        assertEquals(2, result.size());
        assertEquals("p", result.get(0).getTagName());
        assertEquals("Paragraph", result.get(0).getText());
        assertEquals("span", result.get(1).getTagName());
        assertEquals("Span", result.get(1).getText());
    }
}