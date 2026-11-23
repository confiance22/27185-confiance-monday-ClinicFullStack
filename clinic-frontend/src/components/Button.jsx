import React from 'react';

export default function Button({ buttonlabel, action }) {
  return (
    <div style={{ display: 'inline-block', margin: '8px 4px' }}>
      <button
        onClick={action}
        style={{
          padding: '6px 14px',
          border: '1px solid #000000',
         
          backgroundColor: '#000000',
          color: '#ffffff',
          cursor: 'pointer',
          fontWeight: '600'
        }}
      >
        {buttonlabel}
      </button>
    </div>
  );
}