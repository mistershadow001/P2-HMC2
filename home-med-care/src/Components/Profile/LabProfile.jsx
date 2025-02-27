import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function LabProfile() {
  const [labData, setLabData] = useState(null);
  const [error, setError] = useState("");
  const [bookings, setBookings] = useState([]);
  const navigate = useNavigate();

  // Fetch Lab Data
  useEffect(() => {
    const fetchLabData = async () => {
      try {
        const response = await fetch("http://localhost:8080/labs/lab-profile", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });

        if (!response.ok) throw new Error("Failed to fetch lab data");

        const data = await response.json();
        console.log("Lab Data:", data);
        setLabData(data);
      } catch (err) {
        setError(err.message);
      }
    };

    fetchLabData();
  }, []);

  // Fetch Lab Appointments
  useEffect(() => {
    if (!labData?.id) return;

    const fetchBookings = async () => {
      try {
        const response = await fetch(`http://localhost:8080/bookings/labs/${labData.id}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });

        if (!response.ok) throw new Error("Failed to load bookings");

        const data = await response.json();
        console.log("Bookings Data:", data);
        setBookings(data);
      } catch (err) {
        setError("Failed to load bookings");
      }
    };

    fetchBookings();
  }, [labData]);

  // Update Booking Status
  const handleUpdateStatus = async (bookingId) => {
    console.log("Clicked Booking ID:", bookingId); // Debugging log
  
    if (!bookingId) {
      console.error("Error: Booking ID is undefined");
      return;
    }
  
    try {
      const response = await fetch(`http://localhost:8080/bookings/update-status/${bookingId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        body: JSON.stringify({ status: "Completed" }),
      });
  
      if (!response.ok) throw new Error("Failed to update booking status");
  
      console.log(`Booking ${bookingId} marked as completed`);
  
      // Remove the completed booking from state
      setBookings((prevBookings) => prevBookings.filter((b) => b.id !== bookingId));
    } catch (err) {
      console.error("Error updating status:", err);
    }
  };
  

  return (
    <div className="max-w-3xl mx-auto mt-8 p-8 bg-white shadow-lg rounded-lg border border-gray-300">
      {/* Header */}
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-3xl font-bold text-gray-900">Lab Profile</h2>
        <div className="flex space-x-4">
          <button
            onClick={() => navigate("/")}
            className="bg-blue-500 text-white px-5 py-2 rounded-lg shadow-md hover:bg-blue-600 transition duration-300"
          >
            Home
          </button>
          <button
            onClick={() => {
              localStorage.removeItem("token");
              navigate("/login");
            }}
            className="bg-red-500 text-white px-5 py-2 rounded-lg shadow-md hover:bg-red-600 transition duration-300"
          >
            Logout
          </button>
        </div>
      </div>

      {error && <p className="text-red-600 text-center mb-4">{error}</p>}

      {/* Lab Details */}
      {labData ? (
        <div className="space-y-4 bg-gray-100 p-6 rounded-lg">
          {Object.entries(labData).map(([key, value]) =>
            ["email", "paymentId", "password", "id", "Role"].includes(key) ? null : (
              <div key={key} className="flex justify-between items-center">
                <span className="font-medium capitalize text-gray-700">
                  {key.replace(/([A-Z])/g, " $1")}:
                </span>
                <span className="text-gray-900">{value}</span>
              </div>
            )
          )}
        </div>
      ) : (
        <p className="text-gray-600 text-center">Loading...</p>
      )}

      {/* Appointments Section */}
      <div className="mt-8">
        <h3 className="text-2xl font-semibold text-gray-900 mb-4">Appointments</h3>
        {bookings.length > 0 ? (
          <div className="overflow-x-auto">
            <table className="w-full border-collapse border border-gray-300">
              <thead className="bg-gray-200">
                <tr>
                  <th className="border border-gray-300 px-4 py-2 text-left text-gray-700">Patient</th>
                  <th className="border border-gray-300 px-4 py-2 text-left text-gray-700">Phone</th>
                  <th className="border border-gray-300 px-4 py-2 text-left text-gray-700">Address</th>
                  <th className="border border-gray-300 px-4 py-2 text-left text-gray-700">Date</th>
                  <th className="border border-gray-300 px-4 py-2 text-left text-gray-700">Status</th>
                  <th className="border border-gray-300 px-4 py-2 text-left text-gray-700">Actions</th>
                </tr>
              </thead>
              <tbody>
              {bookings.map((booking, index) => (
                    <tr key={index}>
                      <td>{booking.username}</td>
                      <td>{booking.phone}</td>
                      <td>{booking.address}</td>
                      <td>{booking.appointmentDate}</td>
                      <td>
                        <span className={`px-3 py-1 rounded-lg text-white ${booking.status === "Completed" ? "bg-green-500" : "bg-yellow-500"}`}>
                          {booking.status}
                        </span>
                      </td>
                      <td>
                        {booking.status === "Pending" && (
                          <button
                            onClick={() => {
                              console.log("Booking Data:", booking); // Log entire object
                              console.log("Booking ID:", booking.id); // Log ID
                              handleUpdateStatus("2");
                            }}
                            className="bg-blue-500 text-white px-3 py-1 rounded-lg hover:bg-blue-600 transition duration-300"
                          >
                            Mark as Completed
                          </button>
                        )}
                      </td>
                    </tr>
                  ))}


              </tbody>
            </table>
          </div>
        ) : (
          <p className="text-gray-600 text-center">No appointments available.</p>
        )}
      </div>
    </div>
  );
}

export default LabProfile;
