import React from 'react';

export default function InputField({
  label,
  type = 'text',
  name,
  value,
  onChange,
  placeholder = '',
  required = false,
  error = '',
  disabled = false,
  className = '',
  style = {},
  ...props
}) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '6px', ...style }}>
      {label && (
        <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#94a3b8' }}>
          {label} {required && <span style={{ color: '#ef4444' }}>*</span>}
        </label>
      )}
      <input
        type={type}
        name={name}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        required={required}
        disabled={disabled}
        className={`input-field ${className}`}
        style={{
          borderColor: error ? '#ef4444' : undefined,
          opacity: disabled ? 0.6 : 1
        }}
        {...props}
      />
      {error && (
        <span style={{ fontSize: '0.75rem', color: '#fca5a5', fontWeight: 500 }}>
          {error}
        </span>
      )}
    </div>
  );
}
