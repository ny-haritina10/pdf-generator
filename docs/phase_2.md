**Phase 2: HTML Processing** is critical for parsing HTML content to create a structured model for JRXML conversion in your Java 8, JSP/Servlet-based PDF generation system. Below are the main points to achieve the goal of semi-automated `.jrxml` template creation from HTML/CSS, with brief explanations:

1. **HTML Parsing (HtmlDocumentParser.java)**:
   - **Purpose**: Parse HTML input (e.g., payslip or invoice mockups) into a structured format.
   - **Key Task**: Use JSoup to extract elements, inline styles, and build a tree of `HtmlElement` objects, filtering out non-reportable tags (e.g., `<script>`).
   - **Why It Matters**: Provides a clean, hierarchical representation of HTML for further processing into JRXML.

2. **Element Modeling (HtmlElement.java)**:
   - **Purpose**: Represent each HTML element with its properties (tag, ID, classes, text, attributes, inline styles) and relationships (parent/children).
   - **Key Task**: Implement methods to query element properties (e.g., `hasText()`, `isBlock()`) and hierarchy.
   - **Why It Matters**: Enables precise mapping of HTML elements to JRXML components like text fields or tables.

3. **Element Creation (HtmlElementFactory.java)**:
   - **Purpose**: Standardize creation of `HtmlElement` instances from JSoup elements or other sources.
   - **Key Task**: Provide factory methods to create elements (e.g., from JSoup, text-only, or containers).
   - **Why It Matters**: Ensures consistent element construction, simplifying parsing and mapping logic.

4. **Testing and Validation**:
   - **Purpose**: Verify parsing accuracy and element integrity.
   - **Key Task**: Write JUnit tests to check parsing, inline style extraction, and element properties, handling edge cases like invalid HTML.
   - **Why It Matters**: Guarantees reliability of the HTML processing layer before integrating with CSS and JRXML phases.

**Overall Goal**: Build a robust HTML processing layer that accurately extracts and models HTML content, laying the foundation for CSS style application (Phase 3) and JRXML mapping (Phase 5) to automate `.jrxml` template generation for PDF reports.