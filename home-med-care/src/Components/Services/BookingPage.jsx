import { useState, useEffect } from "react";
import { useParams, useLocation } from "react-router-dom";

const BookingPage = () => {
  const [userId, setUserId] = useState(null);
  const [date, setDate] = useState("");
  const [services, setServices] = useState([]);
  const [selectedService, setSelectedService] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  const location = useLocation();
  const { labId: paramLabId } = useParams();
  const labId = location.state?.labId || paramLabId;

  console.log("Lab ID:", labId);

  // Debugging: Log date changes
  useEffect(() => {
    console.log("Date state updated:", date);
  }, [date]);

  useEffect(() => {
    const fetchUserId = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        setError("No authentication token found. Please log in.");
        return;
      }

      try {
        const response = await fetch("http://localhost:8080/users/user", {
          method: "GET",
          headers: { Authorization: `Bearer ${token}` },
        });

        const data = await response.json();
        if (response.ok) {
          setUserId(data.userId);
          console.log("User ID fetched:", data.userId);
        } else {
          throw new Error(data.error || "Failed to fetch user ID");
        }
      } catch (err) {
        setError(err.message);
      }
    };

    fetchUserId();
  }, []);

  useEffect(() => {
    if (!labId) {
      console.error("Lab ID is missing, cannot fetch lab details!");
      setError("Invalid lab selection. Please try again.");
      return;
    }

    const fetchLabDetails = async () => {
      try {
        console.log("Fetching lab details for:", labId);
        const token = localStorage.getItem("token");

        if (!token) {
          throw new Error("No authentication token found.");
        }

        const response = await fetch(`http://localhost:8080/labs/lab/${labId}`, {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });

        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();
        console.log("Fetched Lab Data:", data);

        setServices(data.services || []);
      } catch (err) {
        console.error("Error fetching lab services:", err);
        setError("Failed to fetch lab services");
      }
    };

    fetchLabDetails();
  }, [labId]);

  const handleBooking = async () => {
    if (!userId) {
      setError("User ID not found. Please log in again.");
      return;
    }

    if (!date || !selectedService) {
      setError("Please select a date and service.");
      return;
    }

    setLoading(true);
    setError("");
    setMessage("");

    const token = localStorage.getItem("token");

    // Ensure date is in correct format
    const formattedDate = new Date(date).toISOString().split("T")[0]; // Convert to YYYY-MM-DD

    console.log("Booking Request Payload:", {
      userId,
      labId,
      appointmentDate: formattedDate, // ✅ Fix: Correct field name
      services: selectedService,
    });

    try {
      const response = await fetch("http://localhost:8080/bookings/create", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          userId,
          labId,
          appointmentDate: formattedDate, // ✅ Fix: Correct field name
          services: selectedService,
        }),
      });

      const data = await response.json();
      if (response.ok) {
        setMessage("Booking successful!");
      } else {
        throw new Error(data.error || "Booking failed");
      }
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100 p-5">
      <h2 className="text-3xl font-bold text-green-600 mb-6">Book Your Test</h2>

      {error && <p className="text-red-600">{error}</p>}
      {message && <p className="text-green-600">{message}</p>}

      <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md">
        <form onSubmit={(e) => e.preventDefault()}>
          <label className="block text-gray-700 mb-2">Select Date:</label>
          <input
            type="date"
            value={date}
            onChange={(e) => {
              console.log("Selected Date:", e.target.value); // Debugging
              setDate(e.target.value);
            }}
            className="w-full p-2 border rounded mb-4"
          />

          <label className="block text-gray-700 mb-2">Select Service:</label>
          <select
            value={selectedService}
            onChange={(e) => setSelectedService(e.target.value)}
            className="w-full p-2 border rounded mb-4"
          >
            <option value="">-- Select --</option>
            {services.map((service, index) => (
              <option key={index} value={service}>
                {service}
              </option>
            ))}
          </select>

          <button
            onClick={handleBooking}
            className="w-full bg-green-600 text-white py-2 rounded-lg hover:bg-green-700 transition"
            disabled={loading}
          >
            {loading ? "Booking..." : "Confirm Booking"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default BookingPage;
