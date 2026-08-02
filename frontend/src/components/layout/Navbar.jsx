import React from 'react';
import { NavLink } from 'react-router-dom';
import { Users, Building2, Briefcase, MapPin, LayoutDashboard } from 'lucide-react';
import { useAppContext } from '../../context/AppContext';

export default function Navbar() {
  const { backendConnected } = useAppContext();

  const tabs = [
    { path: '/', label: 'Dashboard', icon: LayoutDashboard },
    { path: '/employees', label: 'Employees', icon: Users },
    { path: '/departments', label: 'Departments', icon: Building2 },
    { path: '/designations', label: 'Designations', icon: Briefcase },
    { path: '/cities', label: 'Cities', icon: MapPin },
  ];

  return (
    <header className="glass-panel" style={{ borderRadius: 0, borderTop: 0, borderLeft: 0, borderRight: 0, position: 'sticky', top: 0, zIndex: 100 }}>
      <div style={{ maxWidth: '1400px', margin: '0 auto', padding: '16px 24px', display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: '16px' }}>
        
        {/* Brand */}
        <NavLink to="/" style={{ textDecoration: 'none', display: 'flex', alignItems: 'center', gap: '12px' }}>
          <div style={{ width: '42px', height: '42px', borderRadius: '12px', background: 'linear-gradient(135deg, #6366f1, #4f46e5)', display: 'flex', alignItems: 'center', justifyContent: 'center', boxShadow: '0 0 20px rgba(99, 102, 241, 0.4)' }}>
            <Users size={22} color="#ffffff" />
          </div>
          <div>
            <h1 style={{ fontSize: '1.25rem', fontWeight: 800, letterSpacing: '-0.02em', background: 'linear-gradient(to right, #ffffff, #94a3b8)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>
              Employee OS
            </h1>
            <span style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: 500 }}>Spring Boot 3.4 & React 18</span>
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

        {/* Backend Status Indicator */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', background: 'rgba(15, 23, 42, 0.6)', padding: '8px 14px', borderRadius: '20px', border: '1px solid rgba(255, 255, 255, 0.05)' }}>
          <span style={{
            width: '8px',
            height: '8px',
            borderRadius: '50%',
            backgroundColor: backendConnected ? '#10b981' : '#ef4444',
            boxShadow: backendConnected ? '0 0 10px #10b981' : '0 0 10px #ef4444'
          }} />
          <span style={{ fontSize: '0.75rem', color: '#94a3b8', fontWeight: 600 }}>
            {backendConnected ? 'Backend Connected' : 'Backend Reconnecting...'}
          </span>
        </div>

      </div>
    </header>
  );
}
