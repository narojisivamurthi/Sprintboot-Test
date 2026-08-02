import { useState, useEffect, useCallback } from 'react';
import { api } from '../services/api';
import { useToast } from '../context/ToastContext';
import { useAppContext } from '../context/AppContext';

export function useEmployees() {
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(false);
  const [pagination, setPagination] = useState({
    page: 0,
    size: 10,
    totalPages: 1,
    totalElements: 0,
    sortBy: 'id',
    sortDir: 'asc'
  });

  const { showToast } = useToast();
  const { loadLookups, setBackendConnected } = useAppContext();

  const loadEmployees = useCallback(async (
    page = pagination.page,
    size = pagination.size,
    sortBy = pagination.sortBy,
    sortDir = pagination.sortDir
  ) => {
    setLoading(true);
    try {
      const data = await api.getEmployees(page, size, sortBy, sortDir);
      if (data && data.content) {
        setEmployees(data.content);

        const p = data.page || {};
        const pageNum = p.number ?? data.number ?? 0;
        const pSize = p.size ?? data.size ?? size;
        const totalP = p.totalPages ?? data.totalPages ?? 1;
        const totalE = p.totalElements ?? data.totalElements ?? data.content.length;

        setPagination({
          page: pageNum,
          size: pSize,
          totalPages: totalP,
          totalElements: totalE,
          sortBy: sortBy,
          sortDir: sortDir
        });
      } else if (Array.isArray(data)) {
        setEmployees(data);
      }
      setBackendConnected(true);
    } catch (err) {
      console.error('Failed to load employees:', err);
      setBackendConnected(false);
    } finally {
      setLoading(false);
    }
  }, [pagination.page, pagination.size, pagination.sortBy, pagination.sortDir, setBackendConnected]);

  useEffect(() => {
    loadEmployees(0, 10, 'id', 'asc');
  }, []);

  const saveEmployee = async (selectedEmp, formData) => {
    try {
      if (selectedEmp) {
        await api.updateEmployee(selectedEmp.id, formData);
        showToast(`Employee #${selectedEmp.id} updated successfully!`);
      } else {
        await api.createEmployee(formData);
        showToast('New Employee created successfully!');
      }
      loadEmployees(pagination.page, pagination.size, pagination.sortBy, pagination.sortDir);
      loadLookups();
      return true;
    } catch (err) {
      showToast(err.message || 'Operation failed', 'error');
      return false;
    }
  };

  const deleteEmployee = async (id) => {
    if (window.confirm(`Are you sure you want to delete Employee #${id}?`)) {
      try {
        await api.deleteEmployee(id);
        showToast(`Employee #${id} deleted successfully.`);
        loadEmployees(pagination.page, pagination.size, pagination.sortBy, pagination.sortDir);
        loadLookups();
        return true;
      } catch (err) {
        showToast(err.message || 'Failed to delete employee', 'error');
        return false;
      }
    }
    return false;
  };

  return {
    employees,
    pagination,
    loading,
    loadEmployees,
    saveEmployee,
    deleteEmployee
  };
}
