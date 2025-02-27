import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";

function UserProfile() {
  const [userData, setUserData] = useState(null);
  const [bookings, setBookings] = useState([]);
  const [error, setError] = useState("");
  const [editMode, setEditMode] = useState(false);
  const [updatedData, setUpdatedData] = useState({});
  const navigate = useNavigate();

  useEffect(() => {
    fetch("http://localhost:8080/users/profile", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    })
      .then((response) => {
        if (response.status === 401) throw new Error("Not logged in");
        if (response.status === 404) throw new Error("User not found");
        return response.json();
      })
      .then((data) => {
        setUserData(data);
        setUpdatedData(data);
        if (data.id) fetchBookings(data.id);
      })
      .catch((err) => setError(err.message));
  }, []);

  const fetchBookings = (user_id) => {
    fetch(`http://localhost:8080/bookings/users/${user_id}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    })
      .then((res) => res.json())
      .then((data) => {
        console.log("Fetched Bookings:", data);
        setBookings(data);
      })
      .catch(() => setError("Failed to fetch bookings"));
  };

  const handleChange = (key, value) => {
    setUpdatedData((prev) => ({ ...prev, [key]: value }));
  };

  const handleSave = () => {
    fetch("http://localhost:8080/users/update-profile", {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
      body: JSON.stringify(updatedData),
    })
      .then((res) => res.json())
      .then((data) => {
        setUserData(data);
        setEditMode(false);
      })
      .catch(() => setError("Failed to update profile"));
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <div className="max-w-3xl mx-auto mt-8 p-6 bg-white shadow-md rounded-lg border border-gray-200">
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-2xl font-semibold text-gray-800">User Profile</h2>
        <button
          onClick={handleLogout}
          className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
        >
          Logout
        </button>
      </div>

      {error && <p className="text-red-500 text-center">{error}</p>}

      {userData ? (
        <div className="space-y-4">
          {Object.entries(userData).map(([key, value]) => {
            if (["email", "password", "id", "role"].includes(key)) return null;
            return (
              <div key={`${userData.id}-${key}`} className="flex justify-between items-center">
                <span className="font-medium capitalize text-gray-700">
                  {key.replace(/([A-Z])/g, " $1")}:
                </span>
                {editMode ? (
                  <input
                    type="text"
                    className="border px-2 py-1 rounded text-gray-800 w-1/2"
                    value={updatedData[key] ?? ""}
                    onChange={(e) => handleChange(key, e.target.value)}
                  />
                ) : (
                  <span className="text-gray-800">{value}</span>
                )}
              </div>
            );
          })}
        </div>
      ) : (
        <p className="text-gray-600 text-center">Loading...</p>
      )}

      <div className="mt-6 flex justify-center space-x-4">
        {editMode ? (
          <button
            onClick={handleSave}
            className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
          >
            Save Changes
          </button>
        ) : (
          <button
            onClick={() => setEditMode(true)}
            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
          >
            Edit Profile
          </button>
        )}
      </div>

      {/* BOOKINGS LIST WITH SCROLLING */}
      <div className="mt-8">
        <h3 className="text-xl font-semibold text-gray-800">My Bookings</h3>
        {bookings.length > 0 ? (
          <div className="mt-4 space-y-4 max-h-[400px] overflow-y-auto">
            {bookings.map((booking) => (
              <div
                key={booking.id}
                className="bg-gray-100 p-4 rounded-lg shadow-md border border-gray-300"
              >
                <p className="text-gray-700">
                  <span className="font-medium">Lab ID:</span> {booking.labId}
                </p>
                <p className="text-gray-700">
                  <span className="font-medium">Appointment Date:</span> {booking.appointmentDate}
                </p>
              </div>
            ))}
          </div>
        ) : (
          <p className="text-gray-600 mt-4">No bookings found.</p>
        )}
      </div>

      <Link to="/" className="block text-blue-500 mt-6 text-center">
        Home
      </Link>
    </div>
  );
}

export default UserProfile;
