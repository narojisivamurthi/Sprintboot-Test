# ⚛️ Redux Toolkit (RTK) & RTK Query Master Roadmap & Learning Progress Tracker

## 🏛️ Redux Toolkit Unidirectional Data Flow Architecture

### 🏗️ RTK Unidirectional Data Flow & State Mutation Pipeline
```mermaid
graph TD
    View["📱 React Component (UI)"] -->|1. dispatch(action)| Dispatcher["⚡ Store Dispatcher"]

    subgraph ReduxStore ["📦 Redux Toolkit Store (configureStore)"]
        Dispatcher -->|2. Route Action| Reducer["⚙️ Slice Reducers (createSlice)"]
        Immer["🛡️ Immer.js Engine (Mutate Draft State Safely)"]
        Reducer <--> Immer
        Reducer -->|3. Update Immutable State| State["💾 Global State Tree"]
    end

    State -->|4. useSelector(selector)| View
    
    subgraph AsyncFlow ["🌐 Async Data Fetching"]
        Thunk["🚀 createAsyncThunk / RTK Query"]
        Thunk -->|pending / fulfilled / rejected| Dispatcher
    end

    View -->|Trigger API Call| Thunk
```

---

## 📑 Phase 1: Redux Core & Redux Toolkit (RTK) Fundamentals

### Module 1: Redux Architecture & Unidirectional Data Flow
- [x] **Redux Principles**
  - Single Source of Truth (Global State Tree), State is Read-Only, Changes are made with Pure Functions (Reducers).
- [x] **Why Redux Toolkit (RTK)?**
  - Standard, recommended way to write Redux logic. Eliminates legacy Redux boilerplate (`types`, `actions`, `reducers` folder fragmentation), configures store automatically, and integrates Immer.js.

### Module 2: RTK Core APIs (`configureStore`, `createSlice`)
- [x] **`configureStore()`**
  - Simplifies store creation by automatically combining slice reducers, adding Redux Thunk middleware, and enabling Redux DevTools extension out of the box.
- [x] **`createSlice()`**
  - Accepts slice name, initial state, and reducer functions. Automatically generates action creators and action types!
- [x] **Immer.js Integration**
  - RTK uses Immer inside `createSlice` allowing developers to write "mutating" code (e.g., `state.user.name = 'Siva'`) which Immer safely converts into immutable state updates.

---

## ⚡ Phase 2: Async Thunks & RTK Query

### Module 3: Asynchronous Logic (`createAsyncThunk`)
- [x] **`createAsyncThunk()`**
  - Handles asynchronous API calls generating `pending`, `fulfilled`, and `rejected` action types automatically handled in `extraReducers`.

### Module 4: RTK Query Data Fetching Engine (`createApi`)
- [x] **RTK Query Architecture**
  - Powerful data fetching and caching capability built into RTK. Replaces manual loading/error state management with auto-generated React hooks (`useGetUsersQuery`, `useUpdateUserMutation`).
- [x] **Caching, Invalidation & Tag System**
  - Automatically caches API responses and invalidates cached data using tags (`providesTags`, `invalidatesTags`) to trigger auto-re-fetching!

---

## 🛠️ Phase 3: Performance Optimization & Selectors

### Module 5: Memoized Selectors (`createSelector` / Reselect)
- [x] **`createSelector()`**
  - Creates memoized selector functions that compute derived data only when input state references change, preventing unnecessary component re-renders.

---

## 🛠️ Phase 4: Practical Redux Toolkit & RTK Query Code

### Complete Slice, RTK Query API & Store Setup (`store.ts`)
```typescript
import { configureStore, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

// 1. RTK Query API Definition
export const userApi = createApi({
  reducerPath: 'userApi',
  baseQuery: fetchBaseQuery({ baseUrl: '/api/' }),
  tagTypes: ['User'],
  endpoints: (builder) => ({
    getUsers: builder.query<Array<{ id: string; name: string }>, void>({
      query: () => 'users',
      providesTags: ['User'],
    }),
    addUser: builder.mutation<void, { name: string }>({
      query: (newUser) => ({
        url: 'users',
        method: 'POST',
        body: newUser,
      }),
      invalidatesTags: ['User'], // Auto-refetches getUsers!
    }),
  }),
});

export const { useGetUsersQuery, useAddUserMutation } = userApi;

// 2. Redux Slice Definition
interface UIState {
  theme: 'light' | 'dark';
}

const uiSlice = createSlice({
  name: 'ui',
  initialState: { theme: 'light' } as UIState,
  reducers: {
    toggleTheme: (state) => {
      state.theme = state.theme === 'light' ? 'dark' : 'light'; // Safe mutation via Immer!
    },
  },
});

export const { toggleTheme } = uiSlice.actions;

// 3. Configure Store
export const store = configureStore({
  reducer: {
    ui: uiSlice.reducer,
    [userApi.reducerPath]: userApi.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(userApi.middleware),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
```

---

## 🎯 Top Redux Toolkit Interview Q&A Cheatsheet (Master List)

### Q1: How does Redux Toolkit eliminate legacy Redux boilerplate?
RTK combines action types, action creators, and reducers into a single `createSlice()` function. It includes `configureStore()` which automatically sets up Redux Thunk middleware and Redux DevTools, and incorporates Immer.js so developers don't have to manually write nested object spread operations (`...state`).

### Q2: How does Immer.js work inside Redux Toolkit's `createSlice`?
Immer wraps current state in a proxy "Draft" object. Developers can write direct state mutations (e.g. `state.items.push(newItem)`). Immer tracks all draft mutations and produces a brand-new immutable state object under the hood.

### Q3: What is RTK Query and how does tag invalidation work?
RTK Query is an advanced data fetching and caching tool built into RTK. When a query endpoint fetches data, it tags the cache with `providesTags: ['User']`. When a mutation occurs (e.g. adding a user), it specifies `invalidatesTags: ['User']`, causing RTK Query to automatically re-fetch matching cached queries in the background.

### Q4: Why should you use `createSelector` (Reselect) for Redux state selection?
`createSelector` creates memoized selectors. If the underlying Redux state has not changed, the selector returns the cached result without re-executing calculations, eliminating unnecessary React component re-renders.
