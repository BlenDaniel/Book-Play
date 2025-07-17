import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useDispatch } from "react-redux";
import { type AppDispatch } from "../../store/store";
import { BookStatus, type BookFormData } from "../../types/book";
import { updateBook, addBook } from "../../store/bookSlice";
import { validateBook } from "../../utils/validation";
import { useBooks } from "../../hooks/useBooks";
import * as api from "../../services/api";
import styles from "./BookForm.module.css";

const BookForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const dispatch = useDispatch<AppDispatch>();
  const { books, loading: booksLoading } = useBooks();

  const [formData, setFormData] = useState<BookFormData>({
    isbn: "",
    title: "",
    subtitle: "",
    copyrightYear: "",
    status: BookStatus.PENDING,
  });

  const [errors, setErrors] = useState<
    Partial<Record<keyof BookFormData | "submit", string>>
  >({});
  const [touched, setTouched] = useState<Record<string, boolean>>({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  useEffect(() => {
    if (id) {
      const book = books.find((b) => 
        String(b.id) === String(id) || b.isbn === id
      );

      console.log('Looking for book with id:', id);
      console.log('Available books:', books);
      console.log('Found book:', book);
      if (book) {
        const { id: _, ...bookData } = book;
        setFormData(bookData);
        // Reset touched and errors when loading a new book
        setTouched({});
        setErrors({});
      }
    } else {
      // Reset form when switching from edit to add mode
      setFormData({
        isbn: "",
        title: "",
        subtitle: "",
        copyrightYear: "",
        status: BookStatus.PENDING,
      });
      setTouched({});
      setErrors({});
    }
  }, [id, books, booksLoading]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value, type } = e.target;
    let processedValue: string | number = value;

    if (type === "number") {
      processedValue = value === "" ? "" : Number(value);
    }

    if (name === "status") processedValue = value as unknown as BookStatus;

    setFormData((prev) => ({ ...prev, [name]: processedValue }));

    // Validate on change if the field was touched
    if (touched[name]) {
      const validationErrors = validateBook({
        ...formData,
        [name]: processedValue,
      });
      setErrors((prev) => ({
        ...prev,
        [name]: validationErrors[name as keyof BookFormData],
      }));
    }
  };

  const handleBlur = (
    e: React.FocusEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name } = e.target;
    setTouched((prev) => ({ ...prev, [name]: true }));

    const validationErrors = validateBook(formData);
    setErrors((prev) => ({
      ...prev,
      [name]: validationErrors[name as keyof BookFormData],
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    // Mark all fields as touched when submitting
    const allTouched = Object.keys(formData).reduce((acc, key) => {
      acc[key] = true;
      return acc;
    }, {} as Record<string, boolean>);
    setTouched(allTouched);

    const validationErrors = validateBook(formData);
    setErrors(validationErrors);

    if (Object.keys(validationErrors).length === 0) {
      setIsSubmitting(true);
      try {
        if (id) {
          const updatedBook = await api.updateBook({ id, ...formData });
          dispatch(updateBook(updatedBook));
        } else {
          const newBook = await api.addBook(formData);
          dispatch(addBook(newBook));
        }
        navigate("/");
      } catch (error) {
        console.error("Error submitting book:", error);
        setErrors((prev) => ({
          ...prev,
          submit: "Failed to submit book. Please try again.",
        }));
      } finally {
        setIsSubmitting(false);
      }
    }
  };

  // Helper function to get error class
  const getErrorClass = (fieldName: string) => {
    return touched[fieldName] && errors[fieldName as keyof BookFormData]
      ? styles.errorInput
      : "";
  };

  // Show loading state while books are being fetched for edit mode
  if (id && booksLoading) {
    return <div>Loading book data...</div>;
  }

  return (
    <div className={styles.container}>
      <h1>{id ? "Edit Book" : "Add Book"}</h1>
      <form onSubmit={handleSubmit} className={styles.form}>
        <div className={styles.formGroup}>
          <div className={styles.formGroup}>
            <label htmlFor="isbn">ISBN</label>
            <input
              type="text"
              id="isbn"
              name="isbn"
              value={formData.isbn}
              onChange={handleChange}
              onBlur={handleBlur}
              className={getErrorClass("isbn")}
              required
            />
            {touched.isbn && errors.isbn && (
              <span className={styles.errorText}>{errors.isbn}</span>
            )}
          </div>

          <label htmlFor="title">Title</label>
          <input
            type="text"
            id="title"
            name="title"
            value={formData.title}
            onChange={handleChange}
            onBlur={handleBlur}
            className={getErrorClass("title")}
            required
          />
          {touched.title && errors.title && (
            <span className={styles.errorText}>{errors.title}</span>
          )}
        </div>

        <div className={styles.formGroup}>
          <label htmlFor="subtitle">Subtitle</label>
          <input
            type="text"
            id="subtitle"
            name="subtitle"
            value={formData.subtitle}
            onChange={handleChange}
            onBlur={handleBlur}
            className={getErrorClass("subtitle")}
            required
          />
          {touched.subtitle && errors.subtitle && (
            <span className={styles.errorText}>{errors.subtitle}</span>
          )}
        </div>

        <div className={styles.formGroup}>
          <label htmlFor="copyrightYear">Copyright Year</label>
          <input
            type="number"
            id="copyrightYear"
            name="copyrightYear"
            value={formData.copyrightYear}
            onChange={handleChange}
            onBlur={handleBlur}
            className={getErrorClass("copyrightYear")}
            min="1900"
            max={new Date().getFullYear()}
            required
          />
          {touched.copyrightYear && errors.copyrightYear && (
            <span className={styles.errorText}>{errors.copyrightYear}</span>
          )}
        </div>
        <div className={styles.formGroup}>
          <label htmlFor="status">Status</label>
          <select
            id="status"
            name="status"
            value={formData.status}
            onChange={handleChange}
            onBlur={handleBlur}
            className={getErrorClass("status")}
          >
            <option value="">Select status</option>
            {Object.entries(BookStatus).map(([key, label]) => (
              <option key={key} value={label}>
                {label.charAt(0).toUpperCase() + label.slice(1).toLowerCase()}
              </option>
            ))}
          </select>
          {touched.status && errors.status && (
            <span className={styles.errorText}>{errors.status}</span>
          )}
        </div>

        {errors.submit && (
          <div className={styles.errorText}>{errors.submit}</div>
        )}

        <button
          type="submit"
          className={styles.submitButton}
          disabled={isSubmitting}
        >
          {isSubmitting ? "Submitting..." : id ? "Update Book" : "Add Book"}
        </button>
      </form>
    </div>
  );
};

export default BookForm;
