module.exports = {
  roots: ["<rootDir>/src"],
  testEnvironment: "jsdom",
  setupFilesAfterEnv: ["<rootDir>/src/__tests__/setup.ts"],
  moduleNameMapper: {
    "\\.(css|less|scss|sass)$": "identity-obj-proxy"
  },
  transform: {
    "^.+\\.(ts|tsx)$": ["ts-jest", { tsconfig: '<rootDir>/tsconfig.jest.json' }]
  },
  collectCoverageFrom: [
    "src/**/*.{ts,tsx}",
    "!src/**/*.d.ts"
  ]
}; 