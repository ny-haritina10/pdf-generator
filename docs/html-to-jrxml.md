# HTML-to-JRXML Conversion - Complete Technical Architecture

## 🏗️ **Architecture Overview**

### **Complete Processing Pipeline**
```
Visual Input → HTML Recreation → CSS Parsing → Layout Calculation → JRXML Mapping → Output Generation
```

### **Core Components Architecture**
```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   HTML Parser   │───▶│   CSS Analyzer   │───▶│  Layout Engine  │
└─────────────────┘    └──────────────────┘    └─────────────────┘
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│  DOM Structure  │    │  Style Rules     │    │ Positioned      │
│  Extraction     │    │  Processing      │    │ Elements        │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                                         │
                                                         ▼
                                           ┌─────────────────┐
                                           │ JRXML Generator │
                                           └─────────────────┘
```

## 🔧 **Technical Stack & Dependencies**

### **Java 8 Compatible Libraries**
```xml
<dependencies>
    <!-- HTML/CSS Parsing -->
    <dependency>
        <groupId>org.jsoup</groupId>
        <artifactId>jsoup</artifactId>
        <version>1.11.3</version>
    </dependency>
    
    <!-- CSS Parser -->
    <dependency>
        <groupId>net.sourceforge.cssparser</groupId>
        <artifactId>cssparser</artifactId>
        <version>0.9.25</version>
    </dependency>
    
    <!-- XML Processing -->
    <dependency>
        <groupId>dom4j</groupId>
        <artifactId>dom4j</artifactId>
        <version>1.6.1</version>
    </dependency>
    
    <!-- Template Engine -->
    <dependency>
        <groupId>org.apache.velocity</groupId>
        <artifactId>velocity</artifactId>
        <version>1.7</version>
    </dependency>
    
    <!-- JSON Processing -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.8.11</version>
    </dependency>
    
    <!-- Commons Libraries -->
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-lang3</artifactId>
        <version>3.7</version>
    </dependency>
</dependencies>
```

## 📐 **Core Architecture Components**

### **1. HTML Document Parser**
```java
public class HtmlDocumentParser {
    private static final Logger logger = LoggerFactory.getLogger(HtmlDocumentParser.class);
    
    public ParsedDocument parseHtml(String htmlContent) {
        try {
            Document doc = Jsoup.parse(htmlContent);
            
            // Extract document structure
            ParsedDocument parsedDoc = new ParsedDocument();
            parsedDoc.setTitle(extractTitle(doc));
            parsedDoc.setElements(extractElements(doc.body()));
            parsedDoc.setStyles(extractInlineStyles(doc));
            
            return parsedDoc;
            
        } catch (Exception e) {
            logger.error("Failed to parse HTML document", e);
            throw new DocumentParsingException("HTML parsing failed", e);
        }
    }
    
    private List<HtmlElement> extractElements(Element body) {
        List<HtmlElement> elements = new ArrayList<>();
        
        // Traverse DOM tree and extract meaningful elements
        body.traverse(new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                if (node instanceof Element) {
                    Element element = (Element) node;
                    if (isReportableElement(element)) {
                        elements.add(createHtmlElement(element, depth));
                    }
                }
            }
            
            @Override
            public void tail(Node node, int depth) {
                // Post-processing if needed
            }
        });
        
        return elements;
    }
    
    private boolean isReportableElement(Element element) {
        String tagName = element.tagName().toLowerCase();
        return Arrays.asList("div", "span", "p", "h1", "h2", "h3", "h4", "h5", "h6", 
                           "table", "tr", "td", "th", "img", "strong", "b", "em", "i")
                    .contains(tagName);
    }
}
```

