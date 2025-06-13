package mg.bici.htmltojrxml.exceptions;

/**
 * Base exception for HTML-to-JRXML conversion errors.
 */
public class ConversionException extends RuntimeException {
    
    private final ErrorCode errorCode;

    public enum ErrorCode {
        CONFIGURATION_ERROR,
        PARSING_ERROR,
        LAYOUT_ERROR,
        VALIDATION_ERROR,
        MAPPING_ERROR,
        GENERATION_ERROR
    }

    public ConversionException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ConversionException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}