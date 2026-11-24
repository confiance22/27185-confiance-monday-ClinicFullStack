import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Button from '../components/Button';

function Appointment() {
    const [appointment_date, setAppointmentDate] = useState('');
    const [reason, setReason] = useState('');
    const [status, setStatus] = useState('PENDING');
    const [patient_id, setPatientId] = useState('');
    const [doctor_id, setDoctorId] = useState('');
    const [editId, setEditId] = useState(null);

    useEffect(() => {
        const saved = localStorage.getItem('editAppointment');
        if (saved) {
            const a = JSON.parse(saved);
            setAppointmentDate(a.appointmentDate);
            setReason(a.reason);
            setStatus(a.status);
            setPatientId(a.patient ? a.patient.id : '');
            setDoctorId(a.doctor ? a.doctor.id : '');
            setEditId(a.id);
            localStorage.removeItem('editAppointment');
        }
    }, []);

    const saveAppointment = () => {
        const appointmentData = {
            appointmentDate: appointment_date,
            reason: reason,
            status: status,
            patient: { id: patient_id },
            doctor: { id: doctor_id }
        };

        if (editId) {
            axios.put('http://localhost:8080/api/appointments/update/' + editId, appointmentData)
                .then(() => { alert('Yooo appointment updated!!!');
                    setEditId(null); setAppointmentDate(''); setReason(''); setStatus('PENDING'); setPatientId(''); setDoctorId('');
                })
                .catch((error) => { console.error(error); alert('Failed to update appointment.'); });
        } else {
            axios.post('http://localhost:8080/api/appointments/save', appointmentData)
                .then(() => { alert('Yooo appointment saved!!!');
                    setAppointmentDate(''); setReason(''); setStatus('PENDING'); setPatientId(''); setDoctorId('');
                })
                .catch((error) => { console.error(error); alert('Failed to save appointment.'); });
        }
    };

    return (
        <div style={{ border: '2px solid black', margin: '100px auto', padding: '30px', width: '350px', borderRadius: '0px' }}>
            <h2 style={{ textAlign: 'center', fontSize: '30px' }} className="font-bold">Save Appointment</h2>
            <label style={{ marginLeft: '20px' }}>Date:</label><br />
            <input style={{ marginLeft: '20px', border: '1px solid #9ca3af', padding: '4px 8px', borderRadius: '0px' }} type="date" value={appointment_date} onChange={(e) => setAppointmentDate(e.target.value)} />
            <br />
            <label style={{ marginLeft: '20px' }}>Reason:</label><br />
            <input style={{ marginLeft: '20px', border: '1px solid #9ca3af', padding: '4px 8px', borderRadius: '0px' }} type="text" placeholder="Enter reason" value={reason} onChange={(e) => setReason(e.target.value)} />
            <br />
            <label style={{ marginLeft: '20px' }}>Status:</label><br />
            <input style={{ marginLeft: '20px', border: '1px solid #9ca3af', padding: '4px 8px', borderRadius: '0px' }} type="text" placeholder="PENDING" value={status} onChange={(e) => setStatus(e.target.value)} />
            <br />
            <label style={{ marginLeft: '20px' }}>Patient ID:</label><br />
            <input style={{ marginLeft: '20px', border: '1px solid #9ca3af', padding: '4px 8px', borderRadius: '0px' }} type="text" placeholder="Enter patient id" value={patient_id} onChange={(e) => setPatientId(e.target.value)} />
            <br />
            <label style={{ marginLeft: '20px' }}>Doctor ID:</label><br />
            <input style={{ marginLeft: '20px', border: '1px solid #9ca3af', padding: '4px 8px', borderRadius: '0px' }} type="text" placeholder="Enter doctor id" value={doctor_id} onChange={(e) => setDoctorId(e.target.value)} />
            <div>
                <Button buttonlabel={editId ? 'Update' : 'Save'} action={saveAppointment} />
            </div>
        </div>
    );
}

export default Appointment;