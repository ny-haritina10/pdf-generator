package mg.bici.htmltojrxml.css;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import mg.bici.htmltojrxml.html.HtmlElement;

public class SelectorMatcherTest {
    private SelectorMatcher matcher;
    private HtmlElement container;
    private HtmlElement title;
    private HtmlElement span;

    @Before
    public void setUp() {
        matcher = new SelectorMatcher();

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
    public void testMatches_IdSelector() {
        assertTrue(matcher.matches(container, "#container"));
        assertFalse(matcher.matches(title, "#container"));
    }

    @Test
    public void testMatches_ClassSelector() {
        assertTrue(matcher.matches(title, ".title"));
        assertFalse(matcher.matches(container, ".title"));
    }

    @Test
    public void testMatches_TagSelector() {
        assertTrue(matcher.matches(container, "div"));
        assertTrue(matcher.matches(title, "h1"));
        assertFalse(matcher.matches(title, "div"));
    }

    @Test
    public void testMatches_DescendantSelector() {
        assertTrue(matcher.matches(span, "div p span"));
        assertFalse(matcher.matches(span, "div h1 span"));
    }

    @Test
    public void testMatches_ChildSelector() {
        assertTrue(matcher.matches(span, "p > span"));
        assertFalse(matcher.matches(span, "div > span"));
    }

    @Test
    public void testMatches_AttributeSelector() {
        assertTrue(matcher.matches(span.getParent(), "[data-test]"));
        assertFalse(matcher.matches(title, "[data-test]"));
    }
}