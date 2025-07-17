import axios from "axios";
import { type Book, type BookFormData } from "../types/book";
import type { ApiResponse } from "../types/apiResponse";

const API_BASE_URL = "http://localhost:9000/api/books";

export const addBook = async (book: BookFormData): Promise<Book> => {
  const response = await axios.post<ApiResponse<Book>>(API_BASE_URL, book);
  return response.data.data;
};

export const fetchBookById = async (id: string): Promise<Book> => {
  const response = await axios.get<ApiResponse<Book>>(`${API_BASE_URL}/${id}`);
  return response.data.data;
};

export const fetchBooks = async (): Promise<Book[]> => {
  const response = await axios.get<ApiResponse<Book[]>>(API_BASE_URL);
  return response.data.data;
};

export const updateBook = async (book: Book): Promise<Book> => {
  // Create the update request with just the ID and data
  const updateRequest = {
    id: book.id,
    isbn: book.isbn,
    title: book.title,
    subtitle: book.subtitle,
    copyrightYear: book.copyrightYear,
    status: book.status,
  };
  const response = await axios.patch<ApiResponse<Book>>(
    API_BASE_URL,
    updateRequest
  );
  return response.data.data;
};

export const deleteBook = async (id: string): Promise<void> => {
  await axios.delete<ApiResponse<string>>(`${API_BASE_URL}/${id}`);
};

export const searchBooks = async (query: string): Promise<Book[]> => {
  const response = await axios.get<ApiResponse<Book[]>>(
    `${API_BASE_URL}/search?query=${encodeURIComponent(query)}`
  );
  return response.data.data;
};
