

package services;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;
import models.Book;
import models.dto.BookDto;
import models.request.BookCreateRequest;
import models.request.BookUpdateRequest;
import exceptions.BookNotFoundException;
import exceptions.BookInvalidRequestException;

@Singleton
public class BookServiceImpl implements BookService {

    private final JPAApi jpaApi;

    @Inject
    public BookServiceImpl(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public BookDto create(BookCreateRequest request) {
        return jpaApi.withTransaction(em -> {
            play.Logger.info("Creating new book with title: {}", request.getTitle());

            Book book = new Book();
            book.setIsbn(request.getIsbn());
            book.setTitle(request.getTitle());
            book.setSubtitle(request.getSubtitle());
                            book.setStatus(Book.BookStatus.valueOf(request.getStatus().toUpperCase()));
            
            if (request.getCopyrightYear() != null) {
                book.setCopyrightYear(request.getCopyrightYear());
            }

            em.persist(book);
            em.flush();
            
            play.Logger.info("Successfully created book with id: {}", book.getId());
            return toDto(book);
        });
    }

    @Override
    public BookDto getOne(String id) {
        return jpaApi.withTransaction(em -> {
            play.Logger.info("Fetching book with id: {}", id);
            
            Book book = em.find(Book.class, id);
            if (book == null) {
                throw new BookNotFoundException("Book not found");
            }
            
            return toDto(book);
        });
    }

    @Override
    public List<BookDto> getAll() {
        return jpaApi.withTransaction(em -> {
            play.Logger.info("Fetching all books");
            
            TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b", Book.class);
            List<Book> books = query.getResultList();
            
            return books.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        });
    }

    @Override
    public BookDto update(BookUpdateRequest request) {
        return jpaApi.withTransaction(em -> {
            play.Logger.info("Updating book with id: {}", request.getId());
            
            Book book = em.find(Book.class, request.getId());
            if (book == null) {
                throw new BookNotFoundException("Book not found with id: " + request.getId());
            }
            
            if (request.getIsbn() != null) book.setIsbn(request.getIsbn());
            if (request.getTitle() != null) book.setTitle(request.getTitle());
            if (request.getSubtitle() != null) book.setSubtitle(request.getSubtitle());
            if (request.getStatus() != null) book.setStatus(Book.BookStatus.valueOf(request.getStatus().toUpperCase()));
            
            if (request.getCopyrightYear() != null) {
                book.setCopyrightYear(request.getCopyrightYear());
            }
            
            em.merge(book);
            em.flush();
            
            play.Logger.info("Successfully updated book with id: {}", book.getId());
            return toDto(book);
        });
    }

    @Override
    public void delete(String id) {
        jpaApi.withTransaction(em -> {
            play.Logger.info("Deleting book with id: {}", id);
            
            Book book = em.find(Book.class, id);
            if (book == null) {
                throw new BookNotFoundException("Book not found with id: " + id);
            }
            
            em.remove(book);
            em.flush();
            
            play.Logger.info("Successfully deleted book with id: {}", id);
            return null;
        });
    }

    @Override
    public List<BookDto> search(String query) {
        return jpaApi.withTransaction(em -> {
            play.Logger.info("Searching books with query: {}", query);
            
            TypedQuery<Book> jpqlQuery = em.createQuery(
                "SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(:query) OR LOWER(b.subtitle) LIKE LOWER(:query)", 
                Book.class
            );
            jpqlQuery.setParameter("query", "%" + query + "%");
            
            List<Book> books = jpqlQuery.getResultList();
            
            return books.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        });
    }
    
    private BookDto toDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setIsbn(book.getIsbn());
        dto.setTitle(book.getTitle());
        dto.setSubtitle(book.getSubtitle());
        dto.setCopyrightYear(book.getCopyrightYear());
        dto.setStatus(book.getStatus().name());
        dto.setCreatedAt(book.getCreatedAt());
        dto.setUpdatedAt(book.getUpdatedAt());
        return dto;
    }
} 