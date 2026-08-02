import React, { useState, useEffect } from 'react';
import { X, User, Mail, Building2, Briefcase, DollarSign, Calendar, MapPin, Activity } from 'lucide-react';

export default function EmployeeModal({ isOpen, onClose, onSave, employee, departments, designations, cities, statuses }) {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    departmentName: '',
    designationTitle: '',
    salary: 60000,
    joiningDate: new Date().toISOString().split('T')[0],
    cityName: '',
    status: 'ACTIVE'
  });

  const [error, setError] = useState('');

  useEffect(() => {
    if (employee) {
      setFormData({
        name: employee.name || '',
        email: employee.email || '',
        departmentName: employee.department?.name || '',
        designationTitle: employee.designation?.title || '',
        salary: employee.salary || 60000,
        joiningDate: employee.joiningDate || new Date().toISOString().split('T')[0],
        cityName: employee.city?.name || '',
        status: employee.status || 'ACTIVE'
      });
    } else {
      setFormData({
        name: '',
        email: '',
        departmentName: departments.length > 0 ? departments[0].name : '',
        designationTitle: designations.length > 0 ? designations[0].title : '',
        salary: 75000,
        joiningDate: new Date().toISOString().split('T')[0],
        cityName: cities.length > 0 ? cities[0].name : '',
        status: 'ACTIVE'
      });
    }
    setError('');
  }, [employee, isOpen, departments, designations, cities]);

  if (!isOpen) return null;

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!formData.name.trim() || !formData.email.trim()) {
      setError('Name and Email are required.');
      return;
    }
    onSave(formData);
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        
        {/* Header */}
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
          <div>
            <h3 style={{ fontSize: '1.25rem', fontWeight: 800 }}>
              {employee ? 'Edit Employee Record' : 'Add New Employee'}
            </h3>
            <span style={{ fontSize: '0.8rem', color: '#94a3b8' }}>
              {employee ? `Updating record #${employee.id}` : 'Fill in employee parameters'}
            </span>
          </div>
          <button className="btn btn-secondary btn-icon" onClick={onClose}>
            <X size={18} />
          </button>
        </div>

        {error && (
          <div style={{ padding: '12px 16px', background: 'rgba(239, 68, 68, 0.15)', border: '1px solid rgba(239, 68, 68, 0.3)', borderRadius: '10px', color: '#fca5a5', fontSize: '0.85rem', marginBottom: '20px' }}>
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          
          {/* Name & Email */}
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
            <div>
              <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#94a3b8', display: 'block', marginBottom: '6px' }}>
                Full Name *
              </label>
              <input
                type="text"
                className="input-field"
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                placeholder="e.g. Siva Naroji"
                required
              />
            </div>
            <div>
              <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#94a3b8', display: 'block', marginBottom: '6px' }}>
                Email Address *
              </label>
              <input
                type="email"
                className="input-field"
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                placeholder="siva@company.com"
                required
              />
            </div>
          </div>

          {/* Department & Designation */}
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
            <div>
              <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#94a3b8', display: 'block', marginBottom: '6px' }}>
                Department *
              </label>
              <select
                className="input-field"
                value={formData.departmentName}
                onChange={(e) => setFormData({ ...formData, departmentName: e.target.value })}
                required
              >
                <option value="">Select Department</option>
                {departments.map((d) => (
                  <option key={d.id} value={d.name}>{d.name} ({d.code})</option>
                ))}
              </select>
            </div>

            <div>
              <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#94a3b8', display: 'block', marginBottom: '6px' }}>
                Designation / Job Title *
              </label>
              <select
                className="input-field"
                value={formData.designationTitle}
                onChange={(e) => setFormData({ ...formData, designationTitle: e.target.value })}
                required
              >
                <option value="">Select Designation</option>
                {designations.map((d) => (
                  <option key={d.id} value={d.title}>{d.title}</option>
                ))}
              </select>
            </div>
          </div>

          {/* Salary & Joining Date */}
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
            <div>
              <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#94a3b8', display: 'block', marginBottom: '6px' }}>
                Annual Salary ($) *
              </label>
              <input
                type="number"
                className="input-field"
                value={formData.salary}
                onChange={(e) => setFormData({ ...formData, salary: parseFloat(e.target.value) || 0 })}
                min="1000"
                step="500"
                required
              />
            </div>

            <div>
              <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#94a3b8', display: 'block', marginBottom: '6px' }}>
                Joining Date
              </label>
              <input
                type="date"
                className="input-field"
                value={formData.joiningDate}
                onChange={(e) => setFormData({ ...formData, joiningDate: e.target.value })}
              />
            </div>
          </div>

          {/* City & Status */}
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
            <div>
              <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#94a3b8', display: 'block', marginBottom: '6px' }}>
                Office / City Location
              </label>
              <select
                className="input-field"
                value={formData.cityName}
                onChange={(e) => setFormData({ ...formData, cityName: e.target.value })}
              >
                <option value="">Select City</option>
                {cities.map((c) => (
                  <option key={c.id} value={c.name}>{c.name}</option>
                ))}
              </select>
            </div>

            <div>
              <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#94a3b8', display: 'block', marginBottom: '6px' }}>
                Employee Status *
              </label>
              <select
                className="input-field"
                value={formData.status}
                onChange={(e) => setFormData({ ...formData, status: e.target.value })}
                required
              >
                {statuses.map((s) => (
                  <option key={s} value={s}>{s}</option>
                ))}
              </select>
            </div>
          </div>

          {/* Modal Actions */}
          <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '12px', marginTop: '12px' }}>
            <button type="button" className="btn btn-secondary" onClick={onClose}>
              Cancel
            </button>
            <button type="submit" className="btn btn-primary">
              {employee ? 'Save Changes' : 'Create Employee'}
            </button>
          </div>

        </form>

      </div>
    </div>
  );
}
