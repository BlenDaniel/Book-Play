import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class BookService {

    private final JPAApi jpaApi;

    @Inject
    public BookService(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    private EntityManager em() {
        return jpaApi.em();
    }

    /**
     * Create a new book
     */
    @Transactional
    public BookDto create(BookCreateRequest request) {
        Book book = request.toBook();
        em().persist(book);
        return new BookDto(book);
    }

    /**
     * Get a book by ID
     */
    @Transactional(readOnly = true)
    public BookDto getOne(String id) {
        try {
            Long bookId = Long.parseLong(id);
            Book book = em().find(Book.class, bookId);
            if (book != null) {
                return new BookDto(book);
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
    @Transactional(readOnly = true)
    public List<BookDto> getAll() {
        TypedQuery<Book> query = em().createQuery("SELECT b FROM Book b", Book.class);
        List<Book> books = query.getResultList();
        return books.stream()
                   .map(BookDto::new)
                   .collect(Collectors.toList());
    }

    /**
     * Update an existing book
     */
    @Transactional
    public BookDto update(BookUpdateRequest request) {
        Book book = em().find(Book.class, request.getId());
        if (book != null) {
            request.updateBook(book);
            em().merge(book);
            return new BookDto(book);
        } else {
            throw new BookNotFoundException("Book not found with id: " + request.getId());
        }
    }

    /**
     * Delete a book by ID
     */
    @Transactional
    public void delete(String id) {
        try {
            Long bookId = Long.parseLong(id);
            Book book = em().find(Book.class, bookId);
            if (book != null) {
                em().remove(book);
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
    @Transactional(readOnly = true)
    public List<BookDto> search(String query) {
        TypedQuery<Book> jpqlQuery = em().createQuery(
            "SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(:query) OR LOWER(b.subtitle) LIKE LOWER(:query)", 
            Book.class
        );
        jpqlQuery.setParameter("query", "%" + query + "%");
        List<Book> books = jpqlQuery.getResultList();
        
        return books.stream()
                   .map(BookDto::new)
                   .collect(Collectors.toList());
    }
} 