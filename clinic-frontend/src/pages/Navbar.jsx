import React from 'react';
import { Link } from 'react-router-dom';

function Navbar() {
    return (
        <div style={{ position: 'fixed', top: 0, left: 0, width: '180px', height: '100vh', backgroundColor: '#101214', padding: '20px', boxSizing: 'border-box', color: '#ffffff' }}>
            <Link to="/office" style={{ display: 'block', marginBottom: '10px' }}>Add Office</Link>
            <Link to="/offices" style={{ display: 'block', marginBottom: '10px' }}>View Offices</Link>
            <Link to="/doctor" style={{ display: 'block', marginBottom: '10px' }}>Add Doctor</Link>
            <Link to="/doctors" style={{ display: 'block', marginBottom: '10px' }}>View Doctors</Link>
            <Link to="/patient" style={{ display: 'block', marginBottom: '10px' }}>Add Patient</Link>
            <Link to="/patients" style={{ display: 'block', marginBottom: '10px' }}>View Patients</Link>
            <Link to="/appointment" style={{ display: 'block', marginBottom: '10px' }}>Add Appointment</Link>
            <Link to="/appointments" style={{ display: 'block' }}>View Appointments</Link>
        </div>
    );
}

export default Navbar;