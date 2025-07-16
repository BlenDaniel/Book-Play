import { type BookFormData } from "../types/book";

export const validateBook = (book: BookFormData) => {
  const errors: Partial<Record<keyof BookFormData, string>> = {};

  if (!book.title.trim()) {
    errors.title = "Title is required";
  }

  if (!book.subtitle.trim()) {
    errors.subtitle = "Subtitle is required";
  }

  if (!book.isbn.trim()) {
    errors.isbn = "ISBN is required";
  } else if (book.isbn.length !== 10 && book.isbn.length !== 13) {
    errors.isbn = "ISBN must be 10 or 13 characters";
  }

  if (!book.copyrightYear) {
    errors.copyrightYear = "Copyright year is required";
  }

  if (!book.status) {
    errors.status = "Status is required";
  }
  
  return errors;
};