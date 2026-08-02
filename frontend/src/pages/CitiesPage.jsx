import React, { useState } from 'react';
import { useAppContext } from '../context/AppContext';
import { useToast } from '../context/ToastContext';
import { api } from '../services/api';
import CityList from '../components/CityList';
import CityModal from '../components/CityModal';

export default function CitiesPage() {
  const { cities, loadLookups } = useAppContext();
  const { showToast } = useToast();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedCity, setSelectedCity] = useState(null);

  const handleOpenAdd = () => {
    setSelectedCity(null);
    setIsModalOpen(true);
  };

  const handleOpenEdit = (city) => {
    setSelectedCity(city);
    setIsModalOpen(true);
  };

  const handleSave = async (formData) => {
    try {
      if (selectedCity) {
        await api.updateCity(selectedCity.id, formData);
        showToast(`City #${selectedCity.id} updated!`);
      } else {
        await api.createCity(formData);
        showToast('New City location added!');
      }
      setIsModalOpen(false);
      setSelectedCity(null);
      loadLookups();
    } catch (err) {
      showToast(err.message || 'Operation failed', 'error');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm(`Delete City #${id}?`)) {
      try {
        await api.deleteCity(id);
        showToast(`City #${id} deleted.`);
        loadLookups();
      } catch (err) {
        showToast(err.message || 'Failed to delete city', 'error');
      }
    }
  };

  return (
    <>
      <CityList
        cities={cities}
        onAdd={handleOpenAdd}
        onEdit={handleOpenEdit}
        onDelete={handleDelete}
      />

      <CityModal
        isOpen={isModalOpen}
        onClose={() => { setIsModalOpen(false); setSelectedCity(null); }}
        onSave={handleSave}
        city={selectedCity}
      />
    </>
  );
}
