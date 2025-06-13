# HTML-to-JRXML Complete Implementation Guide

## 📋 **Implementation Checklist**

### **Phase 1: Project Setup & Infrastructure**
- [ ] Create Maven project structure
- [ ] Configure dependencies
- [ ] Set up logging framework
- [ ] Create configuration properties
- [ ] Implement exception handling classes

### **Phase 2: HTML Processing Layer**
- [ ] Implement HTML document parser
- [ ] Create HTML element models
- [ ] Build DOM tree traversal
- [ ] Add inline style extraction
- [ ] Implement element validation

### **Phase 3: CSS Processing Layer**
- [ ] Implement CSS parser integration
- [ ] Create CSS rule models
- [ ] Build selector matching engine
- [ ] Implement style computation
- [ ] Add CSS cascade logic

### **Phase 4: Layout Calculation Engine**
- [ ] Implement box model calculations
- [ ] Create positioning algorithms
- [ ] Build dimension calculations
- [ ] Add unit conversion utilities
- [ ] Implement layout optimization

### **Phase 5: JRXML Mapping Layer**
- [ ] Create JRXML element models
- [ ] Implement element mappers
- [ ] Build template system
- [ ] Add parameter generation
- [ ] Implement validation

### **Phase 6: Integration & Testing**
- [ ] Create servlet interface
- [ ] Implement file handling
- [ ] Add error handling
- [ ] Create comprehensive tests
- [ ] Performance optimization

## 🏗️ **Project Structure**

```
mg/bici/htmltojrxml/
├── core/
│   ├── HtmlToJrxmlConverter.java
│   ├── ConversionRequest.java
│   ├── ConversionResult.java
│   └── ConversionOptions.java
├── html/
│   ├── HtmlDocumentParser.java
│   ├── HtmlElement.java
│   ├── HtmlElementFactory.java
│   └── InlineStyleExtractor.java
├── css/
│   ├── CssStyleAnalyzer.java
│   ├── ComputedStyle.java
│   ├── StyleRule.java
│   ├── SelectorMatcher.java
│   └── CssPropertyMapper.java
├── layout/
│   ├── LayoutCalculationEngine.java
│   ├── PositionedElement.java
│   ├── Dimensions.java
│   ├── Position.java
│   ├── BoxModel.java
│   └── UnitConverter.java
├── jrxml/
│   ├── JrxmlElementMapper.java
│   ├── JrxmlElement.java (abstract)
│   ├── JrxmlTextField.java
│   ├── JrxmlStaticText.java
│   ├── JrxmlTable.java
│   ├── JrxmlDocumentGenerator.java
│   └── mappers/
│       ├── TextElementMapper.java
│       ├── TableElementMapper.java
│       ├── ImageElementMapper.java
│       └── ContainerElementMapper.java
├── utils/
│   ├── ColorUtils.java
│   ├── FontUtils.java
│   ├── ValidationUtils.java
│   └── XmlUtils.java
├── exceptions/
│   ├── ConversionException.java
│   ├── ParsingException.java
│   ├── LayoutException.java
│   └── ValidationException.java
├── config/
│   ├── ConversionConfig.java
│   ├── FontMappingConfig.java
│   └── ElementMappingConfig.java
└── web/
    ├── HtmlToJrxmlServlet.java
    ├── FileUploadHandler.java
    └── ResponseFormatter.java

src/main/resources/
├── templates/
│   ├── jrxml-template.vm
│   ├── band-template.vm
│   └── element-templates.vm
├── config/
│   ├── font-mappings.properties
│   ├── element-mappings.properties
│   └── conversion-config.properties
└── samples/
    ├── sample-payslip.html
    ├── sample-invoice.html
    └── sample-styles.css

```

## 📝 **Implementation Task List**


** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **

### **Phase 1: Infrastructure Setup (Week 1)**

