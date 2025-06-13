package mg.bici.htmltojrxml.exceptions;

/**
 * Exception for HTML/CSS parsing errors.
 */
public class ParsingException extends ConversionException {
    
    public ParsingException(String message) {
        super(message, ErrorCode.PARSING_ERROR);
    }

    public ParsingException(String message, Throwable cause) {
        super(message, ErrorCode.PARSING_ERROR, cause);
    }
}