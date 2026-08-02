const BASE_URL = '/api'; // Proxied to http://localhost:8080 in dev

async function request(endpoint, options = {}) {
  const url = `${BASE_URL}${endpoint}`;
  const config = {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  };

  try {
    const response = await fetch(url, config);
    if (!response.ok) {
      const errorText = await response.text();
      let errorMessage = `HTTP Error ${response.status}`;
      try {
        const errorJson = JSON.parse(errorText);
        errorMessage = errorJson.message || errorJson.error || errorMessage;
      } catch {
        errorMessage = errorText || errorMessage;
      }
      throw new Error(errorMessage);
    }
    
    // For DELETE or empty responses
    if (response.status === 204 || response.headers.get('content-length') === '0') {
      return null;
    }
    
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
      return await response.json();
    }
    return await response.text();
  } catch (error) {
    console.error(`API Error on [${options.method || 'GET'}] ${endpoint}:`, error);
    throw error;
  }
}

export const api = {
  // Employees
  getEmployees: (page = 0, size = 10, sortBy = 'id', sortDir = 'asc') =>
    request(`/employees?page=${page}&size=${size}&sortBy=${sortBy}&sortDir=${sortDir}`),
  getAllEmployees: () => request('/employees/all'),
  getEmployeeById: (id) => request(`/employees/${id}`),
  createEmployee: (data) => request('/employees', { method: 'POST', body: JSON.stringify(data) }),
  updateEmployee: (id, data) => request(`/employees/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  deleteEmployee: (id) => request(`/employees/${id}`, { method: 'DELETE' }),

  // Departments
  getDepartments: () => request('/departments'),
  getDepartmentById: (id) => request(`/departments/${id}`),
  getDepartmentWithEmployees: (id) => request(`/departments/${id}/employees`),
  createDepartment: (data) => request('/departments', { method: 'POST', body: JSON.stringify(data) }),
  updateDepartment: (id, data) => request(`/departments/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  deleteDepartment: (id) => request(`/departments/${id}`, { method: 'DELETE' }),

  // Designations
  getDesignations: () => request('/designations'),
  getDesignationById: (id) => request(`/designations/${id}`),
  createDesignation: (data) => request('/designations', { method: 'POST', body: JSON.stringify(data) }),
  updateDesignation: (id, data) => request(`/designations/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  deleteDesignation: (id) => request(`/designations/${id}`, { method: 'DELETE' }),

  // Cities
  getCities: () => request('/cities'),
  getCityById: (id) => request(`/cities/${id}`),
  createCity: (data) => request('/cities', { method: 'POST', body: JSON.stringify(data) }),
  updateCity: (id, data) => request(`/cities/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  deleteCity: (id) => request(`/cities/${id}`, { method: 'DELETE' }),

  // Statuses
  getStatuses: () => request('/statuses'),
};
