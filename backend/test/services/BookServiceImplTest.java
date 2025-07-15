package services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import play.db.jpa.JPAApi;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import models.Book;
import models.dto.BookDto;
import models.request.BookCreateRequest;
import models.request.BookUpdateRequest;
import exceptions.BookNotFoundException;
import exceptions.BookInvalidRequestException;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private JPAApi jpaApi;

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Book> typedQuery;

    private BookServiceImpl bookService;

    private Book testBook;
    private BookCreateRequest createRequest;
    private BookUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(jpaApi);
        
        // Setup test data
        testBook = new Book();
        testBook.setId(1L);
        testBook.setIsbn("978-0-123456-78-9");
        testBook.setTitle("Test Book");
        testBook.setSubtitle("Test Subtitle");
        testBook.setCopyrightYear(2023);
        testBook.setStatus(Book.BookStatus.PENDING);
        testBook.setCreatedAt(Instant.now());
        testBook.setUpdatedAt(Instant.now());

        createRequest = new BookCreateRequest();
        createRequest.setIsbn("978-0-123456-78-9");
        createRequest.setTitle("Test Book");
        createRequest.setSubtitle("Test Subtitle");
        createRequest.setCopyrightYear(2023);
        createRequest.setStatus("PENDING");

        updateRequest = new BookUpdateRequest();
        updateRequest.setId(1L);
        updateRequest.setIsbn("978-0-123456-78-9");
        updateRequest.setTitle("Updated Book");
        updateRequest.setSubtitle("Updated Subtitle");
        updateRequest.setCopyrightYear(2024);
        updateRequest.setStatus("APPROVED");
    }

    @Test
    void testCreate_Success() {
        // Given
        when(jpaApi.withTransaction(any(Function.class))).thenAnswer(invocation -> {
            Function<EntityManager, Object> function = invocation.getArgument(0);
            return function.apply(entityManager);
        });

        // When
        BookDto result = bookService.create(createRequest);

        // Then
        verify(entityManager).persist(any(Book.class));
        verify(entityManager).flush();
        
        assertThat(result).isNotNull();
        assertThat(result.getIsbn()).isEqualTo(createRequest.getIsbn());
        assertThat(result.getTitle()).isEqualTo(createRequest.getTitle());
        assertThat(result.getSubtitle()).isEqualTo(createRequest.getSubtitle());
        assertThat(result.getCopyrightYear()).isEqualTo(createRequest.getCopyrightYear());
        assertThat(result.getStatus()).isEqualTo(createRequest.getStatus());
    }

    @Test
    void testGetOne_Success() {
        // Given
        String bookId = "1";
        when(jpaApi.withTransaction(any(Function.class))).thenAnswer(invocation -> {
            Function<EntityManager, Object> function = invocation.getArgument(0);
            return function.apply(entityManager);
        });
        when(entityManager.find(Book.class, bookId)).thenReturn(testBook);

        // When
        BookDto result = bookService.getOne(bookId);

        // Then
        verify(entityManager).find(Book.class, bookId);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testBook.getId());
        assertThat(result.getTitle()).isEqualTo(testBook.getTitle());
    }

    @Test
    void testGetOne_NotFound() {
        // Given
        String bookId = "999";
        when(jpaApi.withTransaction(any(Function.class))).thenAnswer(invocation -> {
            Function<EntityManager, Object> function = invocation.getArgument(0);
            return function.apply(entityManager);
        });
        when(entityManager.find(Book.class, bookId)).thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> bookService.getOne(bookId))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("Book not found");
    }

    @Test
    void testGetAll_Success() {
        // Given
        List<Book> books = Arrays.asList(testBook);
        when(jpaApi.withTransaction(any(Function.class))).thenAnswer(invocation -> {
            Function<EntityManager, Object> function = invocation.getArgument(0);
            return function.apply(entityManager);
        });
        when(entityManager.createQuery("SELECT b FROM Book b", Book.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(books);

        // When
        List<BookDto> result = bookService.getAll();

        // Then
        verify(entityManager).createQuery("SELECT b FROM Book b", Book.class);
        verify(typedQuery).getResultList();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo(testBook.getTitle());
    }

    @Test
    void testUpdate_Success() {
        // Given
        when(jpaApi.withTransaction(any(Function.class))).thenAnswer(invocation -> {
            Function<EntityManager, Object> function = invocation.getArgument(0);
            return function.apply(entityManager);
        });
        when(entityManager.find(Book.class, updateRequest.getId())).thenReturn(testBook);

        // When
        BookDto result = bookService.update(updateRequest);

        // Then
        verify(entityManager).find(Book.class, updateRequest.getId());
        verify(entityManager).merge(any(Book.class));
        verify(entityManager).flush();
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(updateRequest.getTitle());
    }

    @Test
    void testUpdate_NotFound() {
        // Given
        when(jpaApi.withTransaction(any(Function.class))).thenAnswer(invocation -> {
            Function<EntityManager, Object> function = invocation.getArgument(0);
            return function.apply(entityManager);
        });
        when(entityManager.find(Book.class, updateRequest.getId())).thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> bookService.update(updateRequest))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("Book not found with id: " + updateRequest.getId());
    }

    @Test
    void testDelete_Success() {
        // Given
        String bookId = "1";
        when(jpaApi.withTransaction(any(Function.class))).thenAnswer(invocation -> {
            Function<EntityManager, Object> function = invocation.getArgument(0);
            return function.apply(entityManager);
        });
        when(entityManager.find(Book.class, bookId)).thenReturn(testBook);

        // When
        bookService.delete(bookId);

        // Then
        verify(entityManager).find(Book.class, bookId);
        verify(entityManager).remove(testBook);
        verify(entityManager).flush();
    }

    @Test
    void testDelete_NotFound() {
        // Given
        String bookId = "999";
        when(jpaApi.withTransaction(any(Function.class))).thenAnswer(invocation -> {
            Function<EntityManager, Object> function = invocation.getArgument(0);
            return function.apply(entityManager);
        });
        when(entityManager.find(Book.class, bookId)).thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> bookService.delete(bookId))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("Book not found with id: " + bookId);
    }

    @Test
    void testSearch_Success() {
        // Given
        String searchQuery = "Test";
        List<Book> books = Arrays.asList(testBook);
        when(jpaApi.withTransaction(any(Function.class))).thenAnswer(invocation -> {
            Function<EntityManager, Object> function = invocation.getArgument(0);
            return function.apply(entityManager);
        });
        when(entityManager.createQuery(anyString(), eq(Book.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("query"), anyString())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(books);

        // When
        List<BookDto> result = bookService.search(searchQuery);

        // Then
        verify(entityManager).createQuery(
            "SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(:query) OR LOWER(b.subtitle) LIKE LOWER(:query)", 
            Book.class
        );
        verify(typedQuery).setParameter("query", "%" + searchQuery + "%");
        verify(typedQuery).getResultList();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo(testBook.getTitle());
    }

    @Test
    void testSearch_EmptyResult() {
        // Given
        String searchQuery = "NonExistent";
        when(jpaApi.withTransaction(any(Function.class))).thenAnswer(invocation -> {
            Function<EntityManager, Object> function = invocation.getArgument(0);
            return function.apply(entityManager);
        });
        when(entityManager.createQuery(anyString(), eq(Book.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("query"), anyString())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Arrays.asList());

        // When
        List<BookDto> result = bookService.search(searchQuery);

        // Then
        assertThat(result).isEmpty();
    }
} 