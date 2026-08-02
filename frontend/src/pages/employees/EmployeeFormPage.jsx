import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { api } from '../../services/api';
import { useAppContext } from '../../context/AppContext';
import { useToast } from '../../context/ToastContext';
import { ArrowLeft, Save } from 'lucide-react';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import InputField from '../../components/common/InputField';
import SelectField from '../../components/common/SelectField';
import Loader from '../../components/common/Loader';

export default function EmployeeFormPage() {
  const { id } = useParams();
  const isEdit = Boolean(id);
  const navigate = useNavigate();
  const { departments, designations, cities, statuses } = useAppContext();
  const { showToast } = useToast();

  const [formData, setFormData] = useState({
    name: '',
    email: '',
    departmentName: '',
    designationTitle: '',
    salary: 75000,
    joiningDate: new Date().toISOString().split('T')[0],
    cityName: '',
    status: 'ACTIVE'
  });

  const [loading, setLoading] = useState(isEdit);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (isEdit) {
      async function loadData() {
        try {
          const emp = await api.getEmployeeById(id);
          setFormData({
            name: emp.name || '',
            email: emp.email || '',
            departmentName: emp.department?.name || '',
            designationTitle: emp.designation?.title || '',
            salary: emp.salary || 75000,
            joiningDate: emp.joiningDate || new Date().toISOString().split('T')[0],
            cityName: emp.city?.name || '',
            status: emp.status || 'ACTIVE'
          });
        } catch (err) {
          setError(err.message || 'Failed to load employee');
        } finally {
          setLoading(false);
        }
      }
      loadData();
    } else {
      if (departments.length > 0) formData.departmentName = departments[0].name;
      if (designations.length > 0) formData.designationTitle = designations[0].title;
      if (cities.length > 0) formData.cityName = cities[0].name;
    }
  }, [id, isEdit, departments, designations, cities]);

  if (loading) return <Loader text="Loading employee form data..." />;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    setError('');

    try {
      if (isEdit) {
        await api.updateEmployee(id, formData);
        showToast(`Employee #${id} updated successfully!`);
      } else {
        await api.createEmployee(formData);
        showToast('New employee created successfully!');
      }
      navigate('/employees');
    } catch (err) {
      setError(err.message || 'Failed to save employee record');
      showToast(err.message || 'Failed to save employee', 'error');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px', maxWidth: '720px', margin: '0 auto' }}>
      
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Button variant="secondary" onClick={() => navigate('/employees')}>
          <ArrowLeft size={16} /> Back to Directory
        </Button>
      </div>

      <Card>
        <h2 style={{ fontSize: '1.5rem', fontWeight: 800, marginBottom: '4px' }}>
          {isEdit ? `Edit Employee Record #${id}` : 'Create New Employee'}
        </h2>
        <p style={{ color: '#94a3b8', fontSize: '0.875rem', marginBottom: '24px' }}>
          {isEdit ? 'Update employee personal details, department assignment, and compensation.' : 'Enter employee details to onboard into the system.'}
        </p>

        {error && (
          <div style={{ padding: '12px 16px', background: 'rgba(239, 68, 68, 0.15)', border: '1px solid rgba(239, 68, 68, 0.3)', borderRadius: '10px', color: '#fca5a5', fontSize: '0.85rem', marginBottom: '20px' }}>
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
            <InputField
              label="Full Name"
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              placeholder="e.g. Siva Naroji"
              required
            />

            <InputField
              label="Email Address"
              type="email"
              value={formData.email}
              onChange={(e) => setFormData({ ...formData, email: e.target.value })}
              placeholder="siva@company.com"
              required
            />
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
            <SelectField
              label="Department"
              value={formData.departmentName}
              onChange={(e) => setFormData({ ...formData, departmentName: e.target.value })}
              options={departments.map((d) => ({ value: d.name, label: `${d.name} (${d.code})` }))}
              required
            />

            <SelectField
              label="Designation / Job Title"
              value={formData.designationTitle}
              onChange={(e) => setFormData({ ...formData, designationTitle: e.target.value })}
              options={designations.map((d) => ({ value: d.title, label: d.title }))}
              required
            />
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
            <InputField
              label="Annual Salary ($)"
              type="number"
              value={formData.salary}
              onChange={(e) => setFormData({ ...formData, salary: parseFloat(e.target.value) || 0 })}
              min="1000"
              step="500"
              required
            />

            <InputField
              label="Joining Date"
              type="date"
              value={formData.joiningDate}
              onChange={(e) => setFormData({ ...formData, joiningDate: e.target.value })}
            />
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
            <SelectField
              label="Office City"
              value={formData.cityName}
              onChange={(e) => setFormData({ ...formData, cityName: e.target.value })}
              options={cities.map((c) => ({ value: c.name, label: c.name }))}
            />

            <SelectField
              label="Employee Status"
              value={formData.status}
              onChange={(e) => setFormData({ ...formData, status: e.target.value })}
              options={statuses.map((s) => ({ value: s, label: s }))}
              required
            />
          </div>

          <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '12px', marginTop: '12px' }}>
            <Button variant="secondary" onClick={() => navigate('/employees')}>
              Cancel
            </Button>
            <Button variant="primary" type="submit" loading={submitting}>
              <Save size={18} /> {isEdit ? 'Save Changes' : 'Create Employee'}
            </Button>
          </div>

        </form>
      </Card>

    </div>
  );
}
