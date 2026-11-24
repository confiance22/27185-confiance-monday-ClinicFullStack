import './App.css'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Header from './components/Header'
import Navbar from './pages/Navbar'
import Office from './pages/Office'
import Offices from './pages/Offices'

function App() {
  return (
    <BrowserRouter>
      {/* <Header /> */}
      <Navbar />
      <Routes>
        <Route path="/offices" element={<Offices />} />
        <Route path="/office" element={<Office />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App