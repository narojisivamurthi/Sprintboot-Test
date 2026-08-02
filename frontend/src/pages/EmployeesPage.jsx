import React, { useState } from 'react';
import { useEmployees } from '../hooks/useEmployees';
import { useAppContext } from '../context/AppContext';
import EmployeeList from '../components/EmployeeList';
import EmployeeModal from '../components/EmployeeModal';

export default function EmployeesPage() {
  const { employees, pagination, loading, loadEmployees, saveEmployee, deleteEmployee } = useEmployees();
  const { departments, designations, cities, statuses } = useAppContext();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedEmp, setSelectedEmp] = useState(null);

  const handleOpenAdd = () => {
    setSelectedEmp(null);
    setIsModalOpen(true);
  };

  const handleOpenEdit = (emp) => {
    setSelectedEmp(emp);
    setIsModalOpen(true);
  };

  const handleSave = async (formData) => {
    const success = await saveEmployee(selectedEmp, formData);
    if (success) {
      setIsModalOpen(false);
      setSelectedEmp(null);
    }
  };

  return (
    <>
      <EmployeeList
        employees={employees}
        pagination={pagination}
        departments={departments}
        cities={cities}
        statuses={statuses}
        onPageChange={(page) => loadEmployees(page, pagination.size, pagination.sortBy, pagination.sortDir)}
        onPageSizeChange={(size) => loadEmployees(0, size, pagination.sortBy, pagination.sortDir)}
        onSortChange={(sortBy, sortDir) => loadEmployees(0, pagination.size, sortBy, sortDir)}
        onOpenAddModal={handleOpenAdd}
        onOpenEditModal={handleOpenEdit}
        onDeleteEmployee={deleteEmployee}
        loading={loading}
      />

      <EmployeeModal
        isOpen={isModalOpen}
        onClose={() => { setIsModalOpen(false); setSelectedEmp(null); }}
        onSave={handleSave}
        employee={selectedEmp}
        departments={departments}
        designations={designations}
        cities={cities}
        statuses={statuses}
      />
    </>
  );
}
