import play.ApplicationLoader;
import play.Environment;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Bootstrap extends GuiceApplicationLoader {

    @Override
    public GuiceApplicationBuilder builder(ApplicationLoader.Context context) {
        return initialBuilder(context)
            .overrides(overrides(context));
    }

    @Inject
    public Bootstrap(Environment environment) {
        // Initialize database with sample data when application starts
        initializeDatabase();
    }

    private void initializeDatabase() {
        // Check if books already exist to avoid duplicate data
        if (Book.find.all().isEmpty()) {
            createSampleBooks();
        }
    }

    private void createSampleBooks() {
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
            book.save();
        }
        
        System.out.println("Database initialized with " + sampleBooks.length + " sample books");
    }
} 