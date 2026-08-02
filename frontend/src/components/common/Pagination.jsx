import React from 'react';
import { ChevronLeft, ChevronRight, ChevronsLeft, ChevronsRight } from 'lucide-react';
import Button from './Button';

export default function Pagination({
  currentPage = 0,
  totalPages = 1,
  pageSize = 10,
  totalElements = 0,
  onPageChange,
  onPageSizeChange
}) {
  const getPageNumbers = () => {
    const pages = [];
    const maxButtons = 5;
    let startPage = Math.max(0, currentPage - Math.floor(maxButtons / 2));
    let endPage = startPage + maxButtons - 1;

    if (endPage >= totalPages) {
      endPage = totalPages - 1;
      startPage = Math.max(0, endPage - maxButtons + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    return pages;
  };

  return (
    <div style={{ padding: '16px 24px', borderTop: '1px solid rgba(255, 255, 255, 0.05)', display: 'flex', flexWrap: 'wrap', gap: '16px', alignItems: 'center', justifyContent: 'space-between' }}>
      
      {/* Page Info & Per Page Selector */}
      <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
        <span style={{ fontSize: '0.85rem', color: '#94a3b8' }}>
          Showing Page <strong>{currentPage + 1}</strong> of <strong>{totalPages}</strong> ({totalElements} Items)
        </span>

        {onPageSizeChange && (
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <span style={{ fontSize: '0.8rem', color: '#64748b' }}>Per page:</span>
            <select
              className="input-field"
              value={pageSize}
              onChange={(e) => onPageSizeChange(parseInt(e.target.value, 10))}
              style={{ width: 'auto', padding: '4px 8px', fontSize: '0.8rem' }}
            >
              <option value={10}>10</option>
              <option value={25}>25</option>
              <option value={50}>50</option>
              <option value={100}>100</option>
            </select>
          </div>
        )}
      </div>

      {/* Dynamic 5-Page Number Window Navigation */}
      <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
        <Button
          variant="icon"
          disabled={currentPage === 0}
          onClick={() => onPageChange(0)}
          title="First Page"
        >
          <ChevronsLeft size={16} />
        </Button>

        <Button
          variant="secondary"
          disabled={currentPage === 0}
          onClick={() => onPageChange(currentPage - 1)}
          style={{ padding: '6px 12px', fontSize: '0.8rem' }}
        >
          <ChevronLeft size={16} /> Prev
        </Button>

        {getPageNumbers().map((pNum) => (
          <Button
            key={pNum}
            variant={pNum === currentPage ? 'primary' : 'secondary'}
            onClick={() => onPageChange(pNum)}
            style={{
              padding: '6px 12px',
              fontSize: '0.82rem',
              fontWeight: pNum === currentPage ? 700 : 600,
              minWidth: '36px',
              justifyContent: 'center',
              background: pNum === currentPage ? 'linear-gradient(135deg, #6366f1, #4f46e5)' : 'rgba(255, 255, 255, 0.05)',
              color: pNum === currentPage ? '#ffffff' : '#94a3b8',
              border: pNum === currentPage ? 'none' : '1px solid rgba(255, 255, 255, 0.08)'
            }}
          >
            {pNum + 1}
          </Button>
        ))}

        <Button
          variant="secondary"
          disabled={currentPage >= totalPages - 1}
          onClick={() => onPageChange(currentPage + 1)}
          style={{ padding: '6px 12px', fontSize: '0.8rem' }}
        >
          Next <ChevronRight size={16} />
        </Button>

        <Button
          variant="icon"
          disabled={currentPage >= totalPages - 1}
          onClick={() => onPageChange(totalPages - 1)}
          title="Last Page"
        >
          <ChevronsRight size={16} />
        </Button>
      </div>

    </div>
  );
}