### **2. CSS Style Analyzer**
```java
public class CssStyleAnalyzer {
    private CSSOMParser cssParser;
    private Map<String, CSSStyleRule> compiledRules;
    
    public CssStyleAnalyzer() {
        this.cssParser = new CSSOMParser();
        this.compiledRules = new HashMap<>();
    }
    
    public StyleSheet parseStylesheet(String cssContent) {
        try {
            InputSource source = new InputSource(new StringReader(cssContent));
            CSSStyleSheet stylesheet = cssParser.parseStyleSheet(source, null, null);
            
            return processStylesheet(stylesheet);
            
        } catch (IOException e) {
            throw new StyleProcessingException("Failed to parse CSS", e);
        }
    }
    
    public ComputedStyle computeStyle(HtmlElement element, StyleSheet stylesheet) {
        ComputedStyle computedStyle = new ComputedStyle();
        
        // Apply CSS cascade: user agent → author → inline styles
        applyCascade(element, stylesheet, computedStyle);
        
        return computedStyle;
    }
    
    private void applyCascade(HtmlElement element, StyleSheet stylesheet, 
                             ComputedStyle computedStyle) {
        
        // 1. Apply default browser styles
        applyDefaultStyles(element, computedStyle);
        
        // 2. Apply author stylesheet rules
        for (StyleRule rule : stylesheet.getRules()) {
            if (matchesSelector(element, rule.getSelector())) {
                applyRule(rule, computedStyle);
            }
        }
        
        // 3. Apply inline styles (highest specificity)
        applyInlineStyles(element, computedStyle);
    }
    
    private boolean matchesSelector(HtmlElement element, String selector) {
        // CSS selector matching implementation
        // Support for: tag, class, id, descendant, child selectors
        
        if (selector.startsWith("#")) {
            return selector.substring(1).equals(element.getId());
        } else if (selector.startsWith(".")) {
            return element.getClasses().contains(selector.substring(1));
        } else {
            return selector.equals(element.getTagName());
        }
    }
}
```

### **3. Layout Calculation Engine**
```java
public class LayoutCalculationEngine {
    private static final int DEFAULT_DPI = 72; // JasperReports default
    private static final double PX_TO_POINT = 0.75; // Conversion factor
    
    public LayoutResult calculateLayout(List<HtmlElement> elements, 
                                      Map<HtmlElement, ComputedStyle> styles) {
        
        LayoutResult result = new LayoutResult();
        DocumentBox documentBox = new DocumentBox(0, 0, 595, 842); // A4 default
        
        // Create layout context
        LayoutContext context = new LayoutContext(documentBox);
        
        // Process elements in document order
        for (HtmlElement element : elements) {
            ComputedStyle style = styles.get(element);
            PositionedElement positioned = calculateElementLayout(element, style, context);
            result.addElement(positioned);
        }
        
        return result;
    }
    
    private PositionedElement calculateElementLayout(HtmlElement element, 
                                                   ComputedStyle style, 
                                                   LayoutContext context) {
        
        PositionedElement positioned = new PositionedElement();
        positioned.setElement(element);
        
        // Calculate dimensions
        Dimensions dimensions = calculateDimensions(element, style, context);
        positioned.setDimensions(dimensions);
        
        // Calculate position
        Position position = calculatePosition(element, style, context, dimensions);
        positioned.setPosition(position);
        
        // Handle different positioning schemes
        switch (style.getPositionType()) {
            case STATIC:
                handleStaticPositioning(positioned, context);
                break;
            case RELATIVE:
                handleRelativePositioning(positioned, context);
                break;
            case ABSOLUTE:
                handleAbsolutePositioning(positioned, context);
                break;
            case FIXED:
                handleFixedPositioning(positioned, context);
                break;
        }
        
        return positioned;
    }
    
    private Dimensions calculateDimensions(HtmlElement element, ComputedStyle style, 
                                         LayoutContext context) {
        
        // Convert CSS units to JasperReports points
        int width = convertToPoints(style.getWidth(), context.getContainerWidth());
        int height = convertToPoints(style.getHeight(), context.getContainerHeight());
        
        // Handle auto dimensions
        if (style.getWidth().isAuto()) {
            width = calculateAutoWidth(element, style, context);
        }
        if (style.getHeight().isAuto()) {
            height = calculateAutoHeight(element, style, context);
        }
        
        // Apply box model
        Padding padding = convertPadding(style.getPadding());
        Margin margin = convertMargin(style.getMargin());
        Border border = convertBorder(style.getBorder());
        
        return new Dimensions(width, height, padding, margin, border);
    }
    
    private int convertToPoints(CssLength length, int containerSize) {
        switch (length.getUnit()) {
            case PX:
                return (int) (length.getValue() * PX_TO_POINT);
            case PT:
                return (int) length.getValue();
            case PERCENT:
                return (int) (containerSize * length.getValue() / 100);
            case EM:
                return (int) (length.getValue() * 12 * PX_TO_POINT); // Assume 12pt base
            default:
                return 0;
        }
    }
}
```

