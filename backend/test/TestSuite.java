import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import controllers.BookControllerTest;
import services.BookServiceImplTest;

/**
 * Test Suite that runs all tests in the application.
 * This allows running all tests with a single command.
 */
@Suite
@SelectClasses({
    // Service Layer Tests
    BookServiceImplTest.class,
    
    // Controller Layer Tests
    BookControllerTest.class
})
public class TestSuite {
    // Test suite class - no additional implementation needed
    // The annotations handle the test execution
} 