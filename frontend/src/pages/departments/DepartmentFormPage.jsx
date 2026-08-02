import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { api } from '../../services/api';
import { useAppContext } from '../../context/AppContext';
import { useToast } from '../../context/ToastContext';
import { ArrowLeft, Save } from 'lucide-react';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import InputField from '../../components/common/InputField';
import Loader from '../../components/common/Loader';

export default function DepartmentFormPage() {
  const { id } = useParams();
  const isEdit = Boolean(id);
  const navigate = useNavigate();
  const { loadLookups } = useAppContext();
  const { showToast } = useToast();

  const [formData, setFormData] = useState({ name: '', code: '', description: '', location: 'HQ Building A' });
  const [loading, setLoading] = useState(isEdit);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (isEdit) {
      async function loadDept() {
        try {
          const dept = await api.getDepartmentById(id);
          setFormData({
            name: dept.name || '',
            code: dept.code || '',
            description: dept.description || '',
            location: dept.location || 'HQ Building A'
          });
        } catch (err) {
          setError(err.message || 'Failed to load department');
        } finally {
          setLoading(false);
        }
      }
      loadDept();
    }
  }, [id, isEdit]);

  if (loading) return <Loader text="Loading department form..." />;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    setError('');

    try {
      if (isEdit) {
        await api.updateDepartment(id, formData);
        showToast(`Department #${id} updated!`);
      } else {
        await api.createDepartment(formData);
        showToast('New Department created!');
      }
      loadLookups();
      navigate('/departments');
    } catch (err) {
      setError(err.message || 'Failed to save department');
      showToast(err.message || 'Operation failed', 'error');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px', maxWidth: '640px', margin: '0 auto' }}>
      
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Button variant="secondary" onClick={() => navigate('/departments')}>
          <ArrowLeft size={16} /> Back to Departments
        </Button>
      </div>

      <Card>
        <h2 style={{ fontSize: '1.5rem', fontWeight: 800, marginBottom: '4px' }}>
          {isEdit ? `Edit Department #${id}` : 'Create New Department'}
        </h2>
        <p style={{ color: '#94a3b8', fontSize: '0.875rem', marginBottom: '24px' }}>
          {isEdit ? 'Modify department title, code, or location.' : 'Add a new business unit to the organization.'}
        </p>

        {error && (
          <div style={{ padding: '12px 16px', background: 'rgba(239, 68, 68, 0.15)', border: '1px solid rgba(239, 68, 68, 0.3)', borderRadius: '10px', color: '#fca5a5', fontSize: '0.85rem', marginBottom: '20px' }}>
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          <InputField
            label="Department Name"
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            placeholder="e.g. Engineering"
            required
          />

          <InputField
            label="Department Code (Optional - Auto Generated if blank)"
            value={formData.code}
            onChange={(e) => setFormData({ ...formData, code: e.target.value.toUpperCase() })}
            placeholder="e.g. ENG"
          />

          <InputField
            label="Location / Office HQ"
            value={formData.location}
            onChange={(e) => setFormData({ ...formData, location: e.target.value })}
            placeholder="e.g. HQ Building A"
          />

          <div style={{ display: 'flex', flexDirection: 'column', gap: '6px' }}>
            <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#94a3b8' }}>Description</label>
            <textarea
              className="input-field"
              rows={3}
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              placeholder="Brief description of department scope and operations..."
              style={{ resize: 'vertical' }}
            />
          </div>

          <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '12px', marginTop: '12px' }}>
            <Button variant="secondary" onClick={() => navigate('/departments')}>
              Cancel
            </Button>
            <Button variant="primary" type="submit" loading={submitting}>
              <Save size={18} /> {isEdit ? 'Save Changes' : 'Create Department'}
            </Button>
          </div>
        </form>
      </Card>

    </div>
  );
}
