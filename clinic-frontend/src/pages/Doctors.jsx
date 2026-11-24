import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Button from '../components/Button';

function Doctors() {
    const [doctors, setDoctors] = useState([]);
    const [search, setSearch] = useState('');
    const [filtered, setFiltered] = useState([]);

    const loadDoctors = () => {
        axios.get('http://localhost:8080/api/doctors/all')
            .then((response) => {
                setDoctors(response.data);
                setFiltered(response.data);
            })
            .catch((error) => { console.error(error); });
    };

    useEffect(() => { loadDoctors(); }, []);

    const handleSearch = () => {
        const result = doctors.filter((d) =>
            d.firstName.toLowerCase().includes(search.toLowerCase()) ||
            d.lastName.toLowerCase().includes(search.toLowerCase())
        );
        setFiltered(result);
    };

    const handleDelete = (id) => {
        axios.delete('http://localhost:8080/api/doctors/delete/' + id)
            .then(() => { alert('Doctor deleted'); loadDoctors(); })
            .catch((error) => { console.error(error); alert('Failed to delete doctor'); });
    };

    const handleUpdate = (doctor) => {
        localStorage.setItem('editDoctor', JSON.stringify(doctor));
        window.location.href = '/doctor';
    };

    const boxStyle = { border: '2px solid black', margin: '100px auto', padding: '20px', width: '600px', borderRadius: '0px' };

    return (
        <div style={boxStyle}>
            <h2 style={{ textAlign: 'center', fontSize: '24px' }} className="font-bold">All Available Doctors</h2>
            <div style={{ marginBottom: '10px' }}>
                <input style={{ border: '1px solid #9ca3af', padding: '4px 8px', borderRadius: '0px', marginRight: '5px', width: '200px' }} type="text" placeholder="Search by name" value={search} onChange={(e) => setSearch(e.target.value)} />
                <button style={{ padding: '4px 14px', border: '1px solid #000000', backgroundColor: '#000000', color: '#ffffff', cursor: 'pointer', borderRadius: '0px' }} onClick={handleSearch}>Search</button>
            </div>
            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                    <tr>
                        <th style={{ border: '1px solid #9ca3af', padding: '6px' }}>First Name</th>
                        <th style={{ border: '1px solid #9ca3af', padding: '6px' }}>Last Name</th>
                        <th style={{ border: '1px solid #9ca3af', padding: '6px' }}>Action</th>
                    </tr>
                </thead>
                <tbody>
                    {filtered.map((doctor) => (
                        <tr key={doctor.id}>
                            <td style={{ border: '1px solid #9ca3af', padding: '6px' }}>{doctor.firstName}</td>
                            <td style={{ border: '1px solid #9ca3af', padding: '6px' }}>{doctor.lastName}</td>
                            <td style={{ border: '1px solid #9ca3af', padding: '6px' }}>
                                <Button buttonlabel="Update" action={() => handleUpdate(doctor)} />
                                <Button buttonlabel="Delete" action={() => handleDelete(doctor.id)} />
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default Doctors;