### **4. JRXML Element Mapper**
```java
public class JrxmlElementMapper {
    private static final Map<String, ElementMapper> ELEMENT_MAPPERS = new HashMap<>();
    
    static {
        ELEMENT_MAPPERS.put("div", new DivMapper());
        ELEMENT_MAPPERS.put("span", new SpanMapper());
        ELEMENT_MAPPERS.put("p", new ParagraphMapper());
        ELEMENT_MAPPERS.put("table", new TableMapper());
        ELEMENT_MAPPERS.put("img", new ImageMapper());
        ELEMENT_MAPPERS.put("h1", new HeadingMapper());
        ELEMENT_MAPPERS.put("h2", new HeadingMapper());
        ELEMENT_MAPPERS.put("h3", new HeadingMapper());
    }
    
    public JrxmlElement mapToJrxml(PositionedElement positionedElement) {
        HtmlElement htmlElement = positionedElement.getElement();
        ElementMapper mapper = ELEMENT_MAPPERS.get(htmlElement.getTagName());
        
        if (mapper == null) {
            mapper = new DefaultElementMapper();
        }
        
        return mapper.map(positionedElement);
    }
    
    // Specific mappers for different HTML elements
    public static class DivMapper implements ElementMapper {
        @Override
        public JrxmlElement map(PositionedElement positioned) {
            HtmlElement element = positioned.getElement();
            Position pos = positioned.getPosition();
            Dimensions dims = positioned.getDimensions();
            
            if (element.hasText()) {
                // Map to StaticText or TextField
                return createTextField(element, pos, dims);
            } else {
                // Map to Rectangle for styling/borders
                return createRectangle(element, pos, dims);
            }
        }
        
        private JrxmlTextField createTextField(HtmlElement element, Position pos, Dimensions dims) {
            JrxmlTextField textField = new JrxmlTextField();
            textField.setX(pos.getX());
            textField.setY(pos.getY());
            textField.setWidth(dims.getWidth());
            textField.setHeight(dims.getHeight());
            textField.setExpression(createExpression(element.getText()));
            
            // Apply font styling
            ComputedStyle style = element.getComputedStyle();
            textField.setFont(mapFont(style));
            
            return textField;
        }
    }
    
    public static class TableMapper implements ElementMapper {
        @Override
        public JrxmlElement map(PositionedElement positioned) {
            HtmlElement tableElement = positioned.getElement();
            
            // Extract table structure
            TableStructure table = parseTableStructure(tableElement);
            
            // Create JasperReports table or subreport
            if (table.isDynamic()) {
                return createSubreport(table, positioned);
            } else {
                return createStaticTable(table, positioned);
            }
        }
        
        private TableStructure parseTableStructure(HtmlElement tableElement) {
            TableStructure structure = new TableStructure();
            
            // Parse thead, tbody, tfoot
            List<HtmlElement> rows = tableElement.getChildrenByTag("tr");
            for (HtmlElement row : rows) {
                TableRow tableRow = new TableRow();
                List<HtmlElement> cells = row.getChildrenByTag("td", "th");
                
                for (HtmlElement cell : cells) {
                    TableCell tableCell = new TableCell();
                    tableCell.setContent(cell.getText());
                    tableCell.setColspan(parseIntAttribute(cell, "colspan", 1));
                    tableCell.setRowspan(parseIntAttribute(cell, "rowspan", 1));
                    tableRow.addCell(tableCell);
                }
                
                structure.addRow(tableRow);
            }
            
            return structure;
        }
    }
}
```

