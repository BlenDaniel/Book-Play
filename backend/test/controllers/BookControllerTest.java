package controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static play.mvc.Http.Status.*;
import static play.test.Helpers.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Bindings;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;

import models.dto.BookDto;
import services.BookService;
import exceptions.BookNotFoundException;
import exceptions.BookInvalidRequestException;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest extends WithApplication {

    @Mock
    private BookService bookService;

    private BookController controller;
    private BookDto testBookDto;
    private ObjectMapper objectMapper;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
                .overrides(Bindings.bind(BookService.class).toInstance(bookService))
                .build();
    }

    @BeforeEach
    void setUp() {
        controller = new BookController();
        // Use reflection to inject the mock service
        try {
            java.lang.reflect.Field field = BookController.class.getDeclaredField("bookService");
            field.setAccessible(true);
            field.set(controller, bookService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        objectMapper = new ObjectMapper();
        
        // Setup test data
        testBookDto = new BookDto();
        testBookDto.setId(1L);
        testBookDto.setIsbn("978-0-123456-78-9");
        testBookDto.setTitle("Test Book");
        testBookDto.setSubtitle("Test Subtitle");
        testBookDto.setCopyrightYear(2023);
        testBookDto.setStatus("PENDING");
        testBookDto.setCreatedAt(Instant.now());
        testBookDto.setUpdatedAt(Instant.now());
    }

    @Test
    void testCreate_Success() throws Exception {
        // Given
        String requestBody = """
            {
                "isbn": "978-0-123456-78-9",
                "title": "Test Book",
                "subtitle": "Test Subtitle",
                "copyrightYear": 2023,
                "status": "PENDING"
            }
            """;

        when(bookService.create(any())).thenReturn(testBookDto);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/books")
                .bodyJson(Json.parse(requestBody));

        // When
        CompletionStage<Result> resultStage = controller.create(request.build());
        Result result = resultStage.toCompletableFuture().get();

        // Then
        assertThat(result.status()).isEqualTo(OK);
        
        JsonNode responseJson = Json.parse(contentAsString(result));
        assertThat(responseJson.get("success").asBoolean()).isTrue();
        assertThat(responseJson.get("data").get("title").asText()).isEqualTo("Test Book");
        
        verify(bookService).create(any());
    }

    @Test
    void testCreate_InvalidJson() throws Exception {
        // Given
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/books")
                .bodyText("invalid json");

        // When
        CompletionStage<Result> resultStage = controller.create(request.build());
        Result result = resultStage.toCompletableFuture().get();

        // Then
        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        
        JsonNode responseJson = Json.parse(contentAsString(result));
        assertThat(responseJson.get("success").asBoolean()).isFalse();
        assertThat(responseJson.get("message").asText()).isEqualTo("Invalid JSON data");
    }

    @Test
    void testCreate_ServiceException() throws Exception {
        // Given
        String requestBody = """
            {
                "isbn": "invalid-isbn",
                "title": "",
                "subtitle": "Test Subtitle",
                "copyrightYear": 2023,
                "status": "PENDING"
            }
            """;

        when(bookService.create(any())).thenThrow(new BookInvalidRequestException("Invalid book data"));

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/api/books")
                .bodyJson(Json.parse(requestBody));

        // When
        CompletionStage<Result> resultStage = controller.create(request.build());
        Result result = resultStage.toCompletableFuture().get();

        // Then
        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        
        JsonNode responseJson = Json.parse(contentAsString(result));
        assertThat(responseJson.get("success").asBoolean()).isFalse();
        assertThat(responseJson.get("message").asText()).isEqualTo("Invalid book data");
    }

    @Test
    void testGetOne_Success() throws Exception {
        // Given
        String bookId = "1";
        when(bookService.getOne(bookId)).thenReturn(testBookDto);

        // When
        CompletionStage<Result> resultStage = controller.getOne(bookId);
        Result result = resultStage.toCompletableFuture().get();

        // Then
        assertThat(result.status()).isEqualTo(OK);
        
        JsonNode responseJson = Json.parse(contentAsString(result));
        assertThat(responseJson.get("success").asBoolean()).isTrue();
        assertThat(responseJson.get("data").get("id").asLong()).isEqualTo(1L);
        assertThat(responseJson.get("data").get("title").asText()).isEqualTo("Test Book");
        
        verify(bookService).getOne(bookId);
    }

    @Test
    void testGetOne_NotFound() throws Exception {
        // Given
        String bookId = "999";
        when(bookService.getOne(bookId)).thenThrow(new BookNotFoundException("Book not found"));

        // When
        CompletionStage<Result> resultStage = controller.getOne(bookId);
        Result result = resultStage.toCompletableFuture().get();

        // Then
        assertThat(result.status()).isEqualTo(NOT_FOUND);
        
        JsonNode responseJson = Json.parse(contentAsString(result));
        assertThat(responseJson.get("success").asBoolean()).isFalse();
        assertThat(responseJson.get("message").asText()).isEqualTo("Book not found");
    }

    @Test
    void testGetAll_Success() throws Exception {
        // Given
        List<BookDto> books = Arrays.asList(testBookDto);
        when(bookService.getAll()).thenReturn(books);

        // When
        CompletionStage<Result> resultStage = controller.getAll();
        Result result = resultStage.toCompletableFuture().get();

        // Then
        assertThat(result.status()).isEqualTo(OK);
        
        JsonNode responseJson = Json.parse(contentAsString(result));
        assertThat(responseJson.get("success").asBoolean()).isTrue();
        assertThat(responseJson.get("data").isArray()).isTrue();
        assertThat(responseJson.get("data").size()).isEqualTo(1);
        assertThat(responseJson.get("data").get(0).get("title").asText()).isEqualTo("Test Book");
        
        verify(bookService).getAll();
    }

    @Test
    void testGetAll_EmptyList() throws Exception {
        // Given
        when(bookService.getAll()).thenReturn(Arrays.asList());

        // When
        CompletionStage<Result> resultStage = controller.getAll();
        Result result = resultStage.toCompletableFuture().get();

        // Then
        assertThat(result.status()).isEqualTo(OK);
        
        JsonNode responseJson = Json.parse(contentAsString(result));
        assertThat(responseJson.get("success").asBoolean()).isTrue();
        assertThat(responseJson.get("data").isArray()).isTrue();
        assertThat(responseJson.get("data").size()).isEqualTo(0);
    }

    @Test
    void testUpdate_Success() throws Exception {
        // Given
        String requestBody = """
            {
                "id": 1,
                "isbn": "978-0-123456-78-9",
                "title": "Updated Book",
                "subtitle": "Updated Subtitle",
                "copyrightYear": 2024,
                "status": "APPROVED"
            }
            """;

        BookDto updatedBookDto = new BookDto();
        updatedBookDto.setId(1L);
        updatedBookDto.setTitle("Updated Book");
        updatedBookDto.setStatus("APPROVED");

        when(bookService.update(any())).thenReturn(updatedBookDto);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method("PATCH")
                .uri("/api/books")
                .bodyJson(Json.parse(requestBody));

        // When
        CompletionStage<Result> resultStage = controller.update(request.build());
        Result result = resultStage.toCompletableFuture().get();

        // Then
        assertThat(result.status()).isEqualTo(OK);
        
        JsonNode responseJson = Json.parse(contentAsString(result));
        assertThat(responseJson.get("success").asBoolean()).isTrue();
        assertThat(responseJson.get("data").get("title").asText()).isEqualTo("Updated Book");
        assertThat(responseJson.get("data").get("status").asText()).isEqualTo("APPROVED");
        
        verify(bookService).update(any());
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        // Given
        String requestBody = """
            {
                "id": 999,
                "isbn": "978-0-123456-78-9",
                "title": "Updated Book",
                "subtitle": "Updated Subtitle",
                "copyrightYear": 2024,
                "status": "APPROVED"
            }
            """;

        when(bookService.update(any())).thenThrow(new BookNotFoundException("Book not found"));

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method("PATCH")
                .uri("/api/books")
                .bodyJson(Json.parse(requestBody));

        // When
        CompletionStage<Result> resultStage = controller.update(request.build());
        Result result = resultStage.toCompletableFuture().get();

        // Then
        assertThat(result.status()).isEqualTo(NOT_FOUND);
        
        JsonNode responseJson = Json.parse(contentAsString(result));
        assertThat(responseJson.get("success").asBoolean()).isFalse();
        assertThat(responseJson.get("message").asText()).isEqualTo("Book not found");
    }

    @Test
    void testDelete_Success() throws Exception {
        // Given
        String bookId = "1";
        doNothing().when(bookService).delete(bookId);

        // When
        CompletionStage<Result> resultStage = controller.delete(bookId);
        Result result = resultStage.toCompletableFuture().get();

        // Then
        assertThat(result.status()).isEqualTo(OK);
        
        JsonNode responseJson = Json.parse(contentAsString(result));
        assertThat(responseJson.get("success").asBoolean()).isTrue();
        assertThat(responseJson.get("data").asText()).isEqualTo("Book deleted successfully");
        
        verify(bookService).delete(bookId);
    }

    @Test
    void testDelete_NotFound() throws Exception {
        // Given
        String bookId = "999";
        doThrow(new BookNotFoundException("Book not found")).when(bookService).delete(bookId);

        // When
        CompletionStage<Result> resultStage = controller.delete(bookId);
        Result result = resultStage.toCompletableFuture().get();

        // Then
        assertThat(result.status()).isEqualTo(NOT_FOUND);
        
        JsonNode responseJson = Json.parse(contentAsString(result));
        assertThat(responseJson.get("success").asBoolean()).isFalse();
        assertThat(responseJson.get("message").asText()).isEqualTo("Book not found");
    }

    @Test
    void testSearch_Success() throws Exception {
        // Given
        String searchQuery = "Test";
        List<BookDto> books = Arrays.asList(testBookDto);
        when(bookService.search(searchQuery)).thenReturn(books);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/books/search?query=" + searchQuery);

        // When
        CompletionStage<Result> resultStage = controller.search(request.build());
        Result result = resultStage.toCompletableFuture().get();

        // Then
        assertThat(result.status()).isEqualTo(OK);
        
        JsonNode responseJson = Json.parse(contentAsString(result));
        assertThat(responseJson.get("success").asBoolean()).isTrue();
        assertThat(responseJson.get("data").isArray()).isTrue();
        assertThat(responseJson.get("data").size()).isEqualTo(1);
        assertThat(responseJson.get("data").get(0).get("title").asText()).isEqualTo("Test Book");
        
        verify(bookService).search(searchQuery);
    }

    @Test
    void testSearch_MissingQuery() throws Exception {
        // Given
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/books/search");

        // When
        CompletionStage<Result> resultStage = controller.search(request.build());
        Result result = resultStage.toCompletableFuture().get();

        // Then
        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        
        JsonNode responseJson = Json.parse(contentAsString(result));
        assertThat(responseJson.get("success").asBoolean()).isFalse();
        assertThat(responseJson.get("message").asText()).isEqualTo("Query parameter is required");
    }

    @Test
    void testSearch_EmptyQuery() throws Exception {
        // Given
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/books/search?query=");

        // When
        CompletionStage<Result> resultStage = controller.search(request.build());
        Result result = resultStage.toCompletableFuture().get();

        // Then
        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        
        JsonNode responseJson = Json.parse(contentAsString(result));
        assertThat(responseJson.get("success").asBoolean()).isFalse();
        assertThat(responseJson.get("message").asText()).isEqualTo("Query parameter is required");
    }

    @Test
    void testSearch_NoResults() throws Exception {
        // Given
        String searchQuery = "NonExistent";
        when(bookService.search(searchQuery)).thenReturn(Arrays.asList());

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/books/search?query=" + searchQuery);

        // When
        CompletionStage<Result> resultStage = controller.search(request.build());
        Result result = resultStage.toCompletableFuture().get();

        // Then
        assertThat(result.status()).isEqualTo(OK);
        
        JsonNode responseJson = Json.parse(contentAsString(result));
        assertThat(responseJson.get("success").asBoolean()).isTrue();
        assertThat(responseJson.get("data").isArray()).isTrue();
        assertThat(responseJson.get("data").size()).isEqualTo(0);
        
        verify(bookService).search(searchQuery);
    }
} 