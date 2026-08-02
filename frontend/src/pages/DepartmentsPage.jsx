import React, { useState } from 'react';
import { useAppContext } from '../context/AppContext';
import { useToast } from '../context/ToastContext';
import { api } from '../services/api';
import DepartmentList from '../components/DepartmentList';
import DepartmentModal from '../components/DepartmentModal';

export default function DepartmentsPage() {
  const { departments, loadLookups } = useAppContext();
  const { showToast } = useToast();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedDept, setSelectedDept] = useState(null);

  const handleOpenAdd = () => {
    setSelectedDept(null);
    setIsModalOpen(true);
  };

  const handleOpenEdit = (dept) => {
    setSelectedDept(dept);
    setIsModalOpen(true);
  };

  const handleSave = async (formData) => {
    try {
      if (selectedDept) {
        await api.updateDepartment(selectedDept.id, formData);
        showToast(`Department #${selectedDept.id} updated!`);
      } else {
        await api.createDepartment(formData);
        showToast('New Department created!');
      }
      setIsModalOpen(false);
      setSelectedDept(null);
      loadLookups();
    } catch (err) {
      showToast(err.message || 'Operation failed', 'error');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm(`Delete Department #${id}?`)) {
      try {
        await api.deleteDepartment(id);
        showToast(`Department #${id} deleted.`);
        loadLookups();
      } catch (err) {
        showToast(err.message || 'Failed to delete department', 'error');
      }
    }
  };

  return (
    <>
      <DepartmentList
        departments={departments}
        onAdd={handleOpenAdd}
        onEdit={handleOpenEdit}
        onDelete={handleDelete}
      />

      <DepartmentModal
        isOpen={isModalOpen}
        onClose={() => { setIsModalOpen(false); setSelectedDept(null); }}
        onSave={handleSave}
        department={selectedDept}
      />
    </>
  );
}
