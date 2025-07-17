package utils;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import models.Book;
import models.dto.BookDto;
import models.request.BookCreateRequest;
import models.request.BookUpdateRequest;

/**
 * Factory class for creating test data objects.
 * Provides consistent test data across all test classes.
 */
public class TestDataFactory {

    public static Book createTestBook() {
        return createTestBook(1L, "978-0-123456-78-9", "Test Book", "Test Subtitle", 2023, Book.BookStatus.PENDING);
    }

    public static Book createTestBook(Long id, String isbn, String title, String subtitle, Integer copyrightYear, Book.BookStatus status) {
        Book book = new Book();
        book.setId(id);
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setSubtitle(subtitle);
        book.setCopyrightYear(copyrightYear);
        book.setStatus(status);
        book.setCreatedAt(Instant.now());
        book.setUpdatedAt(Instant.now());
        return book;
    }

    public static BookDto createTestBookDto() {
        return createTestBookDto(1L, "978-0-123456-78-9", "Test Book", "Test Subtitle", 2023, "PENDING");
    }

    public static BookDto createTestBookDto(Long id, String isbn, String title, String subtitle, Integer copyrightYear, String status) {
        BookDto dto = new BookDto();
        dto.setId(id);
        dto.setIsbn(isbn);
        dto.setTitle(title);
        dto.setSubtitle(subtitle);
        dto.setCopyrightYear(copyrightYear);
        dto.setStatus(status);
        dto.setCreatedAt(Instant.now());
        dto.setUpdatedAt(Instant.now());
        return dto;
    }

    public static BookCreateRequest createTestBookCreateRequest() {
        return createTestBookCreateRequest("978-0-123456-78-9", "Test Book", "Test Subtitle", 2023, "PENDING");
    }

    public static BookCreateRequest createTestBookCreateRequest(String isbn, String title, String subtitle, Integer copyrightYear, String status) {
        BookCreateRequest request = new BookCreateRequest();
        request.setIsbn(isbn);
        request.setTitle(title);
        request.setSubtitle(subtitle);
        request.setCopyrightYear(copyrightYear);
        request.setStatus(status);
        return request;
    }

    public static BookUpdateRequest createTestBookUpdateRequest() {
        return createTestBookUpdateRequest(1L, "978-0-123456-78-9", "Updated Book", "Updated Subtitle", 2024, "APPROVED");
    }

    public static BookUpdateRequest createTestBookUpdateRequest(Long id, String isbn, String title, String subtitle, Integer copyrightYear, String status) {
        BookUpdateRequest request = new BookUpdateRequest();
        request.setId(id);
        request.setIsbn(isbn);
        request.setTitle(title);
        request.setSubtitle(subtitle);
        request.setCopyrightYear(copyrightYear);
        request.setStatus(status);
        return request;
    }

    public static List<Book> createTestBookList() {
        return Arrays.asList(
            createTestBook(1L, "978-0-123456-78-9", "First Book", "First Subtitle", 2023, Book.BookStatus.PENDING),
            createTestBook(2L, "978-0-123456-78-0", "Second Book", "Second Subtitle", 2022, Book.BookStatus.APPROVED),
            createTestBook(3L, "978-0-123456-78-1", "Third Book", "Third Subtitle", 2021, Book.BookStatus.REJECTED)
        );
    }

    public static List<BookDto> createTestBookDtoList() {
        return Arrays.asList(
            createTestBookDto(1L, "978-0-123456-78-9", "First Book", "First Subtitle", 2023, "PENDING"),
            createTestBookDto(2L, "978-0-123456-78-0", "Second Book", "Second Subtitle", 2022, "APPROVED"),
            createTestBookDto(3L, "978-0-123456-78-1", "Third Book", "Third Subtitle", 2021, "REJECTED")
        );
    }

    // Invalid data for negative testing
    public static BookCreateRequest createInvalidBookCreateRequest() {
        BookCreateRequest request = new BookCreateRequest();
        request.setIsbn(""); // Invalid: empty ISBN
        request.setTitle(""); // Invalid: empty title
        request.setSubtitle("Valid Subtitle");
        request.setCopyrightYear(null); // Invalid: null copyright year
        request.setStatus("INVALID_STATUS"); // Invalid: non-existent status
        return request;
    }

    public static BookUpdateRequest createInvalidBookUpdateRequest() {
        BookUpdateRequest request = new BookUpdateRequest();
        request.setId(null); // Invalid: null ID
        request.setIsbn("");
        request.setTitle("");
        request.setSubtitle("Valid Subtitle");
        request.setCopyrightYear(null);
        request.setStatus("INVALID_STATUS");
        return request;
    }

    // Edge case data
    public static BookCreateRequest createBookCreateRequestWithNullFields() {
        BookCreateRequest request = new BookCreateRequest();
        request.setIsbn("978-0-123456-78-9");
        request.setTitle("Book with Nulls");
        request.setSubtitle(null); // Optional field
        request.setCopyrightYear(2023);
        request.setStatus("PENDING");
        return request;
    }

    public static BookUpdateRequest createPartialBookUpdateRequest(Long id) {
        BookUpdateRequest request = new BookUpdateRequest();
        request.setId(id);
        request.setTitle("Partially Updated Title");
        // Other fields are null - testing partial updates
        return request;
    }
} 