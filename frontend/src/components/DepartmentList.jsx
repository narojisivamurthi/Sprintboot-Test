import React, { useState } from 'react';
import { Building2, Plus, Edit2, Trash2, MapPin, Users } from 'lucide-react';

export default function DepartmentList({ departments, onAdd, onEdit, onDelete }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
      
      <div className="glass-panel" style={{ padding: '20px 24px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h2 style={{ fontSize: '1.25rem', fontWeight: 800 }}>Departments Directory</h2>
          <span style={{ fontSize: '0.85rem', color: '#94a3b8' }}>Total {departments.length} registered organizational units</span>
        </div>
        <button className="btn btn-primary" onClick={onAdd}>
          <Plus size={18} /> Add Department
        </button>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '20px' }}>
        {departments.map((dept) => (
          <div key={dept.id} className="glass-panel glass-panel-interactive" style={{ padding: '24px', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '12px' }}>
                <span className="badge badge-code" style={{ fontSize: '0.85rem', padding: '6px 12px' }}>
                  {dept.code}
                </span>
                <div style={{ display: 'flex', gap: '6px' }}>
                  <button className="btn btn-secondary btn-icon" onClick={() => onEdit(dept)}>
                    <Edit2 size={16} color="#94a3b8" />
                  </button>
                  <button className="btn btn-danger btn-icon" onClick={() => onDelete(dept.id)}>
                    <Trash2 size={16} />
                  </button>
                </div>
              </div>

              <h3 style={{ fontSize: '1.2rem', fontWeight: 800, color: '#f8fafc', marginBottom: '8px' }}>
                {dept.name}
              </h3>

              <div style={{ display: 'flex', alignItems: 'center', gap: '6px', fontSize: '0.85rem', color: '#94a3b8' }}>
                <MapPin size={16} color="#6366f1" />
                <span>{dept.location || 'Corporate HQ'}</span>
              </div>
            </div>

            <div style={{ marginTop: '20px', paddingTop: '16px', borderTop: '1px solid rgba(255, 255, 255, 0.05)', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <span style={{ fontSize: '0.78rem', color: '#64748b' }}>Department ID: #{dept.id}</span>
            </div>
          </div>
        ))}
      </div>

    </div>
  );
}
