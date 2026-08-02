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

export default function DesignationFormPage() {
  const { id } = useParams();
  const isEdit = Boolean(id);
  const navigate = useNavigate();
  const { loadLookups } = useAppContext();
  const { showToast } = useToast();

  const [formData, setFormData] = useState({ title: '', code: '' });
  const [loading, setLoading] = useState(isEdit);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (isEdit) {
      async function loadDesig() {
        try {
          const desig = await api.getDesignationById(id);
          setFormData({
            title: desig.title || '',
            code: desig.code || ''
          });
        } catch (err) {
          setError(err.message || 'Failed to load designation');
        } finally {
          setLoading(false);
        }
      }
      loadDesig();
    }
  }, [id, isEdit]);

  if (loading) return <Loader text="Loading designation form..." />;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    setError('');

    try {
      if (isEdit) {
        await api.updateDesignation(id, formData);
        showToast(`Designation #${id} updated!`);
      } else {
        await api.createDesignation(formData);
        showToast('New Designation created!');
      }
      loadLookups();
      navigate('/designations');
    } catch (err) {
      setError(err.message || 'Failed to save designation');
      showToast(err.message || 'Operation failed', 'error');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px', maxWidth: '540px', margin: '0 auto' }}>
      
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Button variant="secondary" onClick={() => navigate('/designations')}>
          <ArrowLeft size={16} /> Back to Designations
        </Button>
      </div>

      <Card>
        <h2 style={{ fontSize: '1.5rem', fontWeight: 800, marginBottom: '4px' }}>
          {isEdit ? `Edit Designation #${id}` : 'Add New Designation'}
        </h2>
        <p style={{ color: '#94a3b8', fontSize: '0.875rem', marginBottom: '24px' }}>
          {isEdit ? 'Update job title and designation code.' : 'Define a new job role in the organization.'}
        </p>

        {error && (
          <div style={{ padding: '12px 16px', background: 'rgba(239, 68, 68, 0.15)', border: '1px solid rgba(239, 68, 68, 0.3)', borderRadius: '10px', color: '#fca5a5', fontSize: '0.85rem', marginBottom: '20px' }}>
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          <InputField
            label="Designation Title"
            value={formData.title}
            onChange={(e) => setFormData({ ...formData, title: e.target.value })}
            placeholder="e.g. Senior Software Architect"
            required
          />

          <InputField
            label="Designation Code (Optional - Auto Generated if blank)"
            value={formData.code}
            onChange={(e) => setFormData({ ...formData, code: e.target.value.toUpperCase() })}
            placeholder="e.g. SSA"
          />

          <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '12px', marginTop: '12px' }}>
            <Button variant="secondary" onClick={() => navigate('/designations')}>
              Cancel
            </Button>
            <Button variant="primary" type="submit" loading={submitting}>
              <Save size={18} /> {isEdit ? 'Save Changes' : 'Create Designation'}
            </Button>
          </div>
        </form>
      </Card>

    </div>
  );
}
