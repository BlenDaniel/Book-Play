import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import { configureStore } from '@reduxjs/toolkit';
import BookList from './BookList';
import bookSlice from '../../store/bookSlice';
import * as api from '../../services/api';
import { BookStatus } from '../../types/book';

// Mock the API
jest.mock('../../services/api');
const mockedApi = api as jest.Mocked<typeof api>;

// Mock the useBooks hook with proper fields matching your BookList component's expected data shape
jest.mock('../../hooks/useBooks', () => ({
  useBooks: () => ({
    books: [
      {
        id: '1',
        isbn: '978-0-123456-78-9',
        title: 'Test Book 1',
        subtitle: 'Subtitle 1',
        copyrightYear: '2022',
        status:  BookStatus['PENDING']
      },
      {
        id: '2',
        isbn: '978-0-987654-32-1',
        title: 'Test Book 2',
        subtitle: 'Subtitle 2',
        copyrightYear: '2023',
        status:  BookStatus['APPROVED']
      }
    ],
    loading: false,
    error: null,
  }),
}));

const createTestStore = () =>
  configureStore({
    reducer: {
      books: bookSlice,
    },
  });

const renderBookList = () => {
  const store = createTestStore();
  return render(
    <Provider store={store}>
      <BrowserRouter>
        <BookList />
      </BrowserRouter>
    </Provider>
  );
};

describe('BookList Component', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders book list with books', () => {
    renderBookList();

    expect(screen.getByText('Book List')).toBeInTheDocument();

    // Check presence of book info matching mocked books
    expect(screen.getByText('978-0-123456-78-9')).toBeInTheDocument();
    expect(screen.getByText('Test Book 1')).toBeInTheDocument();
    expect(screen.getByText('Subtitle 1')).toBeInTheDocument();
    expect(screen.getByText('2022')).toBeInTheDocument();
    expect(screen.getByText('PENDING')).toBeInTheDocument();

    expect(screen.getByText('978-0-987654-32-1')).toBeInTheDocument();
    expect(screen.getByText('Test Book 2')).toBeInTheDocument();
    expect(screen.getByText('Subtitle 2')).toBeInTheDocument();
    expect(screen.getByText('2023')).toBeInTheDocument();
    expect(screen.getByText('APPROVED')).toBeInTheDocument();
  });

  it('renders search input and buttons', () => {
    renderBookList();

    expect(screen.getByPlaceholderText(/search books/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /search/i })).toBeInTheDocument();
  });

  it('renders table headers', () => {
    renderBookList();

    expect(screen.getByText('ISBN')).toBeInTheDocument();
    expect(screen.getByText('Title')).toBeInTheDocument();
    expect(screen.getByText('Subtitle')).toBeInTheDocument();
    expect(screen.getByText('Copyright Year')).toBeInTheDocument();
    expect(screen.getByText('Status')).toBeInTheDocument();
  });

  it('renders edit and delete buttons for each book', () => {
    renderBookList();

    const editButtons = screen.getAllByText('Edit');
    const deleteButtons = screen.getAllByText('Delete');

    expect(editButtons).toHaveLength(2);
    expect(deleteButtons).toHaveLength(2);
  });

  it('handles search input change', () => {
    renderBookList();

    const searchInput = screen.getByPlaceholderText(/search books/i) as HTMLInputElement;
    fireEvent.change(searchInput, { target: { value: 'test search' } });

    expect(searchInput.value).toBe('test search');
  });

  it('calls search API when search button is clicked and displays results', async () => {
    const mockSearchResults = [
      {
        id: '3',
        isbn: '978-0-111111-11-1',
        title: 'Search Result',
        subtitle: 'Search Subtitle',
        copyrightYear: '2023',
        status: BookStatus['APPROVED'],
      },
    ];

    mockedApi.searchBooks.mockResolvedValueOnce(mockSearchResults);

    renderBookList();

    const searchInput = screen.getByPlaceholderText(/search books/i);
    const searchButton = screen.getByRole('button', { name: /search/i });

    fireEvent.change(searchInput, { target: { value: 'search query' } });
    fireEvent.click(searchButton);

    expect(searchButton).toBeDisabled(); // Searching state disables button

    await waitFor(() => {
      expect(mockedApi.searchBooks).toHaveBeenCalledWith('search query');
    });

    // Verify search results rendered
    expect(screen.getByText('Search Result')).toBeInTheDocument();
    expect(screen.getByText('Search Subtitle')).toBeInTheDocument();
    expect(screen.getByText('978-0-111111-11-1')).toBeInTheDocument();

    // Clear Search button should show up after search results
    expect(screen.getByRole('button', { name: /clear search/i })).toBeInTheDocument();
  });
});
