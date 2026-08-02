import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { api } from '../../services/api';
import { ArrowLeft, Edit2, Mail, Calendar, DollarSign, Building2, Briefcase, MapPin, Shield } from 'lucide-react';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import Badge from '../../components/common/Badge';
import Loader from '../../components/common/Loader';

export default function EmployeeDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [employee, setEmployee] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchEmployee() {
      try {
        const data = await api.getEmployeeById(id);
        setEmployee(data);
      } catch (err) {
        console.error('Failed to load employee details:', err);
      } finally {
        setLoading(false);
      }
    }
    fetchEmployee();
  }, [id]);

  if (loading) return <Loader text="Loading employee profile..." />;

  if (!employee) {
    return (
      <Card style={{ textAlign: 'center', padding: '40px' }}>
        <h2>Employee Record Not Found</h2>
        <p style={{ color: '#94a3b8', margin: '16px 0' }}>No employee exists with ID #{id}.</p>
        <Button variant="primary" onClick={() => navigate('/employees')}>
          <ArrowLeft size={16} /> Return to Directory
        </Button>
      </Card>
    );
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px', maxWidth: '900px', margin: '0 auto' }}>
      
      {/* Top Bar */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Button variant="secondary" onClick={() => navigate('/employees')}>
          <ArrowLeft size={16} /> Back to Directory
        </Button>

        <Button variant="primary" onClick={() => navigate(`/employees/${id}/edit`)}>
          <Edit2 size={16} /> Edit Employee Profile
        </Button>
      </div>

      {/* Main Profile Card */}
      <Card style={{ background: 'linear-gradient(135deg, rgba(30, 41, 59, 0.9) 0%, rgba(15, 23, 42, 0.95) 100%)' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '20px', marginBottom: '24px' }}>
          <div style={{ width: '64px', height: '64px', borderRadius: '50%', background: 'linear-gradient(135deg, #6366f1, #4f46e5)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '1.5rem', fontWeight: 800, color: '#ffffff' }}>
            {employee.name ? employee.name.charAt(0) : 'E'}
          </div>

          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
              <h2 style={{ fontSize: '1.75rem', fontWeight: 800 }}>{employee.name}</h2>
              <Badge variant={employee.status}>{employee.status}</Badge>
            </div>
            <div style={{ color: '#94a3b8', fontSize: '0.9rem', marginTop: '4px' }}>
              Employee Record #{employee.id}
            </div>
          </div>
        </div>

        {/* Profile Info Grid */}
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: '20px', paddingTop: '20px', borderTop: '1px solid rgba(255, 255, 255, 0.08)' }}>
          
          <div>
            <span style={{ fontSize: '0.78rem', color: '#64748b', fontWeight: 700, textTransform: 'uppercase', display: 'flex', alignItems: 'center', gap: '6px' }}>
              <Mail size={14} color="#6366f1" /> Email Address
            </span>
            <div style={{ fontSize: '1rem', fontWeight: 600, color: '#f8fafc', marginTop: '4px' }}>
              {employee.email}
            </div>
          </div>

          <div>
            <span style={{ fontSize: '0.78rem', color: '#64748b', fontWeight: 700, textTransform: 'uppercase', display: 'flex', alignItems: 'center', gap: '6px' }}>
              <Building2 size={14} color="#3b82f6" /> Department
            </span>
            <div style={{ fontSize: '1rem', fontWeight: 600, color: '#f8fafc', marginTop: '4px' }}>
              {employee.department ? `${employee.department.name} (${employee.department.code})` : 'Unassigned'}
            </div>
          </div>

          <div>
            <span style={{ fontSize: '0.78rem', color: '#64748b', fontWeight: 700, textTransform: 'uppercase', display: 'flex', alignItems: 'center', gap: '6px' }}>
              <Briefcase size={14} color="#8b5cf6" /> Designation
            </span>
            <div style={{ fontSize: '1rem', fontWeight: 600, color: '#f8fafc', marginTop: '4px' }}>
              {employee.designation ? employee.designation.title : '-'}
            </div>
          </div>

          <div>
            <span style={{ fontSize: '0.78rem', color: '#64748b', fontWeight: 700, textTransform: 'uppercase', display: 'flex', alignItems: 'center', gap: '6px' }}>
              <DollarSign size={14} color="#10b981" /> Annual Compensation
            </span>
            <div style={{ fontSize: '1rem', fontWeight: 700, color: '#10b981', marginTop: '4px' }}>
              ${employee.salary ? employee.salary.toLocaleString() : '0'} / year
            </div>
          </div>

          <div>
            <span style={{ fontSize: '0.78rem', color: '#64748b', fontWeight: 700, textTransform: 'uppercase', display: 'flex', alignItems: 'center', gap: '6px' }}>
              <Calendar size={14} color="#f59e0b" /> Joining Date
            </span>
            <div style={{ fontSize: '1rem', fontWeight: 600, color: '#f8fafc', marginTop: '4px' }}>
              {employee.joiningDate || 'N/A'}
            </div>
          </div>

          <div>
            <span style={{ fontSize: '0.78rem', color: '#64748b', fontWeight: 700, textTransform: 'uppercase', display: 'flex', alignItems: 'center', gap: '6px' }}>
              <MapPin size={14} color="#ec4899" /> Office City
            </span>
            <div style={{ fontSize: '1rem', fontWeight: 600, color: '#f8fafc', marginTop: '4px' }}>
              {employee.city ? `${employee.city.name}, ${employee.city.state || ''} ${employee.city.country || ''}` : '-'}
            </div>
          </div>

        </div>
      </Card>

    </div>
  );
}
