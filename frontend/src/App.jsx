import React, { lazy, Suspense } from 'react';
import { Routes, Route } from 'react-router-dom';
import Navbar from './components/layout/Navbar';
import Loader from './components/common/Loader';
import ProtectedRoute from './components/common/ProtectedRoute';

// Auth Components
const LoginPage = lazy(() => import('./pages/auth/LoginPage'));
const RegisterPage = lazy(() => import('./pages/auth/RegisterPage'));

// Lazy-loaded Multi-Page Components
const DashboardPage = lazy(() => import('./pages/DashboardPage'));

// Employee Module Routes
const EmployeeListPage = lazy(() => import('./pages/employees/EmployeeListPage'));
const EmployeeDetailPage = lazy(() => import('./pages/employees/EmployeeDetailPage'));
const EmployeeFormPage = lazy(() => import('./pages/employees/EmployeeFormPage'));

// Department Module Routes
const DepartmentListPage = lazy(() => import('./pages/departments/DepartmentListPage'));
const DepartmentDetailPage = lazy(() => import('./pages/departments/DepartmentDetailPage'));
const DepartmentFormPage = lazy(() => import('./pages/departments/DepartmentFormPage'));

// Designation Module Routes
const DesignationListPage = lazy(() => import('./pages/designations/DesignationListPage'));
const DesignationFormPage = lazy(() => import('./pages/designations/DesignationFormPage'));

// City Module Routes
const CityListPage = lazy(() => import('./pages/cities/CityListPage'));
const CityFormPage = lazy(() => import('./pages/cities/CityFormPage'));

// User Module Routes
const UserListPage = lazy(() => import('./pages/users/UserListPage'));

// Fallback Route
const NotFoundPage = lazy(() => import('./pages/NotFoundPage'));

export default function App() {
  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
      {/* Shared Layout Header */}
      <Navbar />

      {/* Main Content View with Multi-Page Routing */}
      <main style={{ flex: 1, maxWidth: '1400px', width: '100%', margin: '0 auto', padding: '32px 24px' }}>
        <Suspense fallback={<Loader text="Loading module..." />}>
          <Routes>
            {/* Public Auth Routes */}
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />

            {/* Protected Routes */}
            <Route path="/" element={<ProtectedRoute><DashboardPage /></ProtectedRoute>} />

            {/* Employee Module */}
            <Route path="/employees" element={<ProtectedRoute><EmployeeListPage /></ProtectedRoute>} />
            <Route path="/employees/new" element={<ProtectedRoute><EmployeeFormPage /></ProtectedRoute>} />
            <Route path="/employees/:id" element={<ProtectedRoute><EmployeeDetailPage /></ProtectedRoute>} />
            <Route path="/employees/:id/edit" element={<ProtectedRoute><EmployeeFormPage /></ProtectedRoute>} />

            {/* Department Module */}
            <Route path="/departments" element={<ProtectedRoute><DepartmentListPage /></ProtectedRoute>} />
            <Route path="/departments/new" element={<ProtectedRoute><DepartmentFormPage /></ProtectedRoute>} />
            <Route path="/departments/:id" element={<ProtectedRoute><DepartmentDetailPage /></ProtectedRoute>} />
            <Route path="/departments/:id/edit" element={<ProtectedRoute><DepartmentFormPage /></ProtectedRoute>} />

            {/* Designation Module */}
            <Route path="/designations" element={<ProtectedRoute><DesignationListPage /></ProtectedRoute>} />
            <Route path="/designations/new" element={<ProtectedRoute><DesignationFormPage /></ProtectedRoute>} />
            <Route path="/designations/:id/edit" element={<ProtectedRoute><DesignationFormPage /></ProtectedRoute>} />

            {/* City Module */}
            <Route path="/cities" element={<ProtectedRoute><CityListPage /></ProtectedRoute>} />
            <Route path="/cities/new" element={<ProtectedRoute><CityFormPage /></ProtectedRoute>} />
            <Route path="/cities/:id/edit" element={<ProtectedRoute><CityFormPage /></ProtectedRoute>} />

            {/* User Management Module */}
            <Route path="/users" element={<ProtectedRoute><UserListPage /></ProtectedRoute>} />

            {/* 404 Fallback */}
            <Route path="*" element={<NotFoundPage />} />
          </Routes>
        </Suspense>
      </main>
    </div>
  );
}
