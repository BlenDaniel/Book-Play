# Book Management System - Code Challenge

A full-stack web application for managing a collection of books. The system provides functionality to create, read, update, delete, and search books in an inventory. Built with Play framework backend in Java and React with TypeScript frontend.

## Code Challenge Requirements

This application implements the following API requirements:

### Data Processor App
- **POST API endpoint** to accept JSON data and store books in a database
- **Subtitle field is optional** - all other fields are mandatory
- **Validation**: If a mandatory field is missing, the book is not stored
- **Return insertion result** for each book operation
- **GET API endpoint** to read data from the database
- **JSON response format** for all endpoints

## Technology Stack

### Frontend
- React 18 with TypeScript
- Vite (build tool and dev server)
- Redux Toolkit for state management
- CSS Modules for styling
- Jest and React Testing Library for testing
- ESLint for code quality

### Backend
- **Play Framework 2.8.x** with Java 21
- **H2 Database** (relational database for development)
- **JPA/Hibernate** for data persistence
- **JUnit 5** for automated testing
- **SBT** for build management
- Lombok for reducing boilerplate code

## Project Structure

```
book-inventory-management/
├── README.md
├── data_sample.json
├── frontend/
│   ├── package.json
│   ├── vite.config.ts
│   ├── tsconfig.json
│   ├── tsconfig.app.json
│   ├── tsconfig.node.json
│   ├── tsconfig.jest.json
│   ├── eslint.config.js
│   ├── jest.config.cjs
│   ├── index.html
│   ├── public/
│   │   └── vite.svg
│   └── src/
│       ├── App.tsx
│       ├── main.tsx
│       ├── index.css
│       ├── components/
│       │   ├── BookForm/
│       │   ├── BookList/
│       │   ├── ErrorBoundary/
│       │   └── Layout/
│       ├── hooks/
│       │   └── useBooks.ts
│       ├── services/
│       │   └── api.ts
│       ├── store/
│       │   ├── store.ts
│       │   └── bookSlice.ts
│       ├── types/
│       │   └── book.ts
│       ├── utils/
│       │   └── validation.ts
│       └── __tests__/
│           └── setup.ts
└── backend/
    ├── build.sbt
    ├── project/
    │   ├── build.properties
    │   └── plugins.sbt
    ├── conf/
    │   ├── application.conf
    │   ├── routes
    │   ├── logback.xml
    │   └── META-INF/
    │       └── persistence.xml
    ├── app/
    │   ├── Bootstrap.java
    │   ├── controllers/
    │   │   ├── BookController.java
    │   │   └── HomeController.java
    │   ├── models/
    │   │   ├── BaseEntity.java
    │   │   ├── Book.java
    │   │   ├── dto/
    │   │   │   └── BookDto.java
    │   │   └── request/
    │   │       ├── BookCreateRequest.java
    │   │       └── BookUpdateRequest.java
    │   ├── services/
    │   │   ├── BookService.java
    │   │   └── BookServiceImpl.java
    │   ├── exceptions/
    │   │   ├── BookInvalidRequestException.java
    │   │   └── BookNotFoundException.java
    │   ├── utils/
    │   │   └── ApiResponse.java
    │   └── views/
    │       ├── index.scala.html
    │       └── main.scala.html
    ├── test/
    │   ├── controllers/
    │   │   └── BookControllerTest.java
    │   ├── services/
    │   │   └── BookServiceImplTest.java
    │   ├── utils/
    │   │   └── TestDataFactory.java
    │   └── TestSuite.java
    └── public/
        ├── images/
        │   └── favicon.png
        ├── javascripts/
        │   └── main.js
        └── stylesheets/
            └── main.css
```

## Features

- **Create new books** with details (ISBN, title, subtitle, copyright year)
- **View all books** in the inventory
- **Search books** by title, subtitle
- **Update existing book** information
- **Delete books** from the inventory
- **Input validation** - mandatory fields enforcement
- **Error handling** with proper API responses
- **Responsive web interface**
- **Automated tests** for all components
- **RESTful API design**

## Prerequisites

Before running this application, make sure you have the following installed:

- **Java 21** or higher
- **SBT (Scala Build Tool)** 1.8.x or higher
- **Node.js** (version 16 or higher)
- **npm** or yarn

## Getting Started

### Backend Setup (Play Framework)

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Run the application using SBT:
   ```bash
   sbt run
   ```

   The backend will start on `http://localhost:9000`

3. To run in development mode with auto-reload:
   ```bash
   sbt ~run
   ```

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

   The frontend will start on `http://localhost:5173`

## API Endpoints

The backend provides the following REST API endpoints as per code challenge requirements:

| Method | Endpoint | Description | Response |
|--------|----------|-------------|----------|
| **POST** | `/api/books` | **Store book data** - accepts JSON, validates mandatory fields | Insertion result with success/error details |
| **GET** | `/api/books` | **Retrieve all books** - returns JSON data from database | JSON array of all books |
| GET | `/api/books/{id}` | Get a specific book by ID | JSON object of book details |
| GET | `/api/books/search?query={query}` | Search books by title or subtitle | JSON array of matching books |
| PATCH | `/api/books` | Update an existing book | Updated book object |
| DELETE | `/api/books/{id}` | Delete a book by ID | Deletion confirmation |

### Request/Response Examples

#### Create a Book (Code Challenge Requirement)
```json
POST /api/books
Content-Type: application/json

{
  "isbn": "9780743273565",
  "title": "The Great Gatsby",
  "subtitle": "A Novel",
  "copyrightYear": 1925
}
```

**Response:**
```json
{
  "success": true,
  "message": "Book stored successfully",
  "data": {
    "id": "1",
    "isbn": "9780743273565",
    "title": "The Great Gatsby",
    "subtitle": "A Novel",
    "copyrightYear": 1925,
    "status": "approved"
  }
}
```

#### Validation Error Response
```json
{
  "success": false,
  "message": "Mandatory field missing",
  "errors": ["Title is required", "ISBN is required"]
}
```

#### Retrieve Books (Code Challenge Requirement)
```json
GET /api/books

Response:
{
  "success": true,
  "data": [
    {
      "id": "1",
      "isbn": "9780743273565", 
      "title": "The Great Gatsby",
      "subtitle": "A Novel",
      "copyrightYear": 1925,
      "status": "approved"
    }
  ]
}
```

## Testing

### Backend Tests (Play Framework)

Run all tests:
```bash
cd backend
sbt test
```

Run tests with coverage:
```bash
sbt jacoco
```

Run specific test:
```bash
sbt "testOnly controllers.BookControllerTest"
```

### Frontend Tests

Run frontend tests:
```bash
cd frontend
npm test
```

Run tests with coverage:
```bash
npm run test:coverage
```

## Development

### Backend Development (Play Framework)

The backend uses Play Framework with the following key features:
- **H2 relational database** for data storage
- **JPA/Hibernate** for data persistence
- **Dependency injection** with Guice
- **Routes file** for URL mapping
- **Action composition** for request handling
- **JSON serialization/deserialization**
- **Comprehensive error handling**
- **Request validation** as per challenge requirements

### Frontend Development

The frontend uses React with TypeScript and includes:
- Redux Toolkit for state management
- Custom hooks for API integration
- CSS Modules for component styling
- Form validation utilities
- Error boundary for error handling
- Responsive design principles

## Configuration

### Backend Configuration (Play Framework)

The main configuration is in `backend/conf/application.conf`:
- Database configuration (H2)
- Server port (default: 9000)
- Logging levels
- CORS settings
- JPA/Hibernate settings

### Frontend Configuration

Frontend configuration is in `frontend/vite.config.ts`:
- Development server settings
- Build configuration
- Testing setup
- Proxy settings for API calls

## Database Schema

The application uses H2 relational database with the following book schema:

```sql
CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    subtitle VARCHAR(255),
    copyright_year INTEGER NOT NULL,
    status VARCHAR(50) DEFAULT 'approved',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## Code Challenge Implementation Notes

- **Mandatory field validation**: ISBN, title, and copyright year are required
- **Optional subtitle field**: Can be null or empty
- **Database storage**: Uses H2 relational database as specified
- **JSON API responses**: All endpoints return properly formatted JSON
- **Error handling**: Comprehensive validation and error responses
- **Automated tests**: Full test coverage for all components
- **Play Framework**: Built using Java with Play Framework as required

## Troubleshooting

### Common Issues

1. **SBT Build Issues**: Ensure Java 21 is installed and JAVA_HOME is set correctly
2. **Database Connection**: H2 database is file-based and creates automatically
3. **Port Conflicts**: Change the port in application.conf (backend) or vite.config.ts (frontend)
4. **CORS Errors**: Make sure the backend CORS configuration includes your frontend URL

### Logs

- Backend logs are available in the console when running with `sbt run`
- Frontend logs are available in the browser console
- Test logs are shown when running test commands

## Documentation

This documentation explains how to run the project as required by the code challenge. The application demonstrates:

- RESTful API design with Play Framework
- Relational database integration
- Comprehensive automated testing
- Clean code architecture
- Proper error handling and validation

For any questions about the implementation or running the project, please refer to this documentation or reach out for clarification.