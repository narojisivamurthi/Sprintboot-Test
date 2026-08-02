import React, { useState, useEffect } from 'react';
import { X } from 'lucide-react';

export default function CityModal({ isOpen, onClose, onSave, city }) {
  const [formData, setFormData] = useState({ name: '', state: 'N/A', country: 'USA' });

  useEffect(() => {
    if (city) {
      setFormData({ name: city.name || '', state: city.state || 'N/A', country: city.country || 'USA' });
    } else {
      setFormData({ name: '', state: 'N/A', country: 'USA' });
    }
  }, [city, isOpen]);

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
            {city ? 'Edit City Location' : 'Add New City Location'}
          </h3>
          <button className="btn btn-secondary btn-icon" onClick={onClose}><X size={18} /></button>
        </div>

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          <div>
            <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#94a3b8', display: 'block', marginBottom: '6px' }}>City Name *</label>
            <input type="text" className="input-field" value={formData.name} onChange={(e) => setFormData({ ...formData, name: e.target.value })} placeholder="e.g. Seattle" required />
          </div>

          <div>
            <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#94a3b8', display: 'block', marginBottom: '6px' }}>State / Region</label>
            <input type="text" className="input-field" value={formData.state} onChange={(e) => setFormData({ ...formData, state: e.target.value })} placeholder="WA" />
          </div>

          <div>
            <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#94a3b8', display: 'block', marginBottom: '6px' }}>Country</label>
            <input type="text" className="input-field" value={formData.country} onChange={(e) => setFormData({ ...formData, country: e.target.value })} placeholder="USA" />
          </div>

          <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '12px', marginTop: '12px' }}>
            <button type="button" className="btn btn-secondary" onClick={onClose}>Cancel</button>
            <button type="submit" className="btn btn-primary">{city ? 'Save' : 'Create'}</button>
          </div>
        </form>
      </div>
    </div>
  );
}
