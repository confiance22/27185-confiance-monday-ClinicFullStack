import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Button from '../components/Button';

function Appointments() {
    const [appointments, setAppointments] = useState([]);
    const [search, setSearch] = useState('');
    const [filtered, setFiltered] = useState([]);

    const loadAppointments = () => {
        axios.get('http://localhost:8080/api/appointments/all')
            .then((response) => {
                setAppointments(response.data);
                setFiltered(response.data);
            })
            .catch((error) => { console.error(error); });
    };

    useEffect(() => { loadAppointments(); }, []);

    const handleSearch = () => {
        const result = appointments.filter((a) =>
            (a.reason || '').toLowerCase().includes(search.toLowerCase()) ||
            (a.status || '').toLowerCase().includes(search.toLowerCase())
        );
        setFiltered(result);
    };

    const handleDelete = (id) => {
        axios.delete('http://localhost:8080/api/appointments/delete/' + id)
            .then(() => { alert('Appointment deleted'); loadAppointments(); })
            .catch((error) => { console.error(error); alert('Failed to delete appointment'); });
    };

    const handleUpdate = (appointment) => {
        localStorage.setItem('editAppointment', JSON.stringify(appointment));
        window.location.href = '/appointment';
    };

    const boxStyle = { border: '2px solid black', margin: '100px auto', padding: '20px', width: '600px', borderRadius: '0px' };

    return (
        <div style={boxStyle}>
            <h2 style={{ textAlign: 'center', fontSize: '24px' }} className="font-bold">All Appointments</h2>
            <div style={{ marginBottom: '10px' }}>
                <input style={{ border: '1px solid #9ca3af', padding: '4px 8px', borderRadius: '0px', marginRight: '5px', width: '200px' }} type="text" placeholder="Search by reason or status" value={search} onChange={(e) => setSearch(e.target.value)} />
                <button style={{ padding: '4px 14px', border: '1px solid #000000', backgroundColor: '#000000', color: '#ffffff', cursor: 'pointer', borderRadius: '0px' }} onClick={handleSearch}>Search</button>
            </div>
            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                    <tr>
                        <th style={{ border: '1px solid #9ca3af', padding: '6px' }}>Date</th>
                        <th style={{ border: '1px solid #9ca3af', padding: '6px' }}>Reason</th>
                        <th style={{ border: '1px solid #9ca3af', padding: '6px' }}>Status</th>
                        <th style={{ border: '1px solid #9ca3af', padding: '6px' }}>Action</th>
                    </tr>
                </thead>
                <tbody>
                    {filtered.map((a) => (
                        <tr key={a.id}>
                            <td style={{ border: '1px solid #9ca3af', padding: '6px' }}>{a.appointmentDate}</td>
                            <td style={{ border: '1px solid #9ca3af', padding: '6px' }}>{a.reason}</td>
                            <td style={{ border: '1px solid #9ca3af', padding: '6px' }}>{a.status}</td>
                            <td style={{ border: '1px solid #9ca3af', padding: '6px' }}>
                                <Button buttonlabel="Update" action={() => handleUpdate(a)} />
                                <Button buttonlabel="Delete" action={() => handleDelete(a.id)} />
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default Appointments;