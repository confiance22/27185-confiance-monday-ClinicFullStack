import React from 'react';
import { Link } from 'react-router-dom';

function Navbar() {
    return (
        <div style={{ position: 'fixed', top: 0, left: 0, width: '150px', height: '100vh', backgroundColor: '#f8f9fa', padding: '20px', boxSizing: 'border-box' }}>
            <Link to="/office" style={{ display: 'block', marginBottom: '10px' }}>Add Office</Link>
            <Link to="/offices" style={{ display: 'block' }}>View All Offices</Link>
        </div>
    );
}

export default Navbar;