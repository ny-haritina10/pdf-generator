package mg.bici.htmltojrxml.html;

import mg.bici.htmltojrxml.exceptions.ParsingException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class HtmlDocumentParserTest {
    private HtmlDocumentParser parser;
    private String testHtml;

    @Before
    public void setUp() {
        parser = new HtmlDocumentParser();
        testHtml = "<div id=\"container\" class=\"main\" style=\"font-size: 12px; color: red;\">" +
                   "<h1 class=\"title\">Payslip</h1>" +
                   "<p style=\"margin: 10px;\">Employee: <span id=\"name\">John Doe</span></p>" +
                   "<table class=\"data\"><tr><th>Item</th><td>Salary</td></tr></table>" +
                   "<script>alert('test');</script>" +
                   "</div>";
    }

    @Test
    public void testParseHtml_ValidHtml_ReturnsParsedDocument() {
        ParsedDocument result = parser.parseHtml(testHtml);

        assertNotNull("Parsed document should not be null", result);
        assertNotNull("Elements list should not be null", result.getElements());
        assertFalse("Elements list should not be empty", result.getElements().isEmpty());
        assertNotNull("Inline styles map should not be null", result.getInlineStyles());

        // verify root element
        HtmlElement root = result.getElements().get(0);
        assertEquals("div", root.getTagName());
        assertEquals("container", root.getId());
        assertTrue(root.getClasses().contains("main"));
        assertEquals(3, root.getChildren().size()); // h1, p, table
    }

    @Test
    public void testParseHtml_ExtractsInlineStyles() {
        ParsedDocument result = parser.parseHtml(testHtml);
        Map<String, String> inlineStyles = result.getInlineStyles();

        assertEquals(2, inlineStyles.size());
        assertTrue(inlineStyles.containsKey("container"));
        assertTrue(inlineStyles.containsKey("name"));
        assertTrue(inlineStyles.get("container").contains("font-size: 12px"));
        assertTrue(inlineStyles.get("name").isEmpty());
    }

    @Test
    public void testParseHtml_FiltersNonReportableElements() {
        ParsedDocument result = parser.parseHtml(testHtml);
        List<HtmlElement> elements = result.getElements();

        // script tag should be filtered out
        assertFalse(elements.stream().anyMatch(e -> e.getTagName().equals("script")));
    }

    @Test(expected = ParsingException.class)
    public void testParseHtml_InvalidHtml_ThrowsParsingException() {
        String invalidHtml = "<div>Unclosed tag";
        parser.parseHtml(invalidHtml);
    }

    @Test
    public void testIsReportableElement() {
        org.jsoup.nodes.Element div = new org.jsoup.nodes.Element("div").text("Test");
        org.jsoup.nodes.Element script = new org.jsoup.nodes.Element("script").text("alert('test')");
        org.jsoup.nodes.Element emptyMeta = new org.jsoup.nodes.Element("meta");

        assertTrue(parser.isReportableElement(div));
        assertFalse(parser.isReportableElement(script));
        assertFalse(parser.isReportableElement(emptyMeta));
    }

    @Test
    public void testExtractElements_HandlesNestedElements() {
        ParsedDocument result = parser.parseHtml(testHtml);
        HtmlElement root = result.getElements().get(0);
        List<HtmlElement> children = root.getChildren();

        assertEquals(3, children.size());
        assertEquals("h1", children.get(0).getTagName());
        assertEquals("p", children.get(1).getTagName());
        assertEquals("table", children.get(2).getTagName());

        // verify nested span in p
        HtmlElement p = children.get(1);
        assertEquals(1, p.getChildren().size());
        assertEquals("span", p.getChildren().get(0).getTagName());
        assertEquals("John Doe", p.getChildren().get(0).getText());
    }
}