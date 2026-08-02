import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAppContext } from '../../context/AppContext';
import { useToast } from '../../context/ToastContext';
import { api } from '../../services/api';
import { Building2, Plus, Edit2, Trash2, Eye } from 'lucide-react';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import Badge from '../../components/common/Badge';
import PageHeader from '../../components/layout/PageHeader';

export default function DepartmentListPage() {
  const navigate = useNavigate();
  const { departments, loadLookups } = useAppContext();
  const { showToast } = useToast();

  const handleDelete = async (id) => {
    if (window.confirm(`Delete Department #${id}?`)) {
      try {
        await api.deleteDepartment(id);
        showToast(`Department #${id} deleted.`);
        loadLookups();
      } catch (err) {
        showToast(err.message || 'Failed to delete department', 'error');
      }
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
      
      <PageHeader
        title="Departments Management"
        subtitle={`Total ${departments.length} corporate business units active`}
        actionButton={
          <Button variant="primary" onClick={() => navigate('/departments/new')}>
            <Plus size={18} /> Add Department
          </Button>
        }
      />

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: '20px' }}>
        {departments.map((dept) => (
          <Card key={dept.id} interactive style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '16px' }}>
                <div style={{ padding: '10px', borderRadius: '12px', background: 'rgba(99, 102, 241, 0.15)' }}>
                  <Building2 size={24} color="#6366f1" />
                </div>
                <div style={{ display: 'flex', gap: '6px' }}>
                  <Button variant="icon" onClick={() => navigate(`/departments/${dept.id}`)} title="View Department & Assigned Employees">
                    <Eye size={16} color="#6366f1" />
                  </Button>
                  <Button variant="icon" onClick={() => navigate(`/departments/${dept.id}/edit`)} title="Edit Department">
                    <Edit2 size={16} color="#94a3b8" />
                  </Button>
                  <Button variant="danger" style={{ padding: '8px' }} onClick={() => handleDelete(dept.id)} title="Delete Department">
                    <Trash2 size={16} />
                  </Button>
                </div>
              </div>

              <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '6px' }}>
                <h3 style={{ fontSize: '1.25rem', fontWeight: 800, color: '#f8fafc' }}>
                  {dept.name}
                </h3>
                <Badge variant="code">{dept.code}</Badge>
              </div>

              <p style={{ fontSize: '0.85rem', color: '#94a3b8', lineHeight: 1.5 }}>
                {dept.description || 'No detailed description specified.'}
              </p>
            </div>

            <div style={{ marginTop: '20px', paddingTop: '16px', borderTop: '1px solid rgba(255, 255, 255, 0.05)', display: 'flex', justifyContent: 'space-between', alignItems: 'center', fontSize: '0.78rem', color: '#64748b' }}>
              <span>HQ: <strong style={{ color: '#cbd5e1' }}>{dept.location || 'HQ'}</strong></span>
              <span>Unit ID: #{dept.id}</span>
            </div>
          </Card>
        ))}
      </div>

    </div>
  );
}