#### **1.1 Maven Project Setup**
**Task**: Create `pom.xml` with required dependencies
```xml
<!-- Key dependencies to add -->
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.11.3</version>
</dependency>
<dependency>
    <groupId>net.sourceforge.cssparser</groupId>
    <artifactId>cssparser</artifactId>
    <version>0.9.25</version>
</dependency>
```

#### **1.2 Configuration Classes**
**Class**: `ConversionConfig.java`
**Methods to implement**:
- `loadConfiguration()`
- `getPageDimensions()`
- `getDefaultFont()`
- `getMargins()`

**Class**: `FontMappingConfig.java`
**Methods to implement**:
- `loadFontMappings()`
- `mapCssFontToJasper(String cssFont)`
- `getAvailableFonts()`

#### **1.3 Exception Handling**
**Classes to create**:
- `ConversionException.java` - Base exception
- `ParsingException.java` - HTML/CSS parsing errors
- `LayoutException.java` - Layout calculation errors
- `ValidationException.java` - JRXML validation errors

**Each exception class needs**:
- Constructor with message
- Constructor with message and cause
- Error code enumeration

** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **

### **Phase 2: HTML Processing (Week 2)**

#### **2.1 HTML Document Parser**
**Class**: `HtmlDocumentParser.java`
**Methods to implement**:
```java
// Core parsing methods
public ParsedDocument parseHtml(String htmlContent)
public List<HtmlElement> extractElements(Element body)
public Map<String, String> extractInlineStyles(Document doc)
public boolean isReportableElement(Element element)

// Utility methods
private HtmlElement createHtmlElement(Element element, int depth)
private String extractText(Element element)
private Map<String, String> extractAttributes(Element element)
```

#### **2.2 HTML Element Model**
**Class**: `HtmlElement.java`
**Properties to implement**:
```java
private String tagName;
private String id;
private List<String> classes;
private String text;
private Map<String, String> attributes;
private List<HtmlElement> children;
private HtmlElement parent;
private Map<String, String> inlineStyles;
```

**Methods to implement**:
```java
public boolean hasText()
public boolean hasChildren()
public List<HtmlElement> getChildrenByTag(String... tags)
public String getAttribute(String name, String defaultValue)
public boolean hasClass(String className)
public boolean isBlock()
public boolean isInline()
```

#### **2.3 HTML Element Factory**
**Class**: `HtmlElementFactory.java`
**Methods to implement**:
```java
public static HtmlElement createFromJsoupElement(Element element)
public static HtmlElement createTextElement(String text)
public static HtmlElement createContainerElement(String tagName)
public static List<HtmlElement> createFromNodeList(Elements elements)
```

** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **

### **Phase 3: CSS Processing (Week 3)**

#### **3.1 CSS Style Analyzer**
**Class**: `CssStyleAnalyzer.java`
**Methods to implement**:
```java
// Main processing methods
public StyleSheet parseStylesheet(String cssContent)
public ComputedStyle computeStyle(HtmlElement element, StyleSheet stylesheet)
public void applyCascade(HtmlElement element, StyleSheet stylesheet, ComputedStyle result)

// Style application methods
private void applyDefaultStyles(HtmlElement element, ComputedStyle result)
private void applyAuthorStyles(HtmlElement element, StyleSheet stylesheet, ComputedStyle result)
private void applyInlineStyles(HtmlElement element, ComputedStyle result)

// Utility methods
private List<StyleRule> getMatchingRules(HtmlElement element, StyleSheet stylesheet)
private int calculateSpecificity(String selector)
```

#### **3.2 Computed Style Model**
**Class**: `ComputedStyle.java`
**Properties to implement**:
```java
// Dimensions
private CssLength width;
private CssLength height;
private CssLength minWidth;
private CssLength maxWidth;

// Positioning
private PositionType position;
private CssLength top, right, bottom, left;
private CssLength zIndex;

// Typography
private String fontFamily;
private CssLength fontSize;
private FontWeight fontWeight;
private FontStyle fontStyle;
private Color color;

// Box model
private Padding padding;
private Margin margin;
private Border border;
private Color backgroundColor;

// Text
private TextAlign textAlign;
private TextDecoration textDecoration;
private CssLength lineHeight;
```

