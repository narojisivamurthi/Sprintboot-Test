import React, { useState, useEffect } from 'react';
import { X } from 'lucide-react';

export default function DepartmentModal({ isOpen, onClose, onSave, department }) {
  const [formData, setFormData] = useState({ name: '', code: '', location: 'Corporate HQ' });

  useEffect(() => {
    if (department) {
      setFormData({ name: department.name || '', code: department.code || '', location: department.location || 'Corporate HQ' });
    } else {
      setFormData({ name: '', code: '', location: 'Corporate HQ' });
    }
  }, [department, isOpen]);

  if (!isOpen) return null;

  const handleSubmit = (e) => {
    e.preventDefault();
    onSave(formData);
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
          <h3 style={{ fontSize: '1.25rem', fontWeight: 800 }}>
            {department ? 'Edit Department' : 'Add New Department'}
          </h3>
          <button className="btn btn-secondary btn-icon" onClick={onClose}><X size={18} /></button>
        </div>

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          <div>
            <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#94a3b8', display: 'block', marginBottom: '6px' }}>Department Name *</label>
            <input type="text" className="input-field" value={formData.name} onChange={(e) => setFormData({ ...formData, name: e.target.value })} placeholder="e.g. Engineering" required />
          </div>

          <div>
            <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#94a3b8', display: 'block', marginBottom: '6px' }}>Department Code *</label>
            <input type="text" className="input-field" value={formData.code} onChange={(e) => setFormData({ ...formData, code: e.target.value.toUpperCase() })} placeholder="e.g. ENG" required />
          </div>

          <div>
            <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#94a3b8', display: 'block', marginBottom: '6px' }}>Location</label>
            <input type="text" className="input-field" value={formData.location} onChange={(e) => setFormData({ ...formData, location: e.target.value })} placeholder="Corporate HQ" />
          </div>

          <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '12px', marginTop: '12px' }}>
            <button type="button" className="btn btn-secondary" onClick={onClose}>Cancel</button>
            <button type="submit" className="btn btn-primary">{department ? 'Save' : 'Create'}</button>
          </div>
        </form>
      </div>
    </div>
  );
}
