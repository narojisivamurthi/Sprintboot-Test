import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { api } from '../../services/api';
import { ArrowLeft, Building2, MapPin, Users, Edit2 } from 'lucide-react';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import Badge from '../../components/common/Badge';
import Loader from '../../components/common/Loader';

export default function DepartmentDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [deptData, setDeptData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchDept() {
      try {
        const data = await api.getDepartmentWithEmployees(id);
        setDeptData(data);
      } catch (err) {
        console.error('Failed to load department details:', err);
      } finally {
        setLoading(false);
      }
    }
    fetchDept();
  }, [id]);

  if (loading) return <Loader text="Loading department details and assigned staff..." />;

  if (!deptData) {
    return (
      <Card style={{ textAlign: 'center', padding: '40px' }}>
        <h2>Department Not Found</h2>
        <Button variant="primary" onClick={() => navigate('/departments')} style={{ marginTop: '16px' }}>
          <ArrowLeft size={16} /> Back to Departments
        </Button>
      </Card>
    );
  }

  const { department, employees = [] } = deptData;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Button variant="secondary" onClick={() => navigate('/departments')}>
          <ArrowLeft size={16} /> Back to Departments
        </Button>

        <Button variant="primary" onClick={() => navigate(`/departments/${id}/edit`)}>
          <Edit2 size={16} /> Edit Department
        </Button>
      </div>

      {/* Department Info Header Card */}
      <Card style={{ background: 'linear-gradient(135deg, rgba(30, 41, 59, 0.9) 0%, rgba(15, 23, 42, 0.95) 100%)' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '20px', marginBottom: '16px' }}>
          <div style={{ padding: '16px', borderRadius: '16px', background: 'rgba(99, 102, 241, 0.2)' }}>
            <Building2 size={32} color="#6366f1" />
          </div>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
              <h2 style={{ fontSize: '1.75rem', fontWeight: 800 }}>{department.name}</h2>
              <Badge variant="code">{department.code}</Badge>
            </div>
            <p style={{ color: '#94a3b8', fontSize: '0.95rem', marginTop: '4px' }}>
              {department.description || 'No description available.'}
            </p>
          </div>
        </div>

        <div style={{ display: 'flex', gap: '24px', paddingTop: '16px', borderTop: '1px solid rgba(255, 255, 255, 0.08)', fontSize: '0.875rem', color: '#94a3b8' }}>
          <span style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
            <MapPin size={16} color="#ec4899" /> Location: <strong style={{ color: '#f8fafc' }}>{department.location || 'HQ'}</strong>
          </span>
          <span style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
            <Users size={16} color="#10b981" /> Total Staff: <strong style={{ color: '#f8fafc' }}>{employees.length} Employees</strong>
          </span>
        </div>
      </Card>

      {/* Assigned Employees List */}
      <div className="glass-panel" style={{ overflow: 'hidden' }}>
        <div style={{ padding: '20px 24px', borderBottom: '1px solid rgba(255, 255, 255, 0.05)' }}>
          <h3 style={{ fontSize: '1.1rem', fontWeight: 700 }}>Assigned Employees ({employees.length})</h3>
        </div>

        <div className="table-container">
          <table className="custom-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Employee Name</th>
                <th>Designation</th>
                <th>Salary</th>
                <th>City</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {employees.length === 0 ? (
                <tr>
                  <td colSpan="6" style={{ textAlign: 'center', padding: '30px', color: '#64748b' }}>
                    No employees currently assigned to this department.
                  </td>
                </tr>
              ) : (
                employees.map((emp) => (
                  <tr key={emp.id} style={{ cursor: 'pointer' }} onClick={() => navigate(`/employees/${emp.id}`)}>
                    <td style={{ fontWeight: 700, color: '#6366f1' }}>#{emp.id}</td>
                    <td>
                      <div>
                        <div style={{ fontWeight: 700, color: '#f8fafc' }}>{emp.name}</div>
                        <div style={{ fontSize: '0.78rem', color: '#64748b' }}>{emp.email}</div>
                      </div>
                    </td>
                    <td>{emp.designation ? emp.designation.title : '-'}</td>
                    <td style={{ fontWeight: 700, color: '#10b981' }}>${emp.salary ? emp.salary.toLocaleString() : '0'}</td>
                    <td>{emp.city ? emp.city.name : '-'}</td>
                    <td><Badge variant={emp.status}>{emp.status}</Badge></td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

    </div>
  );
}
