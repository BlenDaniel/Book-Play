public class BookInvalidRequestException extends RuntimeException {
    
    public BookInvalidRequestException(String message) {
        super(message);
    }
    
    public BookInvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }
} 