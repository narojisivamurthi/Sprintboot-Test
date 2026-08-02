import React from 'react';
import { MapPin, Plus, Edit2, Trash2 } from 'lucide-react';

export default function CityList({ cities, onAdd, onEdit, onDelete }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
      
      <div className="glass-panel" style={{ padding: '20px 24px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h2 style={{ fontSize: '1.25rem', fontWeight: 800 }}>Office & City Locations</h2>
          <span style={{ fontSize: '0.85rem', color: '#94a3b8' }}>Total {cities.length} global cities registered</span>
        </div>
        <button className="btn btn-primary" onClick={onAdd}>
          <Plus size={18} /> Add City
        </button>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: '20px' }}>
        {cities.map((city) => (
          <div key={city.id} className="glass-panel glass-panel-interactive" style={{ padding: '24px', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '12px' }}>
                <div style={{ padding: '8px', borderRadius: '10px', background: 'rgba(236, 72, 153, 0.15)' }}>
                  <MapPin size={20} color="#ec4899" />
                </div>
                <div style={{ display: 'flex', gap: '6px' }}>
                  <button className="btn btn-secondary btn-icon" onClick={() => onEdit(city)}>
                    <Edit2 size={16} color="#94a3b8" />
                  </button>
                  <button className="btn btn-danger btn-icon" onClick={() => onDelete(city.id)}>
                    <Trash2 size={16} />
                  </button>
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
          </div>
        ))}
      </div>

    </div>
  );
}
