import React from 'react';

export default function Card({ children, interactive = false, className = '', style = {}, onClick }) {
  const cardClass = interactive ? 'glass-panel glass-panel-interactive' : 'glass-panel';

  return (
    <div className={`${cardClass} ${className}`} style={{ padding: '24px', ...style }} onClick={onClick}>
      {children}
    </div>
  );
}
