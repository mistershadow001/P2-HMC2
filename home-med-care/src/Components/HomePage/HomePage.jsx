import React, { useState } from "react";
import { FaUserCircle } from "react-icons/fa";
import { Link } from "react-router-dom";

const HomePage = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [filteredLabs, setFilteredLabs] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  // Fetch labs based on search
  const handleSearch = async () => {
    if (!searchTerm.trim()) {
      setError("Please enter a location.");
      return;
    }

    setLoading(true);
    setError("");

    try {
      const response = await fetch(`http://localhost:8080/labs/search-labs?address=${searchTerm}`);
      if (!response.ok) {
        throw new Error("Failed to fetch labs");
      }

      const data = await response.json();
      setFilteredLabs(data);
    } catch (err) {
      setError("Error fetching labs. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  // Clear search results
  const handleClear = () => {
    setSearchTerm("");
    setFilteredLabs([]);
    setError("");
  };

  // Book Now functionality
  const handleBookNow = (labName) => {
    alert(`Booking request sent for ${labName}`);
  };

  return (
    <div className="w-full min-h-screen bg-gray-200">
      {/* Navbar */}
      <nav className="flex justify-between items-center p-6 bg-white shadow-md">
        <div className="text-2xl font-bold text-gray-800">HMC</div>
        <div className="hidden md:flex space-x-6 text-gray-700">
          <a href="#" className="hover:text-gray-900">Why HMC</a>
          <Link to="/login" className="hover:text-gray-900">Lab login</Link>
          <Link to="/Userlogin" className="hover:text-gray-900">User login</Link>
          <Link to="/user-register" className="hover:text-gray-900">Register</Link>
          <Link to="/register" className="hover:text-gray-900">Register your service</Link>
        </div>
        <div className="space-x-4">
          <Link to="/profile">
            <button className="text-green-600 px-4 py-2 text-2xl rounded cursor-pointer">
              <FaUserCircle />
            </button>
          </Link>
          <Link to="/services">
            <button className="bg-green-600 text-white px-4 py-2 rounded">Services</button>
          </Link>
        </div>
      </nav>

      {/* Hero Section */}
      <header className="relative w-full flex flex-col md:flex-row items-center p-10 md:p-20 ml-35">
        <div className="md:w-1/2 space-y-6">
          <h1 className="text-5xl font-semibold text-gray-800 inline-block">
            Home-Med-Care: Reliable Lab Testing at Your Doorstep!
          </h1>
        </div>
        <div className="md:w-1/2 mt-6 md:mt-0">
          <img
            src="https://drsunny.in/wp-content/uploads/2020/11/home-visit-1200x900.jpg"
            alt="Family Care"
            className="w-90 rounded-lg shadow-lg shadow-green-600"
          />
        </div>
      </header>

      {/* Search Section */}
      <div className="flex justify-center items-center mt-6  mr-50 px-6">
        <div className="bg-white shadow-md p-4 rounded-lg flex w-full md:w-2/3 space-x-4">
          <input
            type="text"
            placeholder="Enter location (e.g., Mumbai, Satara, Karad)"
            className="flex-grow p-3 focus:outline-none border border-gray-300 rounded-lg"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <button
            className="bg-green-600 text-white px-6 py-3 rounded-lg"
            onClick={handleSearch}
            disabled={loading}
          >
            {loading ? "Searching..." : "Search"}
          </button>
          <button
            className="bg-green-600 hover:bg-red-500 text-white px-6 py-3 rounded-lg"
            onClick={handleClear}
          >
            Clear
          </button>
        </div>
      </div>

      {/* Display Search Results */}
      {filteredLabs.length > 0 && (
        <div className="mt-6 px-6 overflow-x-auto">
          <div className="flex space-x-6 w-max">
            {filteredLabs.map((lab) => (
              <div
                key={lab.id}
                className="bg-white rounded-lg shadow-lg p-6 transition-transform transform hover:scale-105 min-w-[320px] md:min-w-[400px] lg:min-w-[450px]"
              >
                <h3 className="text-2xl font-bold text-center text-green-600 mb-4">{lab.labName}</h3>
                <p className="text-gray-600 mb-2"><strong>Email:</strong> {lab.email}</p>
                <p className="text-gray-600 mb-2"><strong>Phone:</strong> {lab.phone}</p>
                <p className="text-gray-600 mb-2"><strong>Location:</strong> {lab.address}</p>
                <p className="text-gray-600 mb-4">
                  <strong>Tests:</strong> {lab.services}
                </p>
                <button
                  onClick={() => handleBookNow(lab.labName)}
                  className="w-full bg-green-600 text-white py-2 rounded-lg hover:bg-green-700 transition"
                >
                  Book Now
                </button>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default HomePage;
