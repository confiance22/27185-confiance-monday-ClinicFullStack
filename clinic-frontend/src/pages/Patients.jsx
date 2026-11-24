import React, { useState, useEffect } from 'react';
import axios from 'axios';

function Patients() {
    const [patients, setPatients] = useState([]);
    const [search, setSearch] = useState('');
    const [filtered, setFiltered] = useState([]);

    const loadPatients = () => {
        axios.get('http://localhost:8080/api/patients/all')
            .then((response) => {
                setPatients(response.data);
                setFiltered(response.data);
            })
            .catch((error) => { console.error(error); });
    };

    useEffect(() => { loadPatients(); }, []);

    const handleSearch = () => {
        const result = patients.filter((p) =>
            p.firstName.toLowerCase().includes(search.toLowerCase()) ||
            p.lastName.toLowerCase().includes(search.toLowerCase()) ||
            p.ssNumber.includes(search)
        );
        setFiltered(result);
    };

    const handleDelete = (id) => {
        axios.delete('http://localhost:8080/api/patients/delete/' + id)
            .then(() => { alert('Patient deleted'); loadPatients(); })
            .catch((error) => { console.error(error); alert('Failed to delete patient'); });
    };

    const handleUpdate = (patient) => {
        localStorage.setItem('editPatient', JSON.stringify(patient));
        window.location.href = '/patient';
    };

    const boxStyle = { border: '2px solid black', margin: '100px auto', padding: '20px', width: '600px', borderRadius: '0px' };

    return (
        <div style={boxStyle}>
            <h2 style={{ textAlign: 'center', fontSize: '24px' }} className="font-bold">All Patients</h2>
            <div style={{ marginBottom: '10px' }}>
                <input style={{ border: '1px solid #9ca3af', padding: '4px 8px', borderRadius: '0px', marginRight: '5px', width: '200px' }} type="text" placeholder="Search by name or SS number" value={search} onChange={(e) => setSearch(e.target.value)} />
                <button style={{ padding: '4px 14px', border: '1px solid #2563eb', backgroundColor: '#2563eb', color: '#ffffff', cursor: 'pointer', borderRadius: '0px' }} onClick={handleSearch}>Search</button>
            </div>
            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                    <tr>
                        <th style={{ border: '1px solid #9ca3af', padding: '6px' }}>First Name</th>
                        <th style={{ border: '1px solid #9ca3af', padding: '6px' }}>Last Name</th>
                        <th style={{ border: '1px solid #9ca3af', padding: '6px' }}>SS Number</th>
                        <th style={{ border: '1px solid #9ca3af', padding: '6px' }}>Action</th>
                    </tr>
                </thead>
                <tbody>
                    {filtered.map((patient) => (
                        <tr key={patient.id}>
                            <td style={{ border: '1px solid #9ca3af', padding: '6px' }}>{patient.firstName}</td>
                            <td style={{ border: '1px solid #9ca3af', padding: '6px' }}>{patient.lastName}</td>
                            <td style={{ border: '1px solid #9ca3af', padding: '6px' }}>{patient.ssNumber}</td>
                            <td style={{ border: '1px solid #9ca3af', padding: '6px' }}>
                                <button style={{ padding: '2px 8px', border: '1px solid #2563eb', backgroundColor: '#2563eb', color: '#ffffff', cursor: 'pointer', borderRadius: '0px', marginRight: '4px' }} onClick={() => handleUpdate(patient)}>Update</button>
                                <button style={{ padding: '2px 8px', border: '1px solid #dc2626', backgroundColor: '#dc2626', color: '#ffffff', cursor: 'pointer', borderRadius: '0px' }} onClick={() => handleDelete(patient.id)}>Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default Patients;