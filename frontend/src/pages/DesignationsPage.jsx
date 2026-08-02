import React, { useState } from 'react';
import { useAppContext } from '../context/AppContext';
import { useToast } from '../context/ToastContext';
import { api } from '../services/api';
import DesignationList from '../components/DesignationList';
import DesignationModal from '../components/DesignationModal';

export default function DesignationsPage() {
  const { designations, loadLookups } = useAppContext();
  const { showToast } = useToast();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedDesig, setSelectedDesig] = useState(null);

  const handleOpenAdd = () => {
    setSelectedDesig(null);
    setIsModalOpen(true);
  };

  const handleOpenEdit = (desig) => {
    setSelectedDesig(desig);
    setIsModalOpen(true);
  };

  const handleSave = async (formData) => {
    try {
      if (selectedDesig) {
        await api.updateDesignation(selectedDesig.id, formData);
        showToast(`Designation #${selectedDesig.id} updated!`);
      } else {
        await api.createDesignation(formData);
        showToast('New Designation created!');
      }
      setIsModalOpen(false);
      setSelectedDesig(null);
      loadLookups();
    } catch (err) {
      showToast(err.message || 'Operation failed', 'error');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm(`Delete Designation #${id}?`)) {
      try {
        await api.deleteDesignation(id);
        showToast(`Designation #${id} deleted.`);
        loadLookups();
      } catch (err) {
        showToast(err.message || 'Failed to delete designation', 'error');
      }
    }
  };

  return (
    <>
      <DesignationList
        designations={designations}
        onAdd={handleOpenAdd}
        onEdit={handleOpenEdit}
        onDelete={handleDelete}
      />

      <DesignationModal
        isOpen={isModalOpen}
        onClose={() => { setIsModalOpen(false); setSelectedDesig(null); }}
        onSave={handleSave}
        designation={selectedDesig}
      />
    </>
  );
}
