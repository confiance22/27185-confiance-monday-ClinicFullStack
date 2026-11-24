import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Button from '../components/Button';

function Offices() {
    const [offices, setOffices] = useState([]);
    const [search, setSearch] = useState('');
    const [filtered, setFiltered] = useState([]);

    const loadOffices = () => {
        axios.get('http://localhost:8080/api/offices/all')
            .then((response) => {
                setOffices(response.data);
                setFiltered(response.data);
            })
            .catch((error) => {
                console.error(error);
            });
    };

    useEffect(() => {
        loadOffices();
    }, []);

    const handleSearch = () => {
        const result = offices.filter((office) =>
            office.name.toLowerCase().includes(search.toLowerCase()) ||
            String(office.officeNumber).includes(search)
        );
        setFiltered(result);
    };

    const handleDelete = (id) => {
        axios.delete('http://localhost:8080/api/offices/delete/' + id)
            .then(() => {
                alert('Office deleted');
                loadOffices();
            })
            .catch((error) => {
                console.error(error);
                alert('Failed to delete office');
            });
    };

    const handleUpdate = (office) => {
        localStorage.setItem('editOffice', JSON.stringify(office));
        window.location.href = '/office';
    };

    const boxStyle = { border: '2px solid black', margin: '100px auto', padding: '20px', width: '600px', borderRadius: '0px' };

    return (
        <div style={boxStyle}>
            <h2 style={{ textAlign: 'center', fontSize: '24px' }} className="font-bold">All Available Offices</h2>

            <div style={{ marginBottom: '10px' }}>
                <input style={{ border: '1px solid #9ca3af', padding: '4px 8px', borderRadius: '0px', marginRight: '5px', width: '200px' }} type="text" placeholder="Search by name or number" value={search} onChange={(e) => setSearch(e.target.value)}/>
                <Button buttonlabel="Search" action={handleSearch} />
            </div>

            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                    <tr>
                        <th style={{ border: '1px solid #9ca3af', padding: '6px' }}>Name</th>
                        <th style={{ border: '1px solid #9ca3af', padding: '6px' }}>Number</th>
                        <th style={{ border: '1px solid #9ca3af', padding: '6px' }}>Action</th>
                    </tr>
                </thead>
                <tbody>
                    {filtered.map((office) => (
                        <tr key={office.id}>
                            <td style={{ border: '1px solid #9ca3af', padding: '6px' }}>{office.name}</td>
                            <td style={{ border: '1px solid #9ca3af', padding: '6px' }}>{office.officeNumber}</td>
                            <td style={{ border: '1px solid #9ca3af', padding: '6px' }}>
                                <Button buttonlabel="Update" action={() => handleUpdate(office)} />
                                <Button buttonlabel="Delete" action={() => handleDelete(office.id)} />
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default Offices;