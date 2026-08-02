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

export default function CityFormPage() {
  const { id } = useParams();
  const isEdit = Boolean(id);
  const navigate = useNavigate();
  const { loadLookups } = useAppContext();
  const { showToast } = useToast();

  const [formData, setFormData] = useState({ name: '', state: 'N/A', country: 'USA' });
  const [loading, setLoading] = useState(isEdit);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (isEdit) {
      async function loadCity() {
        try {
          const city = await api.getCityById(id);
          setFormData({
            name: city.name || '',
            state: city.state || 'N/A',
            country: city.country || 'USA'
          });
        } catch (err) {
          setError(err.message || 'Failed to load city details');
        } finally {
          setLoading(false);
        }
      }
      loadCity();
    }
  }, [id, isEdit]);

  if (loading) return <Loader text="Loading city form..." />;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    setError('');

    try {
      if (isEdit) {
        await api.updateCity(id, formData);
        showToast(`City #${id} updated!`);
      } else {
        await api.createCity(formData);
        showToast('New City location added!');
      }
      loadLookups();
      navigate('/cities');
    } catch (err) {
      setError(err.message || 'Failed to save city');
      showToast(err.message || 'Operation failed', 'error');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px', maxWidth: '540px', margin: '0 auto' }}>
      
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Button variant="secondary" onClick={() => navigate('/cities')}>
          <ArrowLeft size={16} /> Back to Cities
        </Button>
      </div>

      <Card>
        <h2 style={{ fontSize: '1.5rem', fontWeight: 800, marginBottom: '4px' }}>
          {isEdit ? `Edit City Location #${id}` : 'Add New Office City'}
        </h2>
        <p style={{ color: '#94a3b8', fontSize: '0.875rem', marginBottom: '24px' }}>
          {isEdit ? 'Update city name, state, or country.' : 'Register a new geographic office location.'}
        </p>

        {error && (
          <div style={{ padding: '12px 16px', background: 'rgba(239, 68, 68, 0.15)', border: '1px solid rgba(239, 68, 68, 0.3)', borderRadius: '10px', color: '#fca5a5', fontSize: '0.85rem', marginBottom: '20px' }}>
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          <InputField
            label="City Name"
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            placeholder="e.g. Seattle"
            required
          />

          <InputField
            label="State / Region"
            value={formData.state}
            onChange={(e) => setFormData({ ...formData, state: e.target.value })}
            placeholder="e.g. WA"
          />

          <InputField
            label="Country"
            value={formData.country}
            onChange={(e) => setFormData({ ...formData, country: e.target.value })}
            placeholder="e.g. USA"
          />

          <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '12px', marginTop: '12px' }}>
            <Button variant="secondary" onClick={() => navigate('/cities')}>
              Cancel
            </Button>
            <Button variant="primary" type="submit" loading={submitting}>
              <Save size={18} /> {isEdit ? 'Save Changes' : 'Create City'}
            </Button>
          </div>
        </form>
      </Card>

    </div>
  );
}