#### **3.3 Selector Matcher**
**Class**: `SelectorMatcher.java`
**Methods to implement**:
```java
public boolean matches(HtmlElement element, String selector)
public boolean matchesSimpleSelector(HtmlElement element, String selector)
public boolean matchesDescendantSelector(HtmlElement element, String selector)
public boolean matchesChildSelector(HtmlElement element, String selector)
public boolean matchesClassSelector(HtmlElement element, String className)
public boolean matchesIdSelector(HtmlElement element, String id)
public boolean matchesAttributeSelector(HtmlElement element, String attribute)
```

** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **

### **Phase 4: Layout Calculation (Week 4)**

#### **4.1 Layout Calculation Engine**
**Class**: `LayoutCalculationEngine.java`
**Methods to implement**:
```java
// Main calculation methods
public LayoutResult calculateLayout(List<HtmlElement> elements, Map<HtmlElement, ComputedStyle> styles)
public PositionedElement calculateElementLayout(HtmlElement element, ComputedStyle style, LayoutContext context)

// Dimension calculations
private Dimensions calculateDimensions(HtmlElement element, ComputedStyle style, LayoutContext context)
private int calculateAutoWidth(HtmlElement element, ComputedStyle style, LayoutContext context)
private int calculateAutoHeight(HtmlElement element, ComputedStyle style, LayoutContext context)

// Position calculations
private Position calculatePosition(HtmlElement element, ComputedStyle style, LayoutContext context)
private void handleStaticPositioning(PositionedElement element, LayoutContext context)
private void handleRelativePositioning(PositionedElement element, LayoutContext context)
private void handleAbsolutePositioning(PositionedElement element, LayoutContext context)

// Layout optimization
public void optimizeLayout(List<PositionedElement> elements)
private void removeOverlappingElements(List<PositionedElement> elements)
private void alignToGrid(List<PositionedElement> elements)
```

#### **4.2 Unit Converter**
**Class**: `UnitConverter.java`
**Methods to implement**:
```java
public static int convertToPoints(CssLength length, int containerSize, int fontSize)
public static int pxToPoints(double pixels)
public static int emToPoints(double em, int fontSize)
public static int percentToPoints(double percent, int containerSize)
public static int cmToPoints(double cm)
public static int mmToPoints(double mm)
public static int inchToPoints(double inches)
```

#### **4.3 Box Model Calculator**
**Class**: `BoxModel.java`
**Methods to implement**:
```java
public int getContentWidth()
public int getContentHeight()
public int getPaddingBoxWidth()
public int getPaddingBoxHeight()
public int getBorderBoxWidth()
public int getBorderBoxHeight()
public int getMarginBoxWidth()
public int getMarginBoxHeight()
public Rectangle getContentBox()
public Rectangle getPaddingBox()
public Rectangle getBorderBox()
public Rectangle getMarginBox()
```

** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **

### **Phase 5: JRXML Mapping (Week 5)**

#### **5.1 JRXML Element Mapper**
**Class**: `JrxmlElementMapper.java`
**Methods to implement**:
```java
public JrxmlElement mapToJrxml(PositionedElement positionedElement)
public List<JrxmlElement> mapElements(List<PositionedElement> elements)
private ElementMapper getMapper(String elementType)
private JrxmlElement createFallbackElement(PositionedElement element)
```

#### **5.2 Individual Element Mappers**

**Class**: `TextElementMapper.java`
**Methods to implement**:
```java
public JrxmlElement map(PositionedElement positioned)
private JrxmlTextField createTextField(HtmlElement element, Position pos, Dimensions dims)
private JrxmlStaticText createStaticText(HtmlElement element, Position pos, Dimensions dims)
private String determineExpression(HtmlElement element)
private FontStyle mapFontStyle(ComputedStyle style)
```

