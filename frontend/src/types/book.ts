export interface Book {
  id: string;
  isbn: string;
  title: string;
  subtitle: string;
  copyrightYear: string;
  status: BookStatus;
}

export enum BookStatus {
  PENDING = "PENDING",
  REJECTED = "REJECTED",
  APPROVED = "APPROVED"
}

export type BookFormData = Omit<Book, "id">;