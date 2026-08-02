import React, { useState } from 'react';
import { Search, Plus, Edit2, Trash2, ChevronLeft, ChevronRight, ChevronsLeft, ChevronsRight, ArrowUpDown } from 'lucide-react';

export default function EmployeeList({
  employees,
  pagination,
  departments,
  cities,
  statuses,
  onPageChange,
  onPageSizeChange,
  onSortChange,
  onOpenAddModal,
  onOpenEditModal,
  onDeleteEmployee,
  loading
}) {
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedDept, setSelectedDept] = useState('');
  const [selectedCity, setSelectedCity] = useState('');
  const [selectedStatus, setSelectedStatus] = useState('');

  // Client-side filtering on current fetched page
  const filteredEmployees = employees.filter((emp) => {
    const matchesSearch =
      emp.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      emp.email?.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesDept = selectedDept === '' || emp.department?.name === selectedDept;
    const matchesCity = selectedCity === '' || emp.city?.name === selectedCity;
    const matchesStatus = selectedStatus === '' || emp.status === selectedStatus;
    return matchesSearch && matchesDept && matchesCity && matchesStatus;
  });

  const getStatusBadge = (status) => {
    switch (status) {
      case 'ACTIVE':
        return <span className="badge badge-active">● Active</span>;
      case 'INACTIVE':
        return <span className="badge badge-inactive">● Inactive</span>;
      case 'ON_LEAVE':
        return <span className="badge badge-leave">● On Leave</span>;
      default:
        return <span className="badge badge-code">{status}</span>;
    }
  };

  const totalPages = pagination?.totalPages || 1;
  const currentPage = pagination?.page || 0;
  const pageSize = pagination?.size || 10;
  const totalElements = pagination?.totalElements || employees.length;

  // Generate dynamic 5-page number window around current page
  const getPageNumbers = () => {
    const pages = [];
    const maxButtons = 5;
    let startPage = Math.max(0, currentPage - Math.floor(maxButtons / 2));
    let endPage = startPage + maxButtons - 1;

    if (endPage >= totalPages) {
      endPage = totalPages - 1;
      startPage = Math.max(0, endPage - maxButtons + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    return pages;
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
      
      {/* Action Header & Search Controls */}
      <div className="glass-panel" style={{ padding: '20px 24px', display: 'flex', flexWrap: 'wrap', gap: '16px', alignItems: 'center', justifyContent: 'space-between' }}>
        
        {/* Search Input */}
        <div style={{ position: 'relative', flex: '1 1 240px', maxWidth: '380px' }}>
          <Search size={18} style={{ position: 'absolute', left: '14px', top: '50%', transform: 'translateY(-50%)', color: '#64748b' }} />
          <input
            type="text"
            className="input-field"
            placeholder="Search employee by name or email..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            style={{ paddingLeft: '40px' }}
          />
        </div>

        {/* Filters & Sorting */}
        <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap', alignItems: 'center' }}>
          
          <select
            className="input-field"
            value={selectedDept}
            onChange={(e) => setSelectedDept(e.target.value)}
            style={{ width: 'auto', minWidth: '140px' }}
          >
            <option value="">All Departments</option>
            {departments.map((d) => (
              <option key={d.id} value={d.name}>{d.name}</option>
            ))}
          </select>

          <select
            className="input-field"
            value={selectedCity}
            onChange={(e) => setSelectedCity(e.target.value)}
            style={{ width: 'auto', minWidth: '130px' }}
          >
            <option value="">All Cities</option>
            {cities.map((c) => (
              <option key={c.id} value={c.name}>{c.name}</option>
            ))}
          </select>

          <select
            className="input-field"
            value={selectedStatus}
            onChange={(e) => setSelectedStatus(e.target.value)}
            style={{ width: 'auto', minWidth: '130px' }}
          >
            <option value="">All Statuses</option>
            {statuses.map((s) => (
              <option key={s} value={s}>{s}</option>
            ))}
          </select>

          {/* Sort By Selector */}
          <select
            className="input-field"
            value={pagination?.sortBy || 'id'}
            onChange={(e) => onSortChange(e.target.value, pagination?.sortDir || 'asc')}
            style={{ width: 'auto', minWidth: '120px' }}
          >
            <option value="id">Sort by ID</option>
            <option value="name">Sort by Name</option>
            <option value="salary">Sort by Salary</option>
            <option value="joiningDate">Sort by Date</option>
          </select>

          {/* Sort Dir Toggle */}
          <button
            className="btn btn-secondary btn-icon"
            onClick={() => onSortChange(pagination?.sortBy || 'id', pagination?.sortDir === 'asc' ? 'desc' : 'asc')}
            title={`Sort Direction: ${pagination?.sortDir === 'asc' ? 'Ascending' : 'Descending'}`}
          >
            <ArrowUpDown size={16} color="#6366f1" />
          </button>

          <button className="btn btn-primary" onClick={onOpenAddModal}>
            <Plus size={18} /> Add Employee
          </button>
        </div>

      </div>

      {/* Employee Table */}
      <div className="glass-panel" style={{ overflow: 'hidden' }}>
        <div className="table-container">
          <table className="custom-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Employee Name</th>
                <th>Department</th>
                <th>Designation</th>
                <th>Salary</th>
                <th>Joining Date</th>
                <th>City</th>
                <th>Status</th>
                <th style={{ textAlign: 'right' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr>
                  <td colSpan="9" style={{ textAlign: 'center', padding: '40px', color: '#94a3b8' }}>
                    Loading employee records...
                  </td>
                </tr>
              ) : filteredEmployees.length === 0 ? (
                <tr>
                  <td colSpan="9" style={{ textAlign: 'center', padding: '40px', color: '#64748b' }}>
                    No employee records match your filter criteria.
                  </td>
                </tr>
              ) : (
                filteredEmployees.map((emp) => (
                  <tr key={emp.id}>
                    <td style={{ fontWeight: 700, color: '#6366f1' }}>#{emp.id}</td>
                    <td>
                      <div>
                        <div style={{ fontWeight: 700, color: '#f8fafc' }}>{emp.name}</div>
                        <div style={{ fontSize: '0.78rem', color: '#64748b' }}>{emp.email}</div>
                      </div>
                    </td>
                    <td>
                      {emp.department ? (
                        <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                          <span className="badge badge-code">{emp.department.code || 'DEPT'}</span>
                          <span style={{ fontSize: '0.85rem' }}>{emp.department.name}</span>
                        </div>
                      ) : (
                        <span style={{ color: '#64748b' }}>Unassigned</span>
                      )}
                    </td>
                    <td>
                      {emp.designation ? (
                        <span style={{ fontWeight: 600, color: '#e2e8f0' }}>{emp.designation.title}</span>
                      ) : (
                        <span style={{ color: '#64748b' }}>-</span>
                      )}
                    </td>
                    <td style={{ fontWeight: 700, color: '#10b981' }}>
                      ${emp.salary ? emp.salary.toLocaleString() : '0'}
                    </td>
                    <td style={{ fontSize: '0.85rem', color: '#94a3b8' }}>
                      {emp.joiningDate || 'N/A'}
                    </td>
                    <td>
                      {emp.city ? (
                        <span style={{ fontSize: '0.85rem', color: '#cbd5e1' }}>{emp.city.name}</span>
                      ) : (
                        <span style={{ color: '#64748b' }}>-</span>
                      )}
                    </td>
                    <td>{getStatusBadge(emp.status)}</td>
                    <td style={{ textAlign: 'right' }}>
                      <div style={{ display: 'inline-flex', gap: '6px' }}>
                        <button
                          className="btn btn-secondary btn-icon"
                          onClick={() => onOpenEditModal(emp)}
                          title="Edit Employee"
                        >
                          <Edit2 size={16} color="#94a3b8" />
                        </button>
                        <button
                          className="btn btn-danger btn-icon"
                          onClick={() => onDeleteEmployee(emp.id)}
                          title="Delete Employee"
                        >
                          <Trash2 size={16} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        {/* Enhanced Multi-Button Page Navigation Bar */}
        <div style={{ padding: '16px 24px', borderTop: '1px solid rgba(255, 255, 255, 0.05)', display: 'flex', flexWrap: 'wrap', gap: '16px', alignItems: 'center', justifyContent: 'space-between' }}>
          
          {/* Page Info & Items Per Page Selector */}
          <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
            <span style={{ fontSize: '0.85rem', color: '#94a3b8' }}>
              Showing Page <strong>{currentPage + 1}</strong> of <strong>{totalPages}</strong> ({totalElements} Total Employees)
            </span>

            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <span style={{ fontSize: '0.8rem', color: '#64748b' }}>Per page:</span>
              <select
                className="input-field"
                value={pageSize}
                onChange={(e) => onPageSizeChange(parseInt(e.target.value, 10))}
                style={{ width: 'auto', padding: '4px 8px', fontSize: '0.8rem' }}
              >
                <option value={10}>10</option>
                <option value={25}>25</option>
                <option value={50}>50</option>
                <option value={100}>100</option>
              </select>
            </div>
          </div>

          {/* Dynamic 5-Page Number Buttons */}
          <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
            {/* First Page Button */}
            <button
              className="btn btn-secondary btn-icon"
              disabled={currentPage === 0}
              onClick={() => onPageChange(0)}
              title="First Page"
              style={{ opacity: currentPage === 0 ? 0.3 : 1 }}
            >
              <ChevronsLeft size={16} />
            </button>

            {/* Prev Page Button */}
            <button
              className="btn btn-secondary"
              disabled={currentPage === 0}
              onClick={() => onPageChange(currentPage - 1)}
              style={{ opacity: currentPage === 0 ? 0.3 : 1, padding: '6px 12px', fontSize: '0.8rem' }}
            >
              <ChevronLeft size={16} /> Prev
            </button>

            {/* Render up to 5 clickable page buttons */}
            {getPageNumbers().map((pNum) => (
              <button
                key={pNum}
                className={`btn ${pNum === currentPage ? 'btn-primary' : 'btn-secondary'}`}
                onClick={() => onPageChange(pNum)}
                style={{
                  padding: '6px 12px',
                  fontSize: '0.82rem',
                  fontWeight: pNum === currentPage ? 700 : 600,
                  minWidth: '36px',
                  justifyContent: 'center',
                  background: pNum === currentPage ? 'linear-gradient(135deg, #6366f1, #4f46e5)' : 'rgba(255, 255, 255, 0.05)',
                  color: pNum === currentPage ? '#ffffff' : '#94a3b8',
                  border: pNum === currentPage ? 'none' : '1px solid rgba(255, 255, 255, 0.08)'
                }}
              >
                {pNum + 1}
              </button>
            ))}

            {/* Next Page Button */}
            <button
              className="btn btn-secondary"
              disabled={currentPage >= totalPages - 1}
              onClick={() => onPageChange(currentPage + 1)}
              style={{ opacity: currentPage >= totalPages - 1 ? 0.3 : 1, padding: '6px 12px', fontSize: '0.8rem' }}
            >
              Next <ChevronRight size={16} />
            </button>

            {/* Last Page Button */}
            <button
              className="btn btn-secondary btn-icon"
              disabled={currentPage >= totalPages - 1}
              onClick={() => onPageChange(totalPages - 1)}
              title="Last Page"
              style={{ opacity: currentPage >= totalPages - 1 ? 0.3 : 1 }}
            >
              <ChevronsRight size={16} />
            </button>
          </div>

        </div>
      </div>

    </div>
  );
}
