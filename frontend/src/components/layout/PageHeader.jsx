import React from 'react';

export default function PageHeader({ title, subtitle, actionButton }) {
  return (
    <div className="glass-panel" style={{ padding: '20px 24px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px' }}>
      <div>
        {title && <h2 style={{ fontSize: '1.25rem', fontWeight: 800 }}>{title}</h2>}
        {subtitle && <span style={{ fontSize: '0.85rem', color: '#94a3b8' }}>{subtitle}</span>}
      </div>
      {actionButton && <div>{actionButton}</div>}
    </div>
  );
}
