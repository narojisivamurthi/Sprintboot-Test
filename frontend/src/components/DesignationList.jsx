import React from 'react';
import { Briefcase, Plus, Edit2, Trash2 } from 'lucide-react';

export default function DesignationList({ designations, onAdd, onEdit, onDelete }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
      
      <div className="glass-panel" style={{ padding: '20px 24px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h2 style={{ fontSize: '1.25rem', fontWeight: 800 }}>Designations & Job Titles</h2>
          <span style={{ fontSize: '0.85rem', color: '#94a3b8' }}>Total {designations.length} active job positions</span>
        </div>
        <button className="btn btn-primary" onClick={onAdd}>
          <Plus size={18} /> Add Designation
        </button>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: '20px' }}>
        {designations.map((d) => (
          <div key={d.id} className="glass-panel glass-panel-interactive" style={{ padding: '24px', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '12px' }}>
                <span className="badge badge-code" style={{ fontSize: '0.85rem', padding: '6px 12px' }}>
                  {d.code}
                </span>
                <div style={{ display: 'flex', gap: '6px' }}>
                  <button className="btn btn-secondary btn-icon" onClick={() => onEdit(d)}>
                    <Edit2 size={16} color="#94a3b8" />
                  </button>
                  <button className="btn btn-danger btn-icon" onClick={() => onDelete(d.id)}>
                    <Trash2 size={16} />
                  </button>
                </div>
              </div>

              <h3 style={{ fontSize: '1.15rem', fontWeight: 800, color: '#f8fafc', marginBottom: '8px' }}>
                {d.title}
              </h3>
            </div>

            <div style={{ marginTop: '16px', paddingTop: '14px', borderTop: '1px solid rgba(255, 255, 255, 0.05)', fontSize: '0.78rem', color: '#64748b' }}>
              Designation ID: #{d.id}
            </div>
          </div>
        ))}
      </div>

    </div>
  );
}
