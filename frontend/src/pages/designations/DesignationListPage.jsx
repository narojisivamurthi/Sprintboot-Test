import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAppContext } from '../../context/AppContext';
import { useToast } from '../../context/ToastContext';
import { api } from '../../services/api';
import { Briefcase, Plus, Edit2, Trash2 } from 'lucide-react';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import Badge from '../../components/common/Badge';
import PageHeader from '../../components/layout/PageHeader';

export default function DesignationListPage() {
  const navigate = useNavigate();
  const { designations, loadLookups } = useAppContext();
  const { showToast } = useToast();

  const handleDelete = async (id) => {
    if (window.confirm(`Delete Designation #${id}?`)) {
      try {
        await api.deleteDesignation(id);
        showToast(`Designation #${id} deleted.`);
        loadLookups();
      } catch (err) {
        showToast(err.message || 'Failed to delete designation', 'error');
      }
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
      
      <PageHeader
        title="Designation & Job Titles Directory"
        subtitle={`Managing ${designations.length} job titles & roles`}
        actionButton={
          <Button variant="primary" onClick={() => navigate('/designations/new')}>
            <Plus size={18} /> Add Designation
          </Button>
        }
      />

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: '20px' }}>
        {designations.map((desig) => (
          <Card key={desig.id} interactive style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '12px' }}>
                <div style={{ padding: '8px', borderRadius: '10px', background: 'rgba(139, 92, 246, 0.15)' }}>
                  <Briefcase size={20} color="#8b5cf6" />
                </div>
                <div style={{ display: 'flex', gap: '6px' }}>
                  <Button variant="icon" onClick={() => navigate(`/designations/${desig.id}/edit`)} title="Edit Designation">
                    <Edit2 size={16} color="#94a3b8" />
                  </Button>
                  <Button variant="danger" style={{ padding: '8px' }} onClick={() => handleDelete(desig.id)} title="Delete Designation">
                    <Trash2 size={16} />
                  </Button>
                </div>
              </div>

              <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '4px' }}>
                <h3 style={{ fontSize: '1.15rem', fontWeight: 800, color: '#f8fafc' }}>
                  {desig.title}
                </h3>
                <Badge variant="code">{desig.code}</Badge>
              </div>
            </div>

            <div style={{ marginTop: '16px', paddingTop: '14px', borderTop: '1px solid rgba(255, 255, 255, 0.05)', fontSize: '0.78rem', color: '#64748b' }}>
              Designation ID: #{desig.id}
            </div>
          </Card>
        ))}
      </div>

    </div>
  );
}
