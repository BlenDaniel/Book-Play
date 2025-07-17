import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.PagedList;
import play.libs.Json;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class BookService {

    /**
     * Create a new book
     */
    public BookDto create(BookCreateRequest request) {
        Book book = request.toBook();
        book.save();
        return new BookDto(book);
    }

    /**
     * Get a book by ID
     */
    public BookDto getOne(String id) {
        try {
            Long bookId = Long.parseLong(id);
            Optional<Book> bookOpt = Optional.ofNullable(Book.find.byId(bookId));
            if (bookOpt.isPresent()) {
                return new BookDto(bookOpt.get());
            } else {
                throw new BookNotFoundException("Book not found with id: " + id);
            }
        } catch (NumberFormatException e) {
            throw new BookInvalidRequestException("Invalid book ID format: " + id);
        }
    }

    /**
     * Get all books
     */
    public List<BookDto> getAll() {
        List<Book> books = Book.find.all();
        return books.stream()
                   .map(BookDto::new)
                   .collect(Collectors.toList());
    }

    /**
     * Update an existing book
     */
    public BookDto update(BookUpdateRequest request) {
        Optional<Book> bookOpt = Optional.ofNullable(Book.find.byId(request.getId()));
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            request.updateBook(book);
            book.save();
            return new BookDto(book);
        } else {
            throw new BookNotFoundException("Book not found with id: " + request.getId());
        }
    }

    /**
     * Delete a book by ID
     */
    public void delete(String id) {
        try {
            Long bookId = Long.parseLong(id);
            Optional<Book> bookOpt = Optional.ofNullable(Book.find.byId(bookId));
            if (bookOpt.isPresent()) {
                bookOpt.get().delete();
            } else {
                throw new BookNotFoundException("Book not found with id: " + id);
            }
        } catch (NumberFormatException e) {
            throw new BookInvalidRequestException("Invalid book ID format: " + id);
        }
    }

    /**
     * Search books by title or subtitle
     */
    public List<BookDto> search(String query) {
        List<Book> books = Book.find
            .where()
            .or()
                .icontains("title", query)
                .icontains("subtitle", query)
            .endOr()
            .findList();
        
        return books.stream()
                   .map(BookDto::new)
                   .collect(Collectors.toList());
    }

    /**
     * Get books with pagination
     */
    public PagedList<Book> getBooks(int page, int pageSize) {
        return Book.find
            .setFirstRow(page * pageSize)
            .setMaxRows(pageSize)
            .findPagedList();
    }
} 