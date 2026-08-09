import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { api } from '../services/api';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const savedUser = localStorage.getItem('user');
    return savedUser ? JSON.parse(savedUser) : null;
  });
  const [token, setToken] = useState(() => localStorage.getItem('token') || null);
  const [loading, setLoading] = useState(true);

  const logout = useCallback(() => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setToken(null);
    setUser(null);
  }, []);

  // Fetch current user details on initial load if token exists
  useEffect(() => {
    let isMounted = true;
    async function loadUser() {
      if (token) {
        try {
          const userData = await api.getCurrentUser();
          if (isMounted) {
            setUser(userData);
            localStorage.setItem('user', JSON.stringify(userData));
          }
        } catch (err) {
          console.error('Failed to authenticate session:', err);
          if (isMounted) {
            logout();
          }
        }
      } else {
        if (isMounted) {
          setUser(null);
        }
      }
      if (isMounted) {
        setLoading(false);
      }
    }

    loadUser();

    return () => {
      isMounted = false;
    };
  }, [token, logout]);

  // Listen for unauthorized 401 events from api fetch client
  useEffect(() => {
    const handleUnauthorized = () => {
      logout();
    };

    window.addEventListener('auth:unauthorized', handleUnauthorized);
    return () => {
      window.removeEventListener('auth:unauthorized', handleUnauthorized);
    };
  }, [logout]);

  const login = async (usernameOrEmail, password) => {
    const response = await api.login({ usernameOrEmail, password });
    const { accessToken, username, email, role } = response;
    
    const userPayload = { username, email, role };
    localStorage.setItem('token', accessToken);
    localStorage.setItem('user', JSON.stringify(userPayload));

    setToken(accessToken);
    setUser(userPayload);
    return userPayload;
  };

  const register = async (userData) => {
    return await api.register(userData);
  };

  const value = {
    user,
    token,
    isAuthenticated: !!token && !!user,
    loading,
    login,
    register,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
