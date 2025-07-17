import { validateBook } from "./validation";
import { type Book, BookStatus } from "../types/book";

describe("validateBook", () => {
  it("should return no errors for valid book", () => {
    const validBook: Book = {
      id: "1",
      title: "Valid Book",
      subtitle: "Valid Subtitle",
      isbn: "1234567890",
      copyrightYear: "2023",
      status: BookStatus.PENDING,
    };
    const errors = validateBook(validBook);
    expect(errors).toEqual({});
  });

  it("should return errors for empty fields", () => {
    const invalidBook: Book = {
      id: "2",
      title: "",
      subtitle: "",
      isbn: "",
      copyrightYear: "2023",
      status: BookStatus.PENDING,
    };
    const errors = validateBook(invalidBook);
    expect(errors).toEqual({
 title: "Title is required",
  subtitle: "Subtitle is required",
  isbn: "ISBN is required",
    });
  });

  it("should return error for invalid ISBN", () => {
    const invalidBook: Book = {
      id: "3",
      title: "Valid Book",
      subtitle: "Valid Subtitle",
      isbn: "123",
      copyrightYear: "2023",
      status: BookStatus.PENDING,
    };
    const errors = validateBook(invalidBook);
    expect(errors).toEqual({
      isbn: "ISBN must be 10 or 13 characters",
    });
  });

  it("should return error for invalid copyright year", () => {
    const invalidBook: Book = {
      id: "4",
      title: "Valid Book",
      subtitle: "Valid Subtitle",
      isbn: "1234567890123",
      copyrightYear: "",
      status: BookStatus.PENDING,
    };
    const errors = validateBook(invalidBook);
    expect(errors).toEqual({
      copyrightYear: "Copyright year is required",
    });
  });

  it("should handle all book statuses", () => {
    const validBookApproved: Book = {
      id: "5",
      title: "Approved Book",
      subtitle: "Approved Subtitle",
      isbn: "1234567890",
      copyrightYear: "2023",
      status: BookStatus.APPROVED,
    };
    const errors = validateBook(validBookApproved);
    expect(errors).toEqual({});
  });

  it("should validate 13-digit ISBN", () => {
    const validBook: Book = {
      id: "6",
      title: "Valid Book",
      subtitle: "Valid Subtitle",
      isbn: "1234567890123",
      copyrightYear: "2023",
      status: BookStatus.PENDING,
    };
    const errors = validateBook(validBook);
    expect(errors).toEqual({});
  });

  it("should handle multiple validation errors", () => {
    const invalidBook: Book = {
      id: "7",
      title: "",
      subtitle: "",
      isbn: "12",
      copyrightYear: "",
      status: BookStatus.PENDING,
    };
    const errors = validateBook(invalidBook);
    expect(errors).toEqual({
        title: "Title is required",
  subtitle: "Subtitle is required",
  isbn: "ISBN must be 10 or 13 characters",
  copyrightYear: "Copyright year is required",
    });
  });
});