**Class**: `TableElementMapper.java`
**Methods to implement**:
```java
public JrxmlElement map(PositionedElement positioned)
private TableStructure parseTableStructure(HtmlElement tableElement)
private JrxmlTable createStaticTable(TableStructure structure, PositionedElement positioned)
private JrxmlSubreport createDynamicTable(TableStructure structure, PositionedElement positioned)
private List<JrxmlTableColumn> createColumns(TableStructure structure)
private List<JrxmlTableRow> createRows(TableStructure structure)
```

**Class**: `ImageElementMapper.java`
**Methods to implement**:
```java
public JrxmlElement map(PositionedElement positioned)
private JrxmlImage createImage(HtmlElement element, Position pos, Dimensions dims)
private String resolveImagePath(String src)
private ScaleType determineScaleType(ComputedStyle style)
```

#### **5.3 JRXML Element Classes**

**Abstract Class**: `JrxmlElement.java`
**Properties and methods**:
```java
// Common properties
protected int x, y, width, height;
protected String uuid;
protected Color foreColor;
protected Color backColor;

// Abstract methods
public abstract String toJrxml();
public abstract String getElementType();

// Common methods
public void setPosition(int x, int y)
public void setDimensions(int width, int height)
public String generateUuid()
protected String formatColor(Color color)
```

**Class**: `JrxmlTextField.java`
**Properties and methods**:
```java
// Properties
private String expression;
private FontStyle font;
private TextAlignment alignment;
private boolean stretchWithOverflow;
private String pattern;

// Methods
public String toJrxml()
public void setExpression(String expression)
public void setFont(FontStyle font)
public void setAlignment(TextAlignment alignment)
```

**Class**: `JrxmlStaticText.java`
**Properties and methods**:
```java
// Properties
private String text;
private FontStyle font;
private TextAlignment alignment;

// Methods
public String toJrxml()
public void setText(String text)
public void setFont(FontStyle font)
```

**Class**: `JrxmlTable.java`
**Properties and methods**:
```java
// Properties
private List<JrxmlTableColumn> columns;
private List<JrxmlTableRow> rows;
private String datasetName;

// Methods
public String toJrxml()
public void addColumn(JrxmlTableColumn column)
public void addRow(JrxmlTableRow row)
public void setDataset(String datasetName)
```

** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **

### **Phase 6: Document Generation (Week 6)**

#### **6.1 JRXML Document Generator**
**Class**: `JrxmlDocumentGenerator.java`
**Methods to implement**:
```java
// Main generation methods
public String generateJrxml(List<JrxmlElement> elements, DocumentMetadata metadata)
public String generateJrxmlFromTemplate(Map<String, Object> templateData)

// Band organization
private BandStructure organizeBands(List<JrxmlElement> elements)
private BandType determineBandType(JrxmlElement element)
private void optimizeBandHeights(BandStructure bands)

// Parameter and field generation
private List<JrxmlParameter> generateParameters(List<JrxmlElement> elements)
private List<JrxmlField> generateFields(List<JrxmlElement> elements)
private void extractParametersFromExpression(String expression, Set<String> parameters)

// Template processing
private void initializeVelocityEngine()
private VelocityContext createTemplateContext(List<JrxmlElement> elements, DocumentMetadata metadata)
```

#### **6.2 Band Structure Manager**
**Class**: `BandStructure.java`
**Methods to implement**:
```java
public void addElement(BandType bandType, JrxmlElement element)
public List<JrxmlElement> getElementsForBand(BandType bandType)
public int calculateBandHeight(BandType bandType)
public void optimizeLayout()
public Map<BandType, Integer> getBandHeights()
```

** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **

### **Phase 7: Web Integration (Week 7)**

