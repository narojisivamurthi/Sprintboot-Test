import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useEmployees } from '../../hooks/useEmployees';
import { useAppContext } from '../../context/AppContext';
import { Search, Plus, Edit2, Trash2, Eye, ArrowUpDown } from 'lucide-react';
import Button from '../../components/common/Button';
import Badge from '../../components/common/Badge';
import Pagination from '../../components/common/Pagination';
import PageHeader from '../../components/layout/PageHeader';

export default function EmployeeListPage() {
  const navigate = useNavigate();
  const { employees, pagination, loading, loadEmployees, deleteEmployee } = useEmployees();
  const { departments, cities, statuses } = useAppContext();

  const [searchTerm, setSearchTerm] = useState('');
  const [selectedDept, setSelectedDept] = useState('');
  const [selectedCity, setSelectedCity] = useState('');
  const [selectedStatus, setSelectedStatus] = useState('');

  // Filter employees on current page
  const filteredEmployees = employees.filter((emp) => {
    const matchesSearch =
      emp.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      emp.email?.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesDept = selectedDept === '' || emp.department?.name === selectedDept;
    const matchesCity = selectedCity === '' || emp.city?.name === selectedCity;
    const matchesStatus = selectedStatus === '' || emp.status === selectedStatus;
    return matchesSearch && matchesDept && matchesCity && matchesStatus;
  });

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
      
      {/* Page Header */}
      <PageHeader
        title="Employee Directory"
        subtitle={`Managing ${pagination.totalElements || employees.length} employee records across organization`}
        actionButton={
          <Button variant="primary" onClick={() => navigate('/employees/new')}>
            <Plus size={18} /> Add Employee
          </Button>
        }
      />

      {/* Filter Toolbar */}
      <div className="glass-panel" style={{ padding: '20px 24px', display: 'flex', flexWrap: 'wrap', gap: '16px', alignItems: 'center', justifyContent: 'space-between' }}>
        
        {/* Search */}
        <div style={{ position: 'relative', flex: '1 1 240px', maxWidth: '380px' }}>
          <Search size={18} style={{ position: 'absolute', left: '14px', top: '50%', transform: 'translateY(-50%)', color: '#64748b' }} />
          <input
            type="text"
            className="input-field"
            placeholder="Search by name or email..."
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

          <select
            className="input-field"
            value={pagination?.sortBy || 'id'}
            onChange={(e) => loadEmployees(0, pagination?.size || 10, e.target.value, pagination?.sortDir || 'asc')}
            style={{ width: 'auto', minWidth: '120px' }}
          >
            <option value="id">Sort by ID</option>
            <option value="name">Sort by Name</option>
            <option value="salary">Sort by Salary</option>
            <option value="joiningDate">Sort by Date</option>
          </select>

          <Button
            variant="icon"
            onClick={() => loadEmployees(0, pagination?.size || 10, pagination?.sortBy || 'id', pagination?.sortDir === 'asc' ? 'desc' : 'asc')}
            title={`Sort Direction: ${pagination?.sortDir === 'asc' ? 'Ascending' : 'Descending'}`}
          >
            <ArrowUpDown size={16} color="#6366f1" />
          </Button>
        </div>

      </div>

      {/* Table */}
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
                    Loading employee directory...
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
                          <Badge variant="code">{emp.department.code || 'DEPT'}</Badge>
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
                    <td>
                      <Badge variant={emp.status}>{emp.status}</Badge>
                    </td>
                    <td style={{ textAlign: 'right' }}>
                      <div style={{ display: 'inline-flex', gap: '6px' }}>
                        <Button
                          variant="icon"
                          onClick={() => navigate(`/employees/${emp.id}`)}
                          title="View Employee Profile"
                        >
                          <Eye size={16} color="#6366f1" />
                        </Button>

                        <Button
                          variant="icon"
                          onClick={() => navigate(`/employees/${emp.id}/edit`)}
                          title="Edit Employee"
                        >
                          <Edit2 size={16} color="#94a3b8" />
                        </Button>

                        <Button
                          variant="danger"
                          style={{ padding: '8px' }}
                          onClick={() => deleteEmployee(emp.id)}
                          title="Delete Employee"
                        >
                          <Trash2 size={16} />
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        {/* Pagination Bar */}
        <Pagination
          currentPage={pagination.page || 0}
          totalPages={pagination.totalPages || 1}
          pageSize={pagination.size || 10}
          totalElements={pagination.totalElements || 0}
          onPageChange={(page) => loadEmployees(page, pagination.size, pagination.sortBy, pagination.sortDir)}
          onPageSizeChange={(size) => loadEmployees(0, size, pagination.sortBy, pagination.sortDir)}
        />
      </div>

    </div>
  );
}
