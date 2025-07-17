package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "books")
public class Book extends BaseEntity {

    public enum BookStatus {
        PENDING,
        REJECTED,
        APPROVED
    }

    @Column(nullable = false)
    @NotNull
    private String isbn;

    @Column(nullable = false)
    @NotNull
    private String title;

    @Column(nullable = false)
    @NotNull
    private String subtitle;

    @Column(name = "copyright_year", nullable = false)
    @NotNull
    private Integer copyrightYear;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private BookStatus status;

    // Default constructor
    public Book() {}

    // Constructor with all fields
    public Book(String isbn, String title, String subtitle, Integer copyrightYear, BookStatus status) {
        this.isbn = isbn;
        this.title = title;
        this.subtitle = subtitle;
        this.copyrightYear = copyrightYear;
        this.status = status;
    }

    // Getters and Setters
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

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
} 