#### **7.1 Main Servlet**
**Class**: `HtmlToJrxmlServlet.java`
**Methods to implement**:
```java
// Servlet lifecycle
public void init() throws ServletException
public void destroy()

// Request handling
protected void doPost(HttpServletRequest request, HttpServletResponse response)
protected void doGet(HttpServletRequest request, HttpServletResponse response)

// Request processing
private ConversionRequest parseRequest(HttpServletRequest request)
private String getHtmlContent(HttpServletRequest request)
private String getCssContent(HttpServletRequest request)
private ConversionOptions getConversionOptions(HttpServletRequest request)

// Response handling
private void sendSuccessResponse(HttpServletResponse response, String jrxml)
private void sendErrorResponse(HttpServletResponse response, String error)
private void sendValidationResponse(HttpServletResponse response, ConversionResult result)
```

#### **7.2 File Upload Handler**
**Class**: `FileUploadHandler.java`
**Methods to implement**:
```java
public UploadResult handleFileUpload(HttpServletRequest request)
public String extractHtmlFromFile(Part filePart)
public String extractCssFromFile(Part filePart)
public boolean isValidFileType(String contentType)
public void validateFileSize(long fileSize)
public void cleanupTempFiles()
```

#### **7.3 Response Formatter**
**Class**: `ResponseFormatter.java`
**Methods to implement**:
```java
public String formatSuccessResponse(String jrxml)
public String formatErrorResponse(String error, String details)
public String formatValidationResponse(List<ValidationError> errors)
public void setDownloadHeaders(HttpServletResponse response, String filename)
public String escapeJsonString(String input)
```

** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **

### **Phase 8: Utility Classes (Ongoing)**

#### **8.1 Color Utilities**
**Class**: `ColorUtils.java`
**Methods to implement**:
```java
public static Color parseColor(String colorString)
public static String colorToHex(Color color)
public static Color rgbToColor(int r, int g, int b)
public static Color hslToColor(float h, float s, float l)
public static boolean isValidColorString(String colorString)
public static Map<String, Color> getNamedColors()
```

#### **8.2 Font Utilities**
**Class**: `FontUtils.java`
**Methods to implement**:
```java
public static String mapCssFontToJasper(String cssFont)
public static int mapFontSize(CssLength fontSize, int parentFontSize)
public static FontWeight mapFontWeight(String cssWeight)
public static FontStyle mapFontStyle(String cssStyle)
public static List<String> getAvailableFonts()
public static boolean isFontAvailable(String fontName)
```

#### **8.3 Validation Utilities**
**Class**: `ValidationUtils.java`
**Methods to implement**:
```java
public static boolean isValidJrxml(String jrxml)
public static List<ValidationError> validateJrxml(String jrxml)
public static boolean isValidExpression(String expression)
public static boolean isValidDimension(int value)
public static boolean isValidPosition(int x, int y)
public static void validateConversionRequest(ConversionRequest request)
```

#### **8.4 XML Utilities**
**Class**: `XmlUtils.java`
**Methods to implement**:
```java
public static String escapeXml(String input)
public static String formatXml(String xml)
public static boolean isWellFormedXml(String xml)
public static Document parseXmlString(String xml)
public static String nodeToString(Node node)
public static void validateXmlAgainstSchema(String xml, String schemaPath)
```

** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **


## 📊 **Implementation Progress Tracking**

### **Weekly Milestones**

**Week 1 Deliverables**:
- [ ] Complete project structure
- [ ] All configuration classes implemented
- [ ] Basic exception handling
- [ ] Initial unit test framework
- [ ] Maven build working

**Week 2 Deliverables**:
- [ ] HtmlDocumentParser fully functional
- [ ] HtmlElement model complete
- [ ] Basic HTML parsing working
- [ ] Unit tests for HTML parsing
- [ ] Sample HTML files processed

