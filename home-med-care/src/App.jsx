import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import HomePage from "./Components/HomePage/homePage";
import RegisterLab from "./Components/Register/RegisterLab";
import Login from "./Components/Register/LogIn";
import LabProfile from "./Components/Profile/LabProfile";
import Services from "./Components/Services/Services";
import RegisterUser from "./Components/Register/RegisterUser";
import UserLogin from "./Components/Register/UserLogin";
import UserProfile from "./Components/Profile/UserProfile";
import ProfileRedirect from './Components/Profile/ProfileRedirect';
import BookingPage from "./Components/Services/BookingPage";


function App() {
  return (
    <Router>
      <Routes>
        {/* HomePage should be mapped to "/" */}
        <Route path="/" element={<HomePage/>} />
        <Route path="/register" element={<RegisterLab/>} />
        <Route path="/login" element={<Login/>} />
        <Route path="/profile" element={<ProfileRedirect/>}/>
        <Route path="/user-profile" element={<UserProfile/>}/>
        <Route path="/lab-profile" element={<LabProfile/>}/>
        <Route path="/services" element={<Services/>}/>
        <Route path="/user-register" element={<RegisterUser/>}/>
        <Route path="/Userlogin"   element={<UserLogin/>}/>
        <Route path="/book" element={<BookingPage/>}/>
      </Routes>
    </Router>
   
  );
}

export default App;
