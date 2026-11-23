import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Button from '../components/Button';

function Office() {
    const [name, setName] = useState('');
    const [office_number, setOfficeNumber] = useState('');
    const [editId, setEditId] = useState(null);

    useEffect(() => {
        const saved = localStorage.getItem('editOffice');
        if (saved) {
            const office = JSON.parse(saved);
            setName(office.name);
            setOfficeNumber(office.officeNumber);
            setEditId(office.id);
            localStorage.removeItem('editOffice');
        }
    }, []);

    const saveOffice = () => {
        const officeData = { name: name, officeNumber: Number(office_number) };

        if (editId) {
            axios.put('http://localhost:8080/api/offices/update/' + editId, officeData)
                .then((response) => { alert('Yooo office updated!!!');
                    console.log(response.data);
                    setEditId(null);
                    setName('');
                    setOfficeNumber('');
                })
                .catch((error) => { console.error(error);
                    alert(error.response ? error.response.data : 'Failed to update office.');
                });
        } else {
            axios.post('http://localhost:8080/api/offices/save', officeData)
                .then((response) => { alert('Yooo office saved!!!');
                    console.log(response.data);
                    setName('');
                    setOfficeNumber('');
                })
                .catch((error) => { console.error(error);
                    alert(error.response ? error.response.data : 'Failed to save office.');
                });
        }
    };

    const boxStyle = { border: '2px solid black', margin: '100px auto', padding: '30px', width: '350px', borderRadius: '0px' };

    return (
        <div style={boxStyle}>
            <h2 style={{ textAlign: 'center', fontSize: '30px' }} className="font-bold">Save Offices</h2>
            <label style={{ marginLeft: '20px' }}>Name:</label><br />
            <input style={{ marginLeft: '20px', border: '1px solid #9ca3af', padding: '4px 8px', borderRadius: '0px' }} type="text" placeholder="Enter Office name" value={name} onChange={(e) => setName(e.target.value)}/>
            <br />
            <label style={{ marginLeft: '20px' }}>Office number:</label>
            <input style={{ marginLeft: '20px', border: '1px solid #9ca3af', padding: '4px 8px', borderRadius: '0px' }} type="text" placeholder="Enter office number" value={office_number} onChange={(e) => setOfficeNumber(e.target.value)}/>

            <div>
                <Button buttonlabel={editId ? 'Update' : 'Save'} action={saveOffice} />
            </div>
        </div>
    );
}

export default Office;