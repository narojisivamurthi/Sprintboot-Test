import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Users, Building2, Briefcase, MapPin, CheckCircle, UserPlus, ShieldCheck } from 'lucide-react';
import { useAppContext } from '../context/AppContext';
import Card from '../components/common/Card';
import Button from '../components/common/Button';

export default function DashboardPage() {
  const navigate = useNavigate();
  const { departments, designations, cities } = useAppContext();

  const totalDepartments = departments.length;
  const totalDesignations = designations.length;
  const totalCities = cities.length;

  const kpis = [
    { title: 'Total Departments', count: totalDepartments, icon: Building2, color: '#3b82f6', subtitle: 'Active business units' },
    { title: 'Designations', count: totalDesignations, icon: Briefcase, color: '#8b5cf6', subtitle: 'Job roles & salary bands' },
    { title: 'Cities / Offices', count: totalCities, icon: MapPin, color: '#ec4899', subtitle: 'Global location coverage' },
    { title: 'System Status', count: '100%', icon: CheckCircle, color: '#10b981', subtitle: 'Operational readiness' }
  ];

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      
      {/* Welcome Banner */}
      <Card style={{ background: 'linear-gradient(135deg, rgba(99, 102, 241, 0.15) 0%, rgba(18, 24, 38, 0.8) 100%)', border: '1px solid rgba(99, 102, 241, 0.3)', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '20px' }}>
        <div>
          <div style={{ display: 'inline-flex', alignItems: 'center', gap: '6px', background: 'rgba(99, 102, 241, 0.2)', color: '#a5b4fc', padding: '4px 12px', borderRadius: '20px', fontSize: '0.75rem', fontWeight: 700, marginBottom: '12px' }}>
            <ShieldCheck size={14} /> Full-Stack Enterprise Architecture
          </div>
          <h2 style={{ fontSize: '1.75rem', fontWeight: 800, marginBottom: '8px' }}>
            Enterprise HR & Workforce Portal
          </h2>
          <p style={{ color: '#94a3b8', fontSize: '0.95rem', maxWidth: '600px' }}>
            Real-time management for employee directory, departmental structures, designation bands, and global office locations.
          </p>
        </div>

        <div style={{ display: 'flex', gap: '12px' }}>
          <Button variant="primary" onClick={() => navigate('/employees')} style={{ padding: '12px 20px', fontSize: '0.9rem' }}>
            <UserPlus size={18} /> View Employee Directory
          </Button>
        </div>
      </Card>

      {/* KPI Cards Grid */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '20px' }}>
        {kpis.map((kpi, idx) => {
          const Icon = kpi.icon;
          return (
            <Card key={idx} interactive>
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
            </Card>
          );
        })}
      </div>

      {/* Module Navigation Cards */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '20px' }}>
        <Card interactive onClick={() => navigate('/departments')}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '14px', marginBottom: '12px' }}>
            <div style={{ padding: '10px', borderRadius: '12px', background: 'rgba(59, 130, 246, 0.15)' }}>
              <Building2 size={24} color="#3b82f6" />
            </div>
            <div>
              <h3 style={{ fontSize: '1.1rem', fontWeight: 700 }}>Departments</h3>
              <span style={{ fontSize: '0.8rem', color: '#94a3b8' }}>{totalDepartments} Departments registered</span>
            </div>
          </div>
          <p style={{ fontSize: '0.85rem', color: '#64748b' }}>Manage organizational units, department codes, HQ locations, and employee assignments.</p>
        </Card>

        <Card interactive onClick={() => navigate('/designations')}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '14px', marginBottom: '12px' }}>
            <div style={{ padding: '10px', borderRadius: '12px', background: 'rgba(139, 92, 246, 0.15)' }}>
              <Briefcase size={24} color="#8b5cf6" />
            </div>
            <div>
              <h3 style={{ fontSize: '1.1rem', fontWeight: 700 }}>Designations</h3>
              <span style={{ fontSize: '0.8rem', color: '#94a3b8' }}>{totalDesignations} Job Titles active</span>
            </div>
          </div>
          <p style={{ fontSize: '0.85rem', color: '#64748b' }}>Configure job positions, unique designation codes, and salary validation rules.</p>
        </Card>

        <Card interactive onClick={() => navigate('/cities')}>
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
        </Card>
      </div>

    </div>
  );
}
