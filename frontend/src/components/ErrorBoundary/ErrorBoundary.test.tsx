import { render } from "@testing-library/react";
import ErrorBoundary from "./ErrorBoundary";

const ProblemChild = () => {
  throw new Error("Error thrown from problem child");
  return <div>Error</div>; // eslint-disable-line
};

describe("ErrorBoundary", () => {
  beforeAll(() => {
    jest.spyOn(console, "error").mockImplementation(() => {});
  });

  afterAll(() => {
    jest.restoreAllMocks();
  });

  test("catches errors and displays fallback", () => {
    const { getByText } = render(
      <ErrorBoundary>
        <ProblemChild />
      </ErrorBoundary>
    );

    expect(getByText("Something went wrong.")).toBeInTheDocument();
    expect(getByText("Error thrown from problem child")).toBeInTheDocument();
    expect(getByText("Try again?")).toBeInTheDocument();
  });
});