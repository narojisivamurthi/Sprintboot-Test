import React from 'react';
import { Users, Building2, Briefcase, MapPin, TrendingUp, CheckCircle, UserPlus, ShieldCheck } from 'lucide-react';

export default function Dashboard({ stats, setActiveTab, onOpenAddEmployee }) {
  const { totalEmployees = 0, activeEmployees = 0, totalDepartments = 0, totalDesignations = 0, totalCities = 0 } = stats;

  const activeRatio = totalEmployees > 0 ? Math.round((activeEmployees / totalEmployees) * 100) : 0;

  const kpis = [
    { title: 'Total Employees', count: totalEmployees, icon: Users, color: '#6366f1', subtitle: `${activeEmployees} Active Employees` },
    { title: 'Active Workforce', count: `${activeRatio}%`, icon: CheckCircle, color: '#10b981', subtitle: 'Operational readiness' },
    { title: 'Departments', count: totalDepartments, icon: Building2, color: '#3b82f6', subtitle: 'Active business units' },
    { title: 'Designations', count: totalDesignations, icon: Briefcase, color: '#8b5cf6', subtitle: 'Job roles & salary bands' },
    { title: 'Cities / Offices', count: totalCities, icon: MapPin, color: '#ec4899', subtitle: 'Global location coverage' },
  ];

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      
      {/* Welcome Banner */}
      <div className="glass-panel" style={{ padding: '32px', background: 'linear-gradient(135deg, rgba(99, 102, 241, 0.15) 0%, rgba(18, 24, 38, 0.8) 100%)', border: '1px solid rgba(99, 102, 241, 0.3)', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '20px' }}>
        <div>
          <div style={{ display: 'inline-flex', alignItems: 'center', gap: '6px', background: 'rgba(99, 102, 241, 0.2)', color: '#a5b4fc', padding: '4px 12px', borderRadius: '20px', fontSize: '0.75rem', fontWeight: 700, marginBottom: '12px' }}>
            <ShieldCheck size={14} /> Full-Stack Spring Boot 3.4 & React Enterprise
          </div>
          <h2 style={{ fontSize: '1.75rem', fontWeight: 800, marginBottom: '8px' }}>
            Enterprise HR & Workforce Portal
          </h2>
          <p style={{ color: '#94a3b8', fontSize: '0.95rem', maxWidth: '600px' }}>
            Real-time management for employee directory, departmental structures, designation bands, and global office locations.
          </p>
        </div>

        <div style={{ display: 'flex', gap: '12px' }}>
          <button className="btn btn-primary" onClick={onOpenAddEmployee} style={{ padding: '12px 20px', fontSize: '0.9rem' }}>
            <UserPlus size={18} /> Quick Add Employee
          </button>
          <button className="btn btn-secondary" onClick={() => setActiveTab('employees')} style={{ padding: '12px 20px', fontSize: '0.9rem' }}>
            View Directory
          </button>
        </div>
      </div>

      {/* KPI Cards Grid */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '20px' }}>
        {kpis.map((kpi, idx) => {
          const Icon = kpi.icon;
          return (
            <div key={idx} className="glass-panel glass-panel-interactive" style={{ padding: '24px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
                <span style={{ fontSize: '0.85rem', color: '#94a3b8', fontWeight: 600 }}>{kpi.title}</span>
                <div style={{ width: '40px', height: '40px', borderRadius: '10px', background: `${kpi.color}20`, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <Icon size={20} color={kpi.color} />
                </div>
              </div>
              <div style={{ fontSize: '2rem', fontWeight: 800, color: '#f8fafc', marginBottom: '4px' }}>
                {kpi.count}
              </div>
              <div style={{ fontSize: '0.75rem', color: '#64748b' }}>
                {kpi.subtitle}
              </div>
            </div>
          );
        })}
      </div>

      {/* Quick Navigation Cards */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '20px' }}>
        <div className="glass-panel glass-panel-interactive" style={{ padding: '24px', cursor: 'pointer' }} onClick={() => setActiveTab('departments')}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '14px', marginBottom: '12px' }}>
            <div style={{ padding: '10px', borderRadius: '12px', background: 'rgba(59, 130, 246, 0.15)' }}>
              <Building2 size={24} color="#3b82f6" />
            </div>
            <div>
              <h3 style={{ fontSize: '1.1rem', fontWeight: 700 }}>Department Directory</h3>
              <span style={{ fontSize: '0.8rem', color: '#94a3b8' }}>{totalDepartments} Departments registered</span>
            </div>
          </div>
          <p style={{ fontSize: '0.85rem', color: '#64748b' }}>Manage organizational units, department codes, HQ locations, and employee assignments.</p>
        </div>

        <div className="glass-panel glass-panel-interactive" style={{ padding: '24px', cursor: 'pointer' }} onClick={() => setActiveTab('designations')}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '14px', marginBottom: '12px' }}>
            <div style={{ padding: '10px', borderRadius: '12px', background: 'rgba(139, 92, 246, 0.15)' }}>
              <Briefcase size={24} color="#8b5cf6" />
            </div>
            <div>
              <h3 style={{ fontSize: '1.1rem', fontWeight: 700 }}>Designations & Roles</h3>
              <span style={{ fontSize: '0.8rem', color: '#94a3b8' }}>{totalDesignations} Job Titles active</span>
            </div>
          </div>
          <p style={{ fontSize: '0.85rem', color: '#64748b' }}>Configure job positions, unique designation codes, and salary validation rules.</p>
        </div>

        <div className="glass-panel glass-panel-interactive" style={{ padding: '24px', cursor: 'pointer' }} onClick={() => setActiveTab('cities')}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '14px', marginBottom: '12px' }}>
            <div style={{ padding: '10px', borderRadius: '12px', background: 'rgba(236, 72, 153, 0.15)' }}>
              <MapPin size={24} color="#ec4899" />
            </div>
            <div>
              <h3 style={{ fontSize: '1.1rem', fontWeight: 700 }}>Office Locations</h3>
              <span style={{ fontSize: '0.8rem', color: '#94a3b8' }}>{totalCities} Global Cities</span>
            </div>
          </div>
          <p style={{ fontSize: '0.85rem', color: '#64748b' }}>Track employee geographic distribution across states, cities, and countries.</p>
        </div>
      </div>

    </div>
  );
}
