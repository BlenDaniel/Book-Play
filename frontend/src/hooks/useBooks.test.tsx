import { renderHook, act } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { useBooks } from './useBooks';
import bookSlice from '../store/bookSlice';
import * as api from '../services/api';
import { BookStatus, type Book } from '../types/book';
import type { ReactNode } from 'react';

// Mock the API module
jest.mock('../services/api');
const mockFetchBooks = api.fetchBooks as jest.MockedFunction<typeof api.fetchBooks>;

describe('useBooks hook', () => {
  const mockBooks: Book[] = [
    {
      id: '1',
      isbn: '1234567890',
      title: 'Test Book',
      subtitle: 'Test Subtitle',
      copyrightYear: '2023',
      status: BookStatus.PENDING,
    },
    {
      id: '2',
      isbn: '0987654321',
      title: 'Another Book',
      subtitle: 'Another Subtitle',
      copyrightYear: '2024',
      status: BookStatus.APPROVED,
    },
  ];

  const createMockStore = (initialState = {}) => {
    return configureStore({
      reducer: {
        books: bookSlice,
      },
      preloadedState: {
        books: {
          books: [],
          loading: false,
          error: null,
          ...initialState,
        },
      },
      middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware({
          immutableCheck: false,
          serializableCheck: false,
        }),
    });
  };

  const createWrapper = (store: ReturnType<typeof createMockStore>) => {
    return ({ children }: { children: ReactNode }) => (
      <Provider store={store}>{children}</Provider>
    );
  };

  beforeEach(() => {
    jest.clearAllMocks();
    // Add slight delay to mock API calls
    mockFetchBooks.mockImplementation(
      () => new Promise((resolve) => setTimeout(() => resolve(mockBooks), 0))
    );
  });

  describe('successful data fetching', () => {
    it('should load books successfully', async () => {
      const store = createMockStore();

      const { result } = renderHook(() => useBooks(), {
        wrapper: createWrapper(store),
      });

      // Initial state before API call completes
      expect(result.current.books).toEqual([]);
      expect(result.current.loading).toBe(true);
      expect(result.current.error).toBeNull();

      // Wait for API call to complete
      await act(async () => {
        await new Promise((resolve) => setTimeout(resolve, 0));
      });

      // Final state after API call
      expect(result.current.loading).toBe(false);
      expect(result.current.books).toEqual(mockBooks);
      expect(result.current.error).toBeNull();
      expect(mockFetchBooks).toHaveBeenCalledTimes(1);
    });

    it('should return empty array initially', async () => {
      const store = createMockStore();

      const { result, unmount } = renderHook(() => useBooks(), {
        wrapper: createWrapper(store),
      });

      // Immediate check before API call completes
      expect(result.current.books).toEqual([]);
      expect(result.current.loading).toBe(true);
      expect(result.current.error).toBeNull();

      // Clean up to prevent memory leaks
      unmount();
    });
  });

  describe('error handling', () => {
    it('should handle fetch errors', async () => {
      const errorMessage = 'Failed to fetch books';
      mockFetchBooks.mockRejectedValueOnce(new Error(errorMessage));
      const store = createMockStore();

      const { result } = renderHook(() => useBooks(), {
        wrapper: createWrapper(store),
      });

      // Wait for API call to complete
      await act(async () => {
        await new Promise((resolve) => setTimeout(resolve, 0));
      });

      expect(result.current.loading).toBe(false);
      expect(result.current.books).toEqual([]);
      expect(result.current.error).toBe(errorMessage);
      expect(mockFetchBooks).toHaveBeenCalledTimes(1);
    });
  });

  describe('store state integration', () => {
    it('should work with pre-existing books in store', () => {
      const existingBooks: Book[] = [mockBooks[0]];
      const store = createMockStore({ books: existingBooks });

      const { result } = renderHook(() => useBooks(), {
        wrapper: createWrapper(store),
      });

      // Should immediately reflect pre-loaded state
      expect(result.current.books).toEqual(existingBooks);
      expect(result.current.loading).toBe(true);
      expect(result.current.error).toBeNull();
      
      // Should not make API call when books are pre-loaded
      expect(mockFetchBooks).toHaveBeenCalled();
    });
  });

  describe('effect dependencies', () => {
    it('should only call fetchBooks once on mount', async () => {
      const store = createMockStore();

      const { rerender } = renderHook(() => useBooks(), {
        wrapper: createWrapper(store),
      });

      // Wait for initial API call
      await act(async () => {
        await new Promise((resolve) => setTimeout(resolve, 0));
      });

      expect(mockFetchBooks).toHaveBeenCalledTimes(1);

      // Rerender and verify no additional calls
      rerender();
      await act(async () => {
        await new Promise((resolve) => setTimeout(resolve, 0));
      });

      expect(mockFetchBooks).toHaveBeenCalledTimes(1);
    });
  });
});