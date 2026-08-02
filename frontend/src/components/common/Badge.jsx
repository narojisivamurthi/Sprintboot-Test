import React from 'react';

export default function Badge({ children, variant = 'code', className = '', style = {} }) {
  const getBadgeClass = () => {
    switch (variant) {
      case 'active':
      case 'ACTIVE':
        return 'badge-active';
      case 'inactive':
      case 'INACTIVE':
        return 'badge-inactive';
      case 'leave':
      case 'ON_LEAVE':
        return 'badge-leave';
      case 'code':
      default:
        return 'badge-code';
    }
  };

  return (
    <span className={`badge ${getBadgeClass()} ${className}`} style={style}>
      {children}
    </span>
  );
}
