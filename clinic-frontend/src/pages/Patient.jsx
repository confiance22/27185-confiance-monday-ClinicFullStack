import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Button from '../components/Button';

function Patient() {
    const [first_name, setFirstName] = useState('');
    const [last_name, setLastName] = useState('');
    const [ss_number, setSsNumber] = useState('');
    const [date_of_birth, setDateOfBirth] = useState('');
    const [editId, setEditId] = useState(null);

    useEffect(() => {
        const saved = localStorage.getItem('editPatient');
        if (saved) {
            const patient = JSON.parse(saved);
            setFirstName(patient.firstName);
            setLastName(patient.lastName);
            setSsNumber(patient.ssNumber);
            setDateOfBirth(patient.dateOfBirth);
            setEditId(patient.id);
            localStorage.removeItem('editPatient');
        }
    }, []);

    const savePatient = () => {
        const patientData = { firstName: first_name, lastName: last_name, ssNumber: ss_number, dateOfBirth: date_of_birth };

        if (editId) {
            axios.put('http://localhost:8080/api/patients/update/' + editId, patientData)
                .then(() => { alert('Yooo patient updated!!!');
                    setEditId(null); setFirstName(''); setLastName(''); setSsNumber(''); setDateOfBirth('');
                })
                .catch((error) => { console.error(error); alert('Failed to update patient.'); });
        } else {
            axios.post('http://localhost:8080/api/patients/save', patientData)
                .then(() => { alert('Yooo patient saved!!!');
                    setFirstName(''); setLastName(''); setSsNumber(''); setDateOfBirth('');
                })
                .catch((error) => { console.error(error); alert('Failed to save patient.'); });
        }
    };

    return (
        <div style={{ border: '2px solid black', margin: '100px auto', padding: '30px', width: '350px', borderRadius: '0px' }}>
            <h2 style={{ textAlign: 'center', fontSize: '30px' }} className="font-bold">Save Patient</h2>
            <label style={{ marginLeft: '20px' }}>First name:</label><br />
            <input style={{ marginLeft: '20px', border: '1px solid #9ca3af', padding: '4px 8px', borderRadius: '0px' }} type="text" placeholder="Enter first name" value={first_name} onChange={(e) => setFirstName(e.target.value)} />
            <br />
            <label style={{ marginLeft: '20px' }}>Last name:</label><br />
            <input style={{ marginLeft: '20px', border: '1px solid #9ca3af', padding: '4px 8px', borderRadius: '0px' }} type="text" placeholder="Enter last name" value={last_name} onChange={(e) => setLastName(e.target.value)} />
            <br />
            <label style={{ marginLeft: '20px' }}>SS number:</label><br />
            <input style={{ marginLeft: '20px', border: '1px solid #9ca3af', padding: '4px 8px', borderRadius: '0px' }} type="text" placeholder="Enter SS number" value={ss_number} onChange={(e) => setSsNumber(e.target.value)} />
            <br />
            <label style={{ marginLeft: '20px' }}>Date of birth:</label><br />
            <input style={{ marginLeft: '20px', border: '1px solid #9ca3af', padding: '4px 8px', borderRadius: '0px' }} type="date" value={date_of_birth} onChange={(e) => setDateOfBirth(e.target.value)} />
            <div>
                <Button buttonlabel={editId ? 'Update' : 'Save'} action={savePatient} />
            </div>
        </div>
    );
}

export default Patient;