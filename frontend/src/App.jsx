import React, { lazy, Suspense } from 'react';
import { Routes, Route } from 'react-router-dom';
import Navbar from './components/layout/Navbar';
import Loader from './components/common/Loader';

// Lazy-loaded Multi-Page Components for Code Splitting & Performance Optimization
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
            {/* Dashboard */}
            <Route path="/" element={<DashboardPage />} />

            {/* Employee Module */}
            <Route path="/employees" element={<EmployeeListPage />} />
            <Route path="/employees/new" element={<EmployeeFormPage />} />
            <Route path="/employees/:id" element={<EmployeeDetailPage />} />
            <Route path="/employees/:id/edit" element={<EmployeeFormPage />} />

            {/* Department Module */}
            <Route path="/departments" element={<DepartmentListPage />} />
            <Route path="/departments/new" element={<DepartmentFormPage />} />
            <Route path="/departments/:id" element={<DepartmentDetailPage />} />
            <Route path="/departments/:id/edit" element={<DepartmentFormPage />} />

            {/* Designation Module */}
            <Route path="/designations" element={<DesignationListPage />} />
            <Route path="/designations/new" element={<DesignationFormPage />} />
            <Route path="/designations/:id/edit" element={<DesignationFormPage />} />

            {/* City Module */}
            <Route path="/cities" element={<CityListPage />} />
            <Route path="/cities/new" element={<CityFormPage />} />
            <Route path="/cities/:id/edit" element={<CityFormPage />} />

            {/* 404 Fallback */}
            <Route path="*" element={<NotFoundPage />} />
          </Routes>
        </Suspense>
      </main>
    </div>
  );
}
