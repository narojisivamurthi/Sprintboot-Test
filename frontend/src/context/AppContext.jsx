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
      const deptsList = Array.isArray(depts) ? depts : (depts && Array.isArray(depts.data) ? depts.data : []);
      const desigsList = Array.isArray(desigs) ? desigs : (desigs && Array.isArray(desigs.data) ? desigs.data : []);
      const citsList = Array.isArray(cits) ? cits : (cits && Array.isArray(cits.data) ? cits.data : []);
      const statsList = Array.isArray(stats) ? stats : (stats && Array.isArray(stats.data) ? stats.data : []);

      setDepartments(deptsList);
      setDesignations(desigsList);
      setCities(citsList);
      if (statsList.length > 0) setStatuses(statsList);
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
