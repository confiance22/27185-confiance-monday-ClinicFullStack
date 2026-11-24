import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Button from '../components/Button';

function Doctor() {
    const [first_name, setFirstName] = useState('');
    const [last_name, setLastName] = useState('');
    const [date_of_birth, setDateOfBirth] = useState('');
    const [editId, setEditId] = useState(null);

    useEffect(() => {
        const saved = localStorage.getItem('editDoctor');
        if (saved) {
            const doctor = JSON.parse(saved);
            setFirstName(doctor.firstName);
            setLastName(doctor.lastName);
            setDateOfBirth(doctor.dateOfBirth);
            setEditId(doctor.id);
            localStorage.removeItem('editDoctor');
        }
    }, []);

    const saveDoctor = () => {
        const doctorData = { firstName: first_name, lastName: last_name, dateOfBirth: date_of_birth };

        if (editId) {
            axios.put('http://localhost:8080/api/doctors/update/' + editId, doctorData)
                .then(() => { alert('Yooo doctor updated!!!');
                    setEditId(null); setFirstName(''); setLastName(''); setDateOfBirth('');
                })
                .catch((error) => { console.error(error); alert('Failed to update doctor.'); });
        } else {
            axios.post('http://localhost:8080/api/doctors/save', doctorData)
                .then(() => { alert('Yooo doctor saved!!!');
                    setFirstName(''); setLastName(''); setDateOfBirth('');
                })
                .catch((error) => { console.error(error); alert('Failed to save doctor.'); });
        }
    };

    return (
        <div style={{ border: '2px solid black', margin: '100px auto', padding: '30px', width: '350px', borderRadius: '0px' }}>
            <h2 style={{ textAlign: 'center', fontSize: '30px' }} className="font-bold">Save Doctor</h2>
            <label style={{ marginLeft: '20px' }}>First name:</label><br />
            <input style={{ marginLeft: '20px', border: '1px solid #9ca3af', padding: '4px 8px', borderRadius: '0px' }} type="text" placeholder="Enter first name" value={first_name} onChange={(e) => setFirstName(e.target.value)} />
            <br />
            <label style={{ marginLeft: '20px' }}>Last name:</label><br />
            <input style={{ marginLeft: '20px', border: '1px solid #9ca3af', padding: '4px 8px', borderRadius: '0px' }} type="text" placeholder="Enter last name" value={last_name} onChange={(e) => setLastName(e.target.value)} />
            <br />
            <label style={{ marginLeft: '20px' }}>Date of birth:</label><br />
            <input style={{ marginLeft: '20px', border: '1px solid #9ca3af', padding: '4px 8px', borderRadius: '0px' }} type="date" value={date_of_birth} onChange={(e) => setDateOfBirth(e.target.value)} />
            <div>
                <Button buttonlabel={editId ? 'Update' : 'Save'} action={saveDoctor} />
            </div>
        </div>
    );
}

export default Doctor;