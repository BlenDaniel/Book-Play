import play.mvc.*;
import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class BookController extends Controller {

    @Inject
    private BookService bookService;

    /**
     * Create a new book
     * POST /api/books
     */
    public CompletionStage<Result> create(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonNode json = request.body().asJson();
                if (json == null) {
                    return badRequest(Json.toJson(ApiResponse.error("Invalid JSON data")));
                }

                BookCreateRequest createRequest = Json.fromJson(json, BookCreateRequest.class);
                BookDto bookDto = bookService.create(createRequest);
                
                return ok(Json.toJson(ApiResponse.success(bookDto)));
            } catch (BookInvalidRequestException e) {
                return badRequest(Json.toJson(ApiResponse.error(e.getMessage())));
            } catch (Exception e) {
                return internalServerError(Json.toJson(ApiResponse.error("Failed to create book")));
            }
        });
    }

    /**
     * Get a book by ID
     * GET /api/books/:id
     */
    public CompletionStage<Result> getOne(String id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                BookDto bookDto = bookService.getOne(id);
                return ok(Json.toJson(ApiResponse.success(bookDto)));
            } catch (BookNotFoundException e) {
                return notFound(Json.toJson(ApiResponse.error(e.getMessage())));
            } catch (BookInvalidRequestException e) {
                return badRequest(Json.toJson(ApiResponse.error(e.getMessage())));
            } catch (Exception e) {
                return internalServerError(Json.toJson(ApiResponse.error("Failed to get book")));
            }
        });
    }

    /**
     * Get all books
     * GET /api/books
     */
    public CompletionStage<Result> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<BookDto> books = bookService.getAll();
                return ok(Json.toJson(ApiResponse.success(books)));
            } catch (Exception e) {
                return internalServerError(Json.toJson(ApiResponse.error("Failed to get books")));
            }
        });
    }

    /**
     * Update an existing book
     * PATCH /api/books
     */
    public CompletionStage<Result> update(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonNode json = request.body().asJson();
                if (json == null) {
                    return badRequest(Json.toJson(ApiResponse.error("Invalid JSON data")));
                }

                BookUpdateRequest updateRequest = Json.fromJson(json, BookUpdateRequest.class);
                BookDto bookDto = bookService.update(updateRequest);
                
                return ok(Json.toJson(ApiResponse.success(bookDto)));
            } catch (BookNotFoundException e) {
                return notFound(Json.toJson(ApiResponse.error(e.getMessage())));
            } catch (BookInvalidRequestException e) {
                return badRequest(Json.toJson(ApiResponse.error(e.getMessage())));
            } catch (Exception e) {
                return internalServerError(Json.toJson(ApiResponse.error("Failed to update book")));
            }
        });
    }

    /**
     * Delete a book by ID
     * DELETE /api/books/:id
     */
    public CompletionStage<Result> delete(String id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                bookService.delete(id);
                return ok(Json.toJson(ApiResponse.success("Book deleted successfully")));
            } catch (BookNotFoundException e) {
                return notFound(Json.toJson(ApiResponse.error(e.getMessage())));
            } catch (BookInvalidRequestException e) {
                return badRequest(Json.toJson(ApiResponse.error(e.getMessage())));
            } catch (Exception e) {
                return internalServerError(Json.toJson(ApiResponse.error("Failed to delete book")));
            }
        });
    }

    /**
     * Search books by title or subtitle
     * GET /api/books/search?query=searchTerm
     */
    public CompletionStage<Result> search(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String query = request.getQueryString("query");
                if (query == null || query.trim().isEmpty()) {
                    return badRequest(Json.toJson(ApiResponse.error("Query parameter is required")));
                }

                List<BookDto> books = bookService.search(query);
                return ok(Json.toJson(ApiResponse.success(books)));
            } catch (Exception e) {
                return internalServerError(Json.toJson(ApiResponse.error("Failed to search books")));
            }
        });
    }
} 