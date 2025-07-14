import play.inject.ApplicationLifecycle;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.concurrent.CompletableFuture;

@Singleton
public class Bootstrap {

    private final JPAApi jpaApi;

    @Inject
    public Bootstrap(ApplicationLifecycle lifecycle, JPAApi jpaApi) {
        this.jpaApi = jpaApi;
        
        // Initialize database with sample data when application starts
        initializeDatabase();
        
        // Clean up on shutdown
        lifecycle.addStopHook(() -> {
            return CompletableFuture.completedFuture(null);
        });
    }

    @Transactional
    public void initializeDatabase() {
        EntityManager em = jpaApi.em();
        
        // Check if books already exist to avoid duplicate data
        TypedQuery<Long> countQuery = em.createQuery("SELECT COUNT(b) FROM Book b", Long.class);
        Long bookCount = countQuery.getSingleResult();
        
        if (bookCount == 0) {
            createSampleBooks(em);
        }
    }

    private void createSampleBooks(EntityManager em) {
        // Sample data from the design
        Book[] sampleBooks = {
            new Book("9780300267662", "Why Architecture Matters", 
                    "A classic work on the joy of experiencing architecture", 2023, Book.BookStatus.APPROVED),
            
            new Book("978-31-10914-67-5", "The Death Penalty", "", 2026, Book.BookStatus.PENDING),
            
            new Book("9783110545982", "Qualitative Interviews", "", 2025, Book.BookStatus.REJECTED),
            
            new Book("978-05-20392-30-4", "Equality within Our Lifetimes", 
                    "A free ebook version of this title is available through Luminos", 2000, Book.BookStatus.APPROVED),
            
            new Book("9780520392314", "A General Theory of Crime", "", 2022, Book.BookStatus.APPROVED),
            
            new Book("9780300268478", "The Great New York Fire of 1776", 
                    "Who set the mysterious fire", 2010, Book.BookStatus.APPROVED)
        };

        for (Book book : sampleBooks) {
            em.persist(book);
        }
        
        System.out.println("Database initialized with " + sampleBooks.length + " sample books");
    }
} 