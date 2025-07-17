import React from "react";
import ErrorBoundary from "./components/ErrorBoundary/ErrorBoundary";
import { Route, Routes } from "react-router-dom";
import BookForm from "./components/BookForm/BookForm";
import BookList from "./components/BookList/BookList";
import Layout from "./components/Layout/Layout";

const App: React.FC = () => {
  return (
    <ErrorBoundary>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<BookList />} />
          <Route path="add" element={<BookForm />} />
          <Route path="edit/:id" element={<BookForm />} />
        </Route>
      </Routes>
    </ErrorBoundary>
  );
};

export default App;
