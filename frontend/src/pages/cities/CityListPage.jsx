import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAppContext } from '../../context/AppContext';
import { useToast } from '../../context/ToastContext';
import { api } from '../../services/api';
import { MapPin, Plus, Edit2, Trash2 } from 'lucide-react';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import PageHeader from '../../components/layout/PageHeader';

export default function CityListPage() {
  const navigate = useNavigate();
  const { cities, loadLookups } = useAppContext();
  const { showToast } = useToast();

  const handleDelete = async (id) => {
    if (window.confirm(`Delete City #${id}?`)) {
      try {
        await api.deleteCity(id);
        showToast(`City #${id} deleted.`);
        loadLookups();
      } catch (err) {
        showToast(err.message || 'Failed to delete city', 'error');
      }
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
      
      <PageHeader
        title="Office Locations & Cities"
        subtitle={`Registered ${cities.length} global office cities`}
        actionButton={
          <Button variant="primary" onClick={() => navigate('/cities/new')}>
            <Plus size={18} /> Add City
          </Button>
        }
      />

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: '20px' }}>
        {cities.map((city) => (
          <Card key={city.id} interactive style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '12px' }}>
                <div style={{ padding: '8px', borderRadius: '10px', background: 'rgba(236, 72, 153, 0.15)' }}>
                  <MapPin size={20} color="#ec4899" />
                </div>
                <div style={{ display: 'flex', gap: '6px' }}>
                  <Button variant="icon" onClick={() => navigate(`/cities/${city.id}/edit`)} title="Edit City">
                    <Edit2 size={16} color="#94a3b8" />
                  </Button>
                  <Button variant="danger" style={{ padding: '8px' }} onClick={() => handleDelete(city.id)} title="Delete City">
                    <Trash2 size={16} />
                  </Button>
                </div>
              </div>

              <h3 style={{ fontSize: '1.2rem', fontWeight: 800, color: '#f8fafc', marginBottom: '4px' }}>
                {city.name}
              </h3>

              <div style={{ fontSize: '0.85rem', color: '#94a3b8' }}>
                {city.state || 'N/A'}, {city.country || 'USA'}
              </div>
            </div>

            <div style={{ marginTop: '16px', paddingTop: '14px', borderTop: '1px solid rgba(255, 255, 255, 0.05)', fontSize: '0.78rem', color: '#64748b' }}>
              Location ID: #{city.id}
            </div>
          </Card>
        ))}
      </div>

    </div>
  );
}