### **5. JRXML Document Generator**
```java
public class JrxmlDocumentGenerator {
    private VelocityEngine velocityEngine;
    private Template jrxmlTemplate;
    
    public JrxmlDocumentGenerator() {
        initializeVelocity();
        loadTemplate();
    }
    
    public String generateJrxml(List<JrxmlElement> elements, DocumentMetadata metadata) {
        VelocityContext context = new VelocityContext();
        
        // Organize elements by bands
        BandStructure bands = organizeBands(elements);
        
        // Set template variables
        context.put("metadata", metadata);
        context.put("pageHeader", bands.getPageHeader());
        context.put("columnHeader", bands.getColumnHeader());
        context.put("detail", bands.getDetail());
        context.put("columnFooter", bands.getColumnFooter());
        context.put("pageFooter", bands.getPageFooter());
        context.put("parameters", generateParameters(elements));
        context.put("fields", generateFields(elements));
        
        // Generate JRXML
        StringWriter writer = new StringWriter();
        jrxmlTemplate.merge(context, writer);
        
        return writer.toString();
    }
    
    private BandStructure organizeBands(List<JrxmlElement> elements) {
        BandStructure bands = new BandStructure();
        
        // Sort elements by Y position
        elements.sort(Comparator.comparingInt(JrxmlElement::getY));
        
        // Determine band placement based on position and content
        for (JrxmlElement element : elements) {
            BandType bandType = determineBandType(element);
            bands.addElement(bandType, element);
        }
        
        return bands;
    }
    
    private BandType determineBandType(JrxmlElement element) {
        // Logic to determine appropriate band based on:
        // - Y position (top = header, bottom = footer)
        // - Content type (repeating = detail)
        // - HTML semantic elements (header, footer tags)
        
        if (element.getY() < 100) {
            return BandType.PAGE_HEADER;
        } else if (element.getY() > 700) {
            return BandType.PAGE_FOOTER;
        } else if (element.isRepeating()) {
            return BandType.DETAIL;
        } else {
            return BandType.DETAIL; // Default
        }
    }
    
    private List<JrxmlParameter> generateParameters(List<JrxmlElement> elements) {
        Set<String> parameterNames = new HashSet<>();
        List<JrxmlParameter> parameters = new ArrayList<>();
        
        // Extract parameters from expressions
        for (JrxmlElement element : elements) {
            if (element instanceof JrxmlTextField) {
                JrxmlTextField textField = (JrxmlTextField) element;
                extractParametersFromExpression(textField.getExpression(), parameterNames);
            }
        }
        
        // Create parameter definitions
        for (String paramName : parameterNames) {
            JrxmlParameter param = new JrxmlParameter();
            param.setName(paramName);
            param.setType(inferParameterType(paramName));
            parameters.add(param);
        }
        
        return parameters;
    }
}
```

## 📋 **Data Models**

### **Core Data Structures**
```java
// HTML Element Representation
public class HtmlElement {
    private String tagName;
    private String id;
    private List<String> classes;
    private String text;
    private Map<String, String> attributes;
    private List<HtmlElement> children;
    private ComputedStyle computedStyle;
    
    // Getters and setters
}

// CSS Style Representation
public class ComputedStyle {
    private CssLength width;
    private CssLength height;
    private PositionType positionType;
    private CssLength top, right, bottom, left;
    private FontStyle font;
    private Color color;
    private Color backgroundColor;
    private BorderStyle border;
    private PaddingStyle padding;
    private MarginStyle margin;
    private TextAlign textAlign;
    private DisplayType display;
    
    // Getters and setters
}

// Layout Result
public class PositionedElement {
    private HtmlElement element;
    private Position position;
    private Dimensions dimensions;
    private ComputedStyle finalStyle;
    
    // Getters and setters
}

// JRXML Elements
public abstract class JrxmlElement {
    protected int x, y, width, height;
    protected String uuid;
    
    public abstract String toJrxml();
}

public class JrxmlTextField extends JrxmlElement {
    private String expression;
    private FontStyle font;
    private TextAlignment alignment;
    private boolean stretchWithOverflow;
    
    @Override
    public String toJrxml() {
        return String.format(
            "<textField isStretchWithOverflow=\"%s\">\n" +
            "  <reportElement x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" uuid=\"%s\"/>\n" +
            "  <textElement textAlignment=\"%s\">\n" +
            "    <font fontName=\"%s\" size=\"%d\" isBold=\"%s\"/>\n" +
            "  </textElement>\n" +
            "  <textFieldExpression><![CDATA[%s]]></textFieldExpression>\n" +
            "</textField>",
            stretchWithOverflow, x, y, width, height, uuid,
            alignment, font.getName(), font.getSize(), font.isBold(),
            expression
        );
    }
}
```

## 🔄 **Complete Processing Workflow**

### **Main Conversion Controller**
```java
@WebServlet("/html-to-jrxml")
public class HtmlToJrxmlServlet extends HttpServlet {
    private HtmlToJrxmlConverter converter;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.converter = new HtmlToJrxmlConverter();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Get HTML content from request
            String htmlContent = getHtmlContent(request);
            String cssContent = getCssContent(request);
            
            // Convert to JRXML
            ConversionRequest conversionRequest = new ConversionRequest()
                .withHtml(htmlContent)
                .withCss(cssContent)
                .withOptions(getConversionOptions(request));
            
            ConversionResult result = converter.convert(conversionRequest);
            
            if (result.isSuccess()) {
                // Return JRXML
                response.setContentType("application/xml");
                response.setHeader("Content-Disposition", 
                    "attachment; filename=\"generated.jrxml\"");
                response.getWriter().write(result.getJrxml());
            } else {
                // Return error
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().write(result.getErrorJson());
            }
            
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Internal server error\"}");
        }
    }
}

public class HtmlToJrxmlConverter {
    private HtmlDocumentParser htmlParser;
    private CssStyleAnalyzer cssAnalyzer;
    private LayoutCalculationEngine layoutEngine;
    private JrxmlElementMapper elementMapper;
    private JrxmlDocumentGenerator documentGenerator;
    
    public ConversionResult convert(ConversionRequest request) {
        try {
            // Step 1: Parse HTML
            ParsedDocument document = htmlParser.parseHtml(request.getHtml());
            
            // Step 2: Parse CSS
            StyleSheet stylesheet = cssAnalyzer.parseStylesheet(request.getCss());
            
            // Step 3: Compute styles
            Map<HtmlElement, ComputedStyle> computedStyles = new HashMap<>();
            for (HtmlElement element : document.getElements()) {
                ComputedStyle style = cssAnalyzer.computeStyle(element, stylesheet);
                computedStyles.put(element, style);
                element.setComputedStyle(style);
            }
            
            // Step 4: Calculate layout
            LayoutResult layout = layoutEngine.calculateLayout(
                document.getElements(), computedStyles);
            
            // Step 5: Map to JRXML elements
            List<JrxmlElement> jrxmlElements = new ArrayList<>();
            for (PositionedElement positioned : layout.getElements()) {
                JrxmlElement jrxmlElement = elementMapper.mapToJrxml(positioned);
                jrxmlElements.add(jrxmlElement);
            }
            
            // Step 6: Generate JRXML document
            DocumentMetadata metadata = createMetadata(request);
            String jrxml = documentGenerator.generateJrxml(jrxmlElements, metadata);
            
            // Step 7: Validate JRXML
            validateJrxml(jrxml);
            
            return ConversionResult.success(jrxml);
            
        } catch (Exception e) {
            logger.error("Conversion process failed", e);
            return ConversionResult.error(e.getMessage());
        }
    }
}
```

## 🎯 **CSS-to-JRXML Mapping Rules**

### **Typography Mapping**
```java
public class FontMapper {
    private static final Map<String, String> FONT_MAPPING = new HashMap<>();
    
    static {
        // Map web fonts to JasperReports fonts
        FONT_MAPPING.put("Arial", "Arial");
        FONT_MAPPING.put("Helvetica", "Helvetica");
        FONT_MAPPING.put("Times New Roman", "Times-Roman");
        FONT_MAPPING.put("Courier New", "Courier");
        FONT_MAPPING.put("Georgia", "Times-Roman");
        FONT_MAPPING.put("Verdana", "Arial");
        
        // Fallbacks
        FONT_MAPPING.put("sans-serif", "Arial");
        FONT_MAPPING.put("serif", "Times-Roman");
        FONT_MAPPING.put("monospace", "Courier");
    }
    
    public static String mapFont(String cssFont) {
        return FONT_MAPPING.getOrDefault(cssFont, "Arial");
    }
    
    public static int mapFontSize(CssLength fontSize) {
        // Convert CSS font-size to points
        switch (fontSize.getUnit()) {
            case PX:
                return (int) (fontSize.getValue() * 0.75);
            case PT:
                return (int) fontSize.getValue();
            case EM:
                return (int) (fontSize.getValue() * 12);
            default:
                return 10; // Default
        }
    }
}
```

### **Color Mapping**
```java
public class ColorMapper {
    public static String mapColor(CssColor cssColor) {
        if (cssColor.isRgb()) {
            return String.format("#%02X%02X%02X", 
                cssColor.getRed(), cssColor.getGreen(), cssColor.getBlue());
        } else if (cssColor.isNamed()) {
            return mapNamedColor(cssColor.getName());
        }
        return "#000000"; // Default black
    }
    
    private static String mapNamedColor(String colorName) {
        switch (colorName.toLowerCase()) {
            case "red": return "#FF0000";
            case "blue": return "#0000FF";
            case "green": return "#008000";
            case "black": return "#000000";
            case "white": return "#FFFFFF";
            default: return "#000000";
        }
    }
}
```

## 🚀 **Implementation Roadmap**

### **Phase 1: Core Infrastructure (Week 1-2)**
1. **Project Setup**
   - Maven project structure
   - Dependency configuration
   - Basic servlet framework

2. **HTML Parser Implementation**
   - JSoup integration
   - DOM extraction logic
   - Element classification

3. **Basic CSS Parser**
   - CSS-OM integration
   - Style rule extraction
   - Selector matching

### **Phase 2: Layout Engine (Week 3-4)**
1. **CSS Box Model Implementation**
   - Dimension calculation
   - Position calculation
   - Unit conversion (px, pt, em, %)

2. **Layout Algorithms**
   - Static positioning
   - Relative positioning
   - Absolute positioning
   - Float handling (basic)

### **Phase 3: JRXML Mapping (Week 5-6)**
1. **Element Mappers**
   - Text elements (div, span, p, headings)
   - Table elements (table, tr, td)
   - Image elements
   - Form elements (input, textarea)

2. **JRXML Generation**
   - Template system with Velocity
   - Band organization logic
   - Parameter/field generation

### **Phase 4: Integration & Testing (Week 7-8)**
1. **Servlet Integration**
   - File upload handling
   - Response formatting
   - Error handling

2. **Testing & Validation**
   - Unit tests for each component
   - Integration tests with sample documents
   - JRXML validation

### **Phase 5: Advanced Features (Week 9-10)**
1. **Complex Layout Support**
   - Flexbox simulation
   - Grid simulation (basic)
   - Multi-column layouts

2. **Enhanced Styling**
   - Border support
   - Background colors/images
   - Advanced typography

## ⚠️ **Limitations & Challenges**

### **CSS Feature Limitations**
- **Not Supported**: CSS Grid, Flexbox, Complex selectors, CSS animations
- **Partial Support**: Floats, Positioning, Z-index
- **Full Support**: Basic box model, Typography, Colors, Borders

### **HTML Element Limitations**
- **Not Supported**: Form elements, Media elements, Canvas
- **Partial Support**: Tables (complex structures), Lists
- **Full Support**: Text elements, Images, Basic containers

### **JasperReports Constraints**
- **Fixed Layout**: No dynamic resizing like web browsers
- **Limited Fonts**: Only embedded fonts available
- **Band Structure**: Must fit JasperReports band model

## 🔧 **Configuration & Tuning**

### **Conversion Options**
```java
public class ConversionOptions {
    private int pageWidth = 595;  // A4 width in points
    private int pageHeight = 842; // A4 height in points
    private int marginTop = 36;
    private int marginBottom = 36;
    private int marginLeft = 36;
    private int marginRight = 36;
    private String defaultFont = "Arial";
    private int defaultFontSize = 10;
    private boolean preserveColors = true;
    private boolean optimizeLayout = true;
    private Map<String, String> fontMappings = new HashMap<>();
    
    // Getters and setters
}
```

### **Quality Optimization**
```java
public class LayoutOptimizer {
    public void optimizeLayout(List<JrxmlElement> elements) {
        // Remove overlapping elements
        removeOverlaps(elements);
        
        // Align elements to grid
        alignToGrid(elements);
        
        // Optimize band heights
        optimizeBandHeights(elements);
        
        // Merge similar elements
        mergeSimilarElements(elements);
    }
}
```

This complete architecture provides a robust foundation for HTML-to-JRXML conversion while working within the constraints of Java 8 and JasperReports. The modular design allows for incremental implementation and future enhancements.
