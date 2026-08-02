import React, { useState, useEffect } from 'react';
import { X } from 'lucide-react';

export default function DesignationModal({ isOpen, onClose, onSave, designation }) {
  const [formData, setFormData] = useState({ title: '', code: '' });

  useEffect(() => {
    if (designation) {
      setFormData({ title: designation.title || '', code: designation.code || '' });
    } else {
      setFormData({ title: '', code: '' });
    }
  }, [designation, isOpen]);

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
            {designation ? 'Edit Designation' : 'Add New Designation'}
          </h3>
          <button className="btn btn-secondary btn-icon" onClick={onClose}><X size={18} /></button>
        </div>

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          <div>
            <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#94a3b8', display: 'block', marginBottom: '6px' }}>Designation Title *</label>
            <input type="text" className="input-field" value={formData.title} onChange={(e) => setFormData({ ...formData, title: e.target.value })} placeholder="e.g. Senior Tech Lead" required />
          </div>

          <div>
            <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#94a3b8', display: 'block', marginBottom: '6px' }}>Code (Optional - Auto Generated if blank)</label>
            <input type="text" className="input-field" value={formData.code} onChange={(e) => setFormData({ ...formData, code: e.target.value.toUpperCase() })} placeholder="e.g. STL" />
          </div>

          <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '12px', marginTop: '12px' }}>
            <button type="button" className="btn btn-secondary" onClick={onClose}>Cancel</button>
            <button type="submit" className="btn btn-primary">{designation ? 'Save' : 'Create'}</button>
          </div>
        </form>
      </div>
    </div>
  );
}
