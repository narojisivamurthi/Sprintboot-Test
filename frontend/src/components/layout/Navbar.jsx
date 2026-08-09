import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { Users, Building2, Briefcase, MapPin, LayoutDashboard, LogOut, LogIn, UserPlus, UserCheck } from 'lucide-react';
import { useAppContext } from '../../context/AppContext';
import { useAuth } from '../../context/AuthContext';
import Badge from '../common/Badge';

export default function Navbar() {
  const { backendConnected } = useAppContext();
  const { user, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();

  const tabs = [
    { path: '/', label: 'Dashboard', icon: LayoutDashboard },
    { path: '/employees', label: 'Employees', icon: Users },
    { path: '/departments', label: 'Departments', icon: Building2 },
    { path: '/designations', label: 'Designations', icon: Briefcase },
    { path: '/cities', label: 'Cities', icon: MapPin },
    { path: '/users', label: 'Users', icon: UserCheck },
  ];

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="glass-panel" style={{ borderRadius: 0, borderTop: 0, borderLeft: 0, borderRight: 0, position: 'sticky', top: 0, zIndex: 100 }}>
      <div style={{ maxWidth: '1400px', margin: '0 auto', padding: '14px 24px', display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: '16px' }}>
        
        {/* Brand */}
        <NavLink to="/" style={{ textDecoration: 'none', display: 'flex', alignItems: 'center', gap: '12px' }}>
          <div style={{ width: '42px', height: '42px', borderRadius: '12px', background: 'linear-gradient(135deg, #6366f1, #4f46e5)', display: 'flex', alignItems: 'center', justifyContent: 'center', boxShadow: '0 0 20px rgba(99, 102, 241, 0.4)' }}>
            <Users size={22} color="#ffffff" />
          </div>
          <div>
            <h1 style={{ fontSize: '1.25rem', fontWeight: 800, letterSpacing: '-0.02em', background: 'linear-gradient(to right, #ffffff, #94a3b8)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>
              Employee OS
            </h1>
            <span style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: 500 }}>JWT Secured • Spring Boot & React</span>
          </div>
        </NavLink>

        {/* Navigation Links */}
        <nav style={{ display: 'flex', alignItems: 'center', gap: '6px', background: 'rgba(15, 23, 42, 0.6)', padding: '6px', borderRadius: '14px', border: '1px solid rgba(255, 255, 255, 0.05)' }}>
          {tabs.map((tab) => {
            const Icon = tab.icon;
            return (
              <NavLink
                key={tab.path}
                to={tab.path}
                className={({ isActive }) => `btn ${isActive ? 'btn-primary' : 'btn-secondary'}`}
                style={({ isActive }) => ({
                  padding: '8px 16px',
                  borderRadius: '10px',
                  border: 'none',
                  fontSize: '0.85rem',
                  textDecoration: 'none',
                  display: 'flex',
                  alignItems: 'center',
                  gap: '8px',
                  background: isActive ? 'linear-gradient(135deg, #6366f1, #4f46e5)' : 'transparent',
                  color: isActive ? '#ffffff' : '#94a3b8',
                })}
              >
                <Icon size={16} />
                {tab.label}
              </NavLink>
            );
          })}
        </nav>

        {/* User Auth Section & Backend Status */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          
          {/* Backend Status Indicator */}
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px', background: 'rgba(15, 23, 42, 0.6)', padding: '8px 12px', borderRadius: '20px', border: '1px solid rgba(255, 255, 255, 0.05)' }}>
            <span style={{
              width: '8px',
              height: '8px',
              borderRadius: '50%',
              backgroundColor: backendConnected ? '#10b981' : '#ef4444',
              boxShadow: backendConnected ? '0 0 10px #10b981' : '0 0 10px #ef4444'
            }} />
            <span style={{ fontSize: '0.75rem', color: '#94a3b8', fontWeight: 600 }}>
              {backendConnected ? 'API Online' : 'Offline'}
            </span>
          </div>

          {/* User Status / Login Buttons */}
          {isAuthenticated ? (
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px', background: 'rgba(18, 24, 38, 0.8)', padding: '6px 12px', borderRadius: '14px', border: '1px solid rgba(255, 255, 255, 0.1)' }}>
              <div style={{
                width: '32px',
                height: '32px',
                borderRadius: '50%',
                background: user?.role === 'ROLE_ADMIN' ? 'linear-gradient(135deg, #6366f1, #4f46e5)' : 'linear-gradient(135deg, #10b981, #059669)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                fontWeight: 700,
                fontSize: '0.85rem',
                color: '#ffffff',
              }}>
                {user?.username ? user.username.charAt(0).toUpperCase() : 'U'}
              </div>
              <div style={{ display: 'flex', flexDirection: 'column' }}>
                <span style={{ fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-main)' }}>
                  {user?.username}
                </span>
                <span style={{ fontSize: '0.7rem', color: user?.role === 'ROLE_ADMIN' ? '#a5b4fc' : '#6ee7b7' }}>
                  {user?.role === 'ROLE_ADMIN' ? 'Admin' : 'User'}
                </span>
              </div>
              <button
                onClick={handleLogout}
                title="Sign Out"
                style={{
                  background: 'rgba(239, 68, 68, 0.1)',
                  border: '1px solid rgba(239, 68, 68, 0.2)',
                  color: '#f87171',
                  borderRadius: '8px',
                  padding: '6px 10px',
                  cursor: 'pointer',
                  display: 'flex',
                  alignItems: 'center',
                  gap: '6px',
                  fontSize: '0.8rem',
                  fontWeight: 600,
                  marginLeft: '6px',
                }}
              >
                <LogOut size={14} />
                Logout
              </button>
            </div>
          ) : (
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <NavLink
                to="/login"
                style={{
                  textDecoration: 'none',
                  color: 'var(--text-main)',
                  padding: '8px 14px',
                  borderRadius: '10px',
                  fontSize: '0.85rem',
                  fontWeight: 600,
                  display: 'flex',
                  alignItems: 'center',
                  gap: '6px',
                  backgroundColor: 'rgba(255, 255, 255, 0.05)',
                  border: '1px solid var(--border-color)',
                }}
              >
                <LogIn size={16} />
                Sign In
              </NavLink>
              <NavLink
                to="/register"
                style={{
                  textDecoration: 'none',
                  color: '#ffffff',
                  padding: '8px 14px',
                  borderRadius: '10px',
                  fontSize: '0.85rem',
                  fontWeight: 600,
                  display: 'flex',
                  alignItems: 'center',
                  gap: '6px',
                  background: 'linear-gradient(135deg, #6366f1, #4f46e5)',
                }}
              >
                <UserPlus size={16} />
                Register
              </NavLink>
            </div>
          )}

        </div>

      </div>
    </header>
  );
}
