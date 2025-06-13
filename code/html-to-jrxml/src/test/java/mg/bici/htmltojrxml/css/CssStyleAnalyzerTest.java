package mg.bici.htmltojrxml.css;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.css.CSSStyleSheet;

import mg.bici.htmltojrxml.exceptions.ParsingException;
import mg.bici.htmltojrxml.html.HtmlElement;
import mg.bici.htmltojrxml.utils.Color;

public class CssStyleAnalyzerTest {
    private CssStyleAnalyzer analyzer;
    private String testCss;
    private HtmlElement container;
    private HtmlElement title;
    private HtmlElement span;

    @Before
    public void setUp() {
        analyzer = new CssStyleAnalyzer();
        testCss = "#container {\n" +
                  "    font-family: Arial;\n" +
                  "    font-size: 12px;\n" +
                  "    color: #FF0000;\n" +
                  "    margin: 10px;\n" +
                  "}\n" +
                  ".title {\n" +
                  "    font-weight: bold;\n" +
                  "    text-align: center;\n" +
                  "}\n" +
                  "p > span {\n" +
                  "    color: black;\n" +
                  "}\n" +
                  "[data-test] {\n" +
                  "    padding: 5px;\n" +
                  "}";

        // mock HTML structure
        container = new HtmlElement();
        container.setTagName("div");
        container.setId("container");
        container.setClasses(Arrays.asList("main"));
        container.setInlineStyles(new HashMap<>());

        title = new HtmlElement();
        title.setTagName("h1");
        title.setClasses(Arrays.asList("title"));
        title.setParent(container);
        title.setInlineStyles(new HashMap<>());
        container.getChildren().add(title);

        span = new HtmlElement();
        span.setTagName("span");
        span.setInlineStyles(new HashMap<>());

        HtmlElement p = new HtmlElement();
        p.setTagName("p");
        p.setAttributes(new HashMap<String, String>() {{ put("data-test", "value"); }});
        p.setParent(container);
        p.setInlineStyles(new HashMap<>());
        p.getChildren().add(span);
        span.setParent(p);
        container.getChildren().add(p);
    }

    @Test
    public void testParseStylesheet_ValidCss_ReturnsStylesheet() {
        CSSStyleSheet stylesheet = analyzer.parseStylesheet(testCss);
        assertNotNull("Stylesheet should not be null", stylesheet);
        assertEquals(4, stylesheet.getCssRules().getLength());
    }

    @Test(expected = ParsingException.class)
    public void testParseStylesheet_InvalidCss_ThrowsParsingException() {
        String invalidCss = "div { color: red; /* unclosed comment ";
        analyzer.parseStylesheet(invalidCss);
    }

    @Test
    public void testComputeStyle_AppliesIdSelectorStyles() {
        CSSStyleSheet stylesheet = analyzer.parseStylesheet(testCss);
        ComputedStyle style = analyzer.computeStyle(container, stylesheet);

        assertEquals("Arial", style.getFontFamily());
        assertEquals(12f, style.getFontSize().getValue(), 0.01);
        assertEquals(CssLength.Unit.PX, style.getFontSize().getUnit());
        assertEquals(new Color(255, 0, 0), style.getColor());
        assertEquals(10f, style.getMargin().getTop(), 0.01);
    }

    @Test
    public void testComputeStyle_AppliesClassSelectorStyles() {
        CSSStyleSheet stylesheet = analyzer.parseStylesheet(testCss);
        ComputedStyle style = analyzer.computeStyle(title, stylesheet);

        assertEquals(FontWeight.BOLD, style.getFontWeight());
        assertEquals(TextAlign.CENTER, style.getTextAlign());
    }

    @Test
    public void testComputeStyle_AppliesChildSelectorStyles() {
        CSSStyleSheet stylesheet = analyzer.parseStylesheet(testCss);
        ComputedStyle style = analyzer.computeStyle(span, stylesheet);

        assertEquals(new Color(0, 0, 0), style.getColor());
    }

    @Test
    public void testComputeStyle_AppliesAttributeSelectorStyles() {
        CSSStyleSheet stylesheet = analyzer.parseStylesheet(testCss);
        ComputedStyle style = analyzer.computeStyle(span.getParent(), stylesheet);

        assertEquals(5f, style.getPadding().getTop(), 0.01);
    }

    @Test
    public void testApplyCascade_OverridesInlineStyles() {
        container.getInlineStyles().put("color", "blue");
        CSSStyleSheet stylesheet = analyzer.parseStylesheet(testCss);
        ComputedStyle style = new ComputedStyle();
        analyzer.applyCascade(container, stylesheet, style);

        assertEquals(new Color(0, 0, 255), style.getColor()); // inline style wins
        assertEquals("Arial", style.getFontFamily()); // from stylesheet
    }

    @Test
    public void testComputeStyle_DefaultStylesApplied() {
        CSSStyleSheet emptyStylesheet = analyzer.parseStylesheet("");
        ComputedStyle style = analyzer.computeStyle(container, emptyStylesheet);

        assertEquals("Arial", style.getFontFamily());
        assertEquals(10f, style.getFontSize().getValue(), 0.01);
        assertEquals("block", style.getDisplay());
    }
}