**Week 3 Deliverables**:
- [ ] CssStyleAnalyzer implemented
- [ ] ComputedStyle model complete
- [ ] Basic CSS parsing working
- [ ] Style computation functional
- [ ] CSS cascade logic working

**Week 4 Deliverables**:
- [ ] LayoutCalculationEngine implemented
- [ ] All positioning algorithms working
- [ ] Unit conversion complete
- [ ] Layout optimization functional
- [ ] Basic layout calculations working

**Week 5 Deliverables**:
- [ ] All JRXML element mappers implemented
- [ ] JRXML element models complete
- [ ] Basic mapping functionality working
- [ ] Element-specific mapping logic
- [ ] Mapping validation working

**Week 6 Deliverables**:
- [ ] JrxmlDocumentGenerator complete
- [ ] Template system working
- [ ] Band organization functional
- [ ] Parameter generation working
- [ ] Complete JRXML output

**Week 7 Deliverables**:
- [ ] Servlet implementation complete
- [ ] File upload handling working
- [ ] Web interface functional
- [ ] Error handling complete
- [ ] Basic web integration working

**Week 8 Deliverables**:
- [ ] All unit tests implemented
- [ ] Integration tests complete
- [ ] Performance tests running
- [ ] Documentation complete
- [ ] System ready for deployment

** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **

## 🔧 **Configuration Files to Create**

### **font-mappings.properties**
```properties
# CSS Font to JasperReports Font Mapping
Arial=Arial
Helvetica=Helvetica
Times\ New\ Roman=Times-Roman
Courier\ New=Courier
Georgia=Times-Roman
Verdana=Arial
Tahoma=Arial
sans-serif=Arial
serif=Times-Roman
monospace=Courier
```

### **element-mappings.properties**
```properties
# HTML Element to JRXML Element Mapping
div=container
span=textfield
p=textfield
h1=textfield
h2=textfield
h3=textfield
h4=textfield
h5=textfield
h6=textfield
table=table
tr=tablerow
td=tablecell
th=tablecell
img=image
strong=textfield
b=textfield
em=textfield
i=textfield
```

### **conversion-config.properties**
```properties
# Page Configuration
page.width=595
page.height=842
page.orientation=portrait
page.margin.top=36
page.margin.bottom=36
page.margin.left=36
page.margin.right=36

# Font Configuration
font.default.family=Arial
font.default.size=10
font.default.encoding=UTF-8

# Layout Configuration
layout.grid.size=5
layout.optimize=true
layout.remove.overlaps=true
layout.align.elements=true

# Conversion Options
conversion.preserve.colors=true
conversion.preserve.fonts=true
conversion.strict.mode=false
conversion.validate.output=true
```

** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **
** || ========================================================================================== || **

## 🎯 **Quality Assurance Checklist**

### **Code Quality**
- [ ] All classes have proper JavaDoc documentation
- [ ] All methods have parameter validation
- [ ] Proper exception handling throughout
- [ ] Logging implemented at appropriate levels
- [ ] Code follows Java naming conventions
- [ ] No hardcoded values (use configuration)

### **Testing Coverage**
- [ ] Unit tests cover all public methods
- [ ] Integration tests cover main workflows
- [ ] Performance tests for large documents
- [ ] Error handling tests for edge cases
- [ ] Memory leak tests for long-running processes

### **Documentation**
- [ ] README with setup instructions
- [ ] API documentation for public interfaces
- [ ] Configuration guide
- [ ] Troubleshooting guide
- [ ] Sample code and examples

### **Performance Requirements**
- [ ] Process 1-page document in < 2 seconds
- [ ] Process 10-page document in < 10 seconds
- [ ] Memory usage < 100MB for typical documents
- [ ] Handle concurrent requests efficiently
- [ ] Graceful degradation for complex layouts

This comprehensive implementation guide provides a complete roadmap for building the HTML-to-JRXML conversion system. Each phase builds upon the previous one, ensuring steady progress toward a fully functional solution.