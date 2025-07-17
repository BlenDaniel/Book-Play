import bookReducer, {
  getBooksStart,
  getBooksSuccess,
  getBooksFailure,
  addBook,
  updateBook,
  deleteBook,
} from "./bookSlice";
import { BookStatus, type Book } from "../types/book";

describe("bookSlice", () => {
  const initialState = {
    books: [],
    loading: false,
    error: null,
  };

  it("should handle initial state", () => {
    expect(bookReducer(undefined, { type: "unknown" })).toEqual(initialState);
  });

  it("should handle getBooksStart", () => {
    const actual = bookReducer(initialState, getBooksStart());
    expect(actual.loading).toBe(true);
  });

  it("should handle getBooksSuccess", () => {
    const books: Book[] = [
      {
        id: "1",
        isbn: "1234567890",
        title: "Test Book",
        subtitle: "Test Subtitle",
        copyrightYear: "2023",
        status: BookStatus["PENDING"],
      },
    ];
    const actual = bookReducer(initialState, getBooksSuccess(books));
    expect(actual.books).toEqual(books);
    expect(actual.loading).toBe(false);
    expect(actual.error).toBeNull();
  });

  it("should handle getBooksFailure", () => {
    const error = "Failed to fetch books";
    const actual = bookReducer(initialState, getBooksFailure(error));
    expect(actual.error).toBe(error);
    expect(actual.loading).toBe(false);
  });

  it("should handle addBook", () => {
    const book: Book = {
      id: "1",
      isbn: "1234567890",
      title: "Test Book",
      subtitle: "Test Subtitle",
      copyrightYear: "2023",
      status: BookStatus["PENDING"],
    };
    const actual = bookReducer(initialState, addBook(book));
    expect(actual.books).toEqual([book]);
  });

  it("should handle updateBook", () => {
    const initialStateWithBook = {
      ...initialState,
      books: [
        {
          id: "1",
          isbn: "1234567890",
          title: "Test Book",
          subtitle: "Test Subtitle",
          copyrightYear: "2023",
          status: BookStatus["PENDING"],
        },
      ],
    };
    const updatedBook: Book = {
      id: "1",
      isbn: "0987654321",
      title: "Updated Book",
      subtitle: "Updated Subtitle",
      copyrightYear: "2024",
      status: BookStatus["APPROVED"],
    };
    const actual = bookReducer(initialStateWithBook, updateBook(updatedBook));
    expect(actual.books).toEqual([updatedBook]);
  });

  it("should handle deleteBook", () => {
    const initialStateWithBooks = {
      ...initialState,
      books: [
        {
          id: "1",
          isbn: "1234567890",
          title: "Test Book 1",
          subtitle: "Subtitle 1",
          copyrightYear: "2023",
          status: BookStatus["PENDING"],
        },
        {
          id: "2",
          isbn: "0987654321",
          title: "Test Book 2",
          subtitle: "Subtitle 2",
          copyrightYear: "2023",
          status: BookStatus["APPROVED"],
        },
      ],
    };
    const actual = bookReducer(initialStateWithBooks, deleteBook("1"));
    expect(actual.books).toEqual([
      {
        id: "2",
        isbn: "0987654321",
        title: "Test Book 2",
        subtitle: "Subtitle 2",
        copyrightYear: "2023",
        status: BookStatus["APPROVED"],
      },
    ]);
  });
});
