package mg.bici.htmltojrxml.css;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;

import mg.bici.htmltojrxml.exceptions.ParsingException;
import mg.bici.htmltojrxml.html.HtmlElement;
import mg.bici.htmltojrxml.utils.Color;
import mg.bici.htmltojrxml.utils.ColorUtils;

/**
 * Analyzes CSS styles and computes styles for HTML elements.
 */
public class CssStyleAnalyzer {
    private final SelectorMatcher selectorMatcher;

    public CssStyleAnalyzer() {
        this.selectorMatcher = new SelectorMatcher();
    }

    /**
     * Parses CSS content into a stylesheet.
     *
     * @param cssContent the CSS content
     * @return parsed CSS stylesheet
     */
    public CSSStyleSheet parseStylesheet(String cssContent) {
        try {
            CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
            parser.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(CSSParseException e) throws CSSParseException {}
                @Override
                public void error(CSSParseException e) throws CSSParseException {
                    throw new ParsingException("CSS parsing error: " + e.getMessage(), e);
                }
                @Override
                public void fatalError(CSSParseException e) throws CSSParseException {
                    throw new ParsingException("Fatal CSS parsing error: " + e.getMessage(), e);
                }
            });
            InputSource source = new InputSource(new StringReader(cssContent));
            return parser.parseStyleSheet(source, null, null);
        } catch (Exception e) {
            throw new ParsingException("Failed to parse CSS content", e);
        }
    }

    /**
     * Computes the final style for an HTML element.
     *
     * @param element    the HTML element
     * @param stylesheet the CSS stylesheet
     * @return computed style
     */
    public ComputedStyle computeStyle(HtmlElement element, CSSStyleSheet stylesheet) {
        ComputedStyle result = new ComputedStyle();
        applyDefaultStyles(element, result);
        applyAuthorStyles(element, stylesheet, result);
        applyInlineStyles(element, result);
        return result;
    }

    /**
     * Applies cascading rules to compute styles.
     *
     * @param element    the HTML element
     * @param stylesheet the CSS stylesheet
     * @param result     the computed style
     */
    public void applyCascade(HtmlElement element, CSSStyleSheet stylesheet, ComputedStyle result) {
        // cascade is handled by applying styles in order: default, author, inline
        applyDefaultStyles(element, result);
        applyAuthorStyles(element, stylesheet, result);
        applyInlineStyles(element, result);
    }

    // applies default styles based on element type
    private void applyDefaultStyles(HtmlElement element, ComputedStyle result) {
        // set basic defaults
        result.setFontFamily("Arial");
        result.setFontSize(new CssLength(10, CssLength.Unit.PT));
        result.setColor(new Color(0, 0, 0)); // black
        if (element.isBlock()) {
            result.setDisplay("block");
        } else if (element.isInline()) {
            result.setDisplay("inline");
        }
    }

    // applies styles from stylesheet
    private void applyAuthorStyles(HtmlElement element, CSSStyleSheet stylesheet, ComputedStyle result) {
        List<StyleRule> matchingRules = getMatchingRules(element, stylesheet);
        for (StyleRule rule : matchingRules) {
            CSSStyleDeclaration declaration = rule.getDeclaration();
            for (int i = 0; i < declaration.getLength(); i++) {
                String property = declaration.item(i);
                String value = declaration.getPropertyValue(property);
                applyStyleProperty(result, property, value);
            }
        }
    }

    // applies inline styles from element
    private void applyInlineStyles(HtmlElement element, ComputedStyle result) {
        for (Map.Entry<String, String> entry : element.getInlineStyles().entrySet()) {
            applyStyleProperty(result, entry.getKey(), entry.getValue());
        }
    }

    // applies a single style property
    private void applyStyleProperty(ComputedStyle style, String property, String value) {
        switch (property.toLowerCase()) {
            case "font-family":
                style.setFontFamily(value);
                break;
            case "font-size":
                style.setFontSize(parseCssLength(value));
                break;
            case "color":
                style.setColor(ColorUtils.parseColor(value));
                break;
            case "width":
                style.setWidth(parseCssLength(value));
                break;
            case "height":
                style.setHeight(parseCssLength(value));
                break;
            case "margin":
                style.setMargin(parseBoxModel(value));
                break;
            case "padding":
                style.setPadding(parsePadding(value));
                break;
            case "font-weight":
                style.setFontWeight(parseFontWeight(value));
                break;
            case "text-align":
                style.setTextAlign(TextAlign.fromString(value));
                break;
            // add more properties as needed
        }
    }

    private FontWeight parseFontWeight(String value) {
        switch (value.toLowerCase()) {
            case "bold":
            case "bolder":
                return FontWeight.BOLD;
            case "lighter":
                return FontWeight.LIGHTER;
            default:
                return FontWeight.NORMAL;
        }
    }

    // parses CSS length (e.g., "12px" -> CssLength)
    private CssLength parseCssLength(String value) {
        try {
            String cleaned = value.replaceAll("[^0-9.]", "");
            float number = Float.parseFloat(cleaned);
            if (value.contains("px")) {
                return new CssLength(number, CssLength.Unit.PX);
            } else if (value.contains("pt")) {
                return new CssLength(number, CssLength.Unit.PT);
            }
            return new CssLength(number, CssLength.Unit.PX); // default to pixels
        } catch (NumberFormatException e) {
            return new CssLength(0, CssLength.Unit.PX);
        }
    }

    // parses box model values (e.g., "10px 5px" -> Margin)
    private Margin parseBoxModel(String value) {
        String[] parts = value.trim().split("\\s+");
        float top = 0, right = 0, bottom = 0, left = 0;
        if (parts.length == 1) {
            top = right = bottom = left = parseCssLength(parts[0]).getValue();
        } else if (parts.length == 2) {
            top = bottom = parseCssLength(parts[0]).getValue();
            right = left = parseCssLength(parts[1]).getValue();
        } else if (parts.length == 4) {
            top = parseCssLength(parts[0]).getValue();
            right = parseCssLength(parts[1]).getValue();
            bottom = parseCssLength(parts[2]).getValue();
            left = parseCssLength(parts[3]).getValue();
        }
        return new Margin(top, right, bottom, left);
    }

    // parses padding values (e.g., "10px 5px" -> Padding)
    private Padding parsePadding(String value) {
        String[] parts = value.trim().split("\\s+");
        float top = 0, right = 0, bottom = 0, left = 0;
        if (parts.length == 1) {
            top = right = bottom = left = parseCssLength(parts[0]).getValue();
        } else if (parts.length == 2) {
            top = bottom = parseCssLength(parts[0]).getValue();
            right = left = parseCssLength(parts[1]).getValue();
        } else if (parts.length == 4) {
            top = parseCssLength(parts[0]).getValue();
            right = parseCssLength(parts[1]).getValue();
            bottom = parseCssLength(parts[2]).getValue();
            left = parseCssLength(parts[3]).getValue();
        }
        return new Padding(top, right, bottom, left);
    }
    
    // gets matching CSS rules for an element
    private List<StyleRule> getMatchingRules(HtmlElement element, CSSStyleSheet stylesheet) {
        List<StyleRule> matchingRules = new ArrayList<>();
        CSSRuleList ruleList = stylesheet.getCssRules();
        for (int i = 0; i < ruleList.getLength(); i++) {
            if (ruleList.item(i) instanceof CSSStyleRule) {
                CSSStyleRule cssRule = (CSSStyleRule) ruleList.item(i);
                String selector = cssRule.getSelectorText();
                if (selectorMatcher.matches(element, selector)) {
                    matchingRules.add(new StyleRule(selector, cssRule.getStyle()));
                }
            }
        }
        // sort by specificity (simplified)
        matchingRules.sort((a, b) -> calculateSpecificity(b.getSelector()) - calculateSpecificity(a.getSelector()));
        return matchingRules;
    }

    // calculates selector specificity (simplified)
    private int calculateSpecificity(String selector) {
        int specificity = 0;
        if (selector.contains("#")) specificity += 100; // ID
        if (selector.contains(".")) specificity += 10;  // Class
        if (selector.matches("[a-zA-Z]+")) specificity += 1; // Element
        return specificity;
    }
}

/**
 * Represents a CSS style rule.
 */
class StyleRule {
    private final String selector;
    private final CSSStyleDeclaration declaration;

    public StyleRule(String selector, CSSStyleDeclaration declaration) {
        this.selector = selector;
        this.declaration = declaration;
    }

    public String getSelector() {
        return selector;
    }

    public CSSStyleDeclaration getDeclaration() {
        return declaration;
    }
}