import React, { createContext, useContext, useState, useEffect } from 'react';
import { api } from '../services/api';

const AppContext = createContext(null);

export function AppProvider({ children }) {
  const [departments, setDepartments] = useState([]);
  const [designations, setDesignations] = useState([]);
  const [cities, setCities] = useState([]);
  const [statuses, setStatuses] = useState(['ACTIVE', 'INACTIVE', 'ON_LEAVE']);
  const [backendConnected, setBackendConnected] = useState(true);

  const loadLookups = async () => {
    try {
      const [depts, desigs, cits, stats] = await Promise.all([
        api.getDepartments().catch(() => []),
        api.getDesignations().catch(() => []),
        api.getCities().catch(() => []),
        api.getStatuses().catch(() => ['ACTIVE', 'INACTIVE', 'ON_LEAVE']),
      ]);
      if (Array.isArray(depts)) setDepartments(depts);
      if (Array.isArray(desigs)) setDesignations(desigs);
      if (Array.isArray(cits)) setCities(cits);
      if (Array.isArray(stats) && stats.length > 0) setStatuses(stats);
      setBackendConnected(true);
    } catch (err) {
      console.error('Failed to load global lookups:', err);
      setBackendConnected(false);
    }
  };

  useEffect(() => {
    loadLookups();
  }, []);

  return (
    <AppContext.Provider
      value={{
        departments,
        designations,
        cities,
        statuses,
        backendConnected,
        loadLookups,
        setBackendConnected
      }}
    >
      {children}
    </AppContext.Provider>
  );
}

export function useAppContext() {
  const context = useContext(AppContext);
  if (!context) {
    throw new Error('useAppContext must be used within an AppProvider');
  }
  return context;
}
