import React from 'react';

export default function SelectField({
  label,
  name,
  value,
  onChange,
  options = [],
  placeholder = 'Select option...',
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
      <select
        name={name}
        value={value}
        onChange={onChange}
        required={required}
        disabled={disabled}
        className={`input-field ${className}`}
        style={{
          borderColor: error ? '#ef4444' : undefined,
          opacity: disabled ? 0.6 : 1
        }}
        {...props}
      >
        {placeholder && <option value="">{placeholder}</option>}
        {options.map((opt, idx) => {
          const val = typeof opt === 'object' ? opt.value : opt;
          const lbl = typeof opt === 'object' ? opt.label : opt;
          return (
            <option key={idx} value={val}>
              {lbl}
            </option>
          );
        })}
      </select>
      {error && (
        <span style={{ fontSize: '0.75rem', color: '#fca5a5', fontWeight: 500 }}>
          {error}
        </span>
      )}
    </div>
  );
}
