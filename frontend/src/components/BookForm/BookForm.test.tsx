import { render, screen, fireEvent } from '@testing-library/react';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import { configureStore } from '@reduxjs/toolkit';
import BookForm from './BookForm';
import bookSlice from '../../store/bookSlice';

const createTestStore = () =>
  configureStore({
    reducer: {
      books: bookSlice,
    },
    preloadedState: {
      books: {
        books: [],
        loading: false,
        error: null,
      },
    },
  });

const renderBookForm = () => {
  const store = createTestStore();
  return render(
    <Provider store={store}>
      <BrowserRouter>
        <BookForm />
      </BrowserRouter>
    </Provider>
  );
};

describe('BookForm Component', () => {
  it('should render add book form', () => {
    renderBookForm();

    expect(screen.getByRole('heading', { name: /^add book$/i })).toBeInTheDocument();
    expect(screen.getByLabelText(/^isbn$/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/^title$/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/^subtitle$/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/^copyright year$/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/^status$/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /^add book$/i })).toBeInTheDocument();
  });

  it('should allow input in all form fields', () => {
    renderBookForm();

    const isbnInput = screen.getByLabelText(/^isbn$/i) as HTMLInputElement;
    const titleInput = screen.getByLabelText(/^title$/i) as HTMLInputElement;
    const subtitleInput = screen.getByLabelText(/^subtitle$/i) as HTMLInputElement;
    const yearInput = screen.getByLabelText(/^copyright year$/i) as HTMLInputElement;
    const statusSelect = screen.getByLabelText(/^status$/i) as HTMLSelectElement;

    fireEvent.change(isbnInput, { target: { value: '978-0-123456-78-9' } });
    fireEvent.change(titleInput, { target: { value: 'Test Book' } });
    fireEvent.change(subtitleInput, { target: { value: 'Test Subtitle' } });
    fireEvent.change(yearInput, { target: { value: '2023' } });
    fireEvent.change(statusSelect, { target: { value: 'APPROVED' } });

    expect(isbnInput.value).toBe('978-0-123456-78-9');
    expect(titleInput.value).toBe('Test Book');
    expect(subtitleInput.value).toBe('Test Subtitle');
    expect(yearInput.value).toBe('2023');
    expect(statusSelect.value).toBe('APPROVED'); // Matches the value from BookStatus enum
  });

  it('should have required validation attributes on required fields', () => {
    renderBookForm();

    expect(screen.getByLabelText(/^isbn$/i)).toHaveAttribute('required');
    expect(screen.getByLabelText(/^title$/i)).toHaveAttribute('required');
    expect(screen.getByLabelText(/^subtitle$/i)).toHaveAttribute('required');
    expect(screen.getByLabelText(/^copyright year$/i)).toHaveAttribute('required');
  });

  it('should have proper input types', () => {
    renderBookForm();

    expect(screen.getByLabelText(/^isbn$/i)).toHaveAttribute('type', 'text');
    expect(screen.getByLabelText(/^title$/i)).toHaveAttribute('type', 'text');
    expect(screen.getByLabelText(/^subtitle$/i)).toHaveAttribute('type', 'text');
    expect(screen.getByLabelText(/^copyright year$/i)).toHaveAttribute('type', 'number');

    // The status field is a select element, so no 'type' attribute to check
    expect(screen.getByLabelText(/^status$/i).tagName.toLowerCase()).toBe('select');
  });
});
