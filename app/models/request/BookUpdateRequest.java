import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class BookUpdateRequest {

    @NotNull(message = "Book ID is required")
    private Long id;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @NotBlank(message = "Title is required")
    private String title;

    private String subtitle;

    @JsonProperty("copyrightYear")
    @NotNull(message = "Copyright year is required")
    private Integer copyrightYear;

    @NotBlank(message = "Status is required")
    private String status;

    // Default constructor
    public BookUpdateRequest() {}

    // Constructor
    public BookUpdateRequest(Long id, String isbn, String title, String subtitle, Integer copyrightYear, String status) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.subtitle = subtitle;
        this.copyrightYear = copyrightYear;
        this.status = status;
    }

    // Update existing Book entity
    public void updateBook(Book book) {
        book.setIsbn(this.isbn);
        book.setTitle(this.title);
        book.setSubtitle(this.subtitle != null ? this.subtitle : "");
        book.setCopyrightYear(this.copyrightYear);
        book.setStatus(Book.BookStatus.valueOf(this.status.toUpperCase()));
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Integer getCopyrightYear() {
        return copyrightYear;
    }

    public void setCopyrightYear(Integer copyrightYear) {
        this.copyrightYear = copyrightYear;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 