import './App.css'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Header from './components/Header'
import Navbar from './pages/Navbar'
import Office from './pages/Office'
import Offices from './pages/Offices'
import Doctor from './pages/Doctor'
import Doctors from './pages/Doctors'
import Patient from './pages/Patient'
import Patients from './pages/Patients'
import Appointment from './pages/Appointment'
import Appointments from './pages/Appointments'

function App() {
  return (
    <BrowserRouter>
      <Header />
      <Navbar />
      <Routes>
        <Route path="/" element={<Offices />} />
        <Route path="/office" element={<Office />} />
        <Route path="/offices" element={<Offices />} />
        <Route path="/doctor" element={<Doctor />} />
        <Route path="/doctors" element={<Doctors />} />
        <Route path="/patient" element={<Patient />} />
        <Route path="/patients" element={<Patients />} />
        <Route path="/appointment" element={<Appointment />} />
        <Route path="/appointments" element={<Appointments />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App