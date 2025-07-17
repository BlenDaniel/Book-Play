import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { type AppDispatch, type RootState } from "../store/store";
import { fetchBooks } from "../services/api";
import {
  getBooksStart,
  getBooksSuccess,
  getBooksFailure,
} from "../store/bookSlice";

export const useBooks = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { books, loading, error } = useSelector(
    (state: RootState) => state.books
  );

  useEffect(() => {
    const loadBooks = async () => {
      try {
        dispatch(getBooksStart());
        const books = await fetchBooks();
        dispatch(getBooksSuccess(books));
      } catch (err) {
        dispatch(
          getBooksFailure(err instanceof Error ? err.message : "Unknown error")
        );
      }
    };

    loadBooks();
  }, [dispatch]);

  return { books, loading, error };
};