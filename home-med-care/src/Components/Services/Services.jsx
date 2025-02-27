import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const Services = () => {
  const [labs, setLabs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [search, setSearch] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchLabs = async () => {
      setLoading(true);
      setError("");

      const token = localStorage.getItem("token");

      try {
        const response = await fetch("http://localhost:8080/labs/show", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.status === 401) {
          throw new Error("Unauthorized access. Please log in.");
        }
        if (!response.ok) {
          throw new Error("Failed to fetch labs");
        }

        const data = await response.json();
        setLabs(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchLabs();
  }, []);

  const handleBookNow = (lab) => {
    navigate("/book", { state: { labId: lab.id } });
};


  // Filter labs based on search input
  const filteredLabs = labs.filter((lab) =>
    lab.labName.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gray-100 p-5">
      {/* Search Input */}
      <input
        type="text"
        placeholder="Search labs..."
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        className="w-full p-2 mb-4 border border-gray-300 rounded-lg"
      />

      {/* Grid of Labs */}
      <div className="grid sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        {filteredLabs.map((lab) => (
          <div key={lab.id} className="bg-white rounded-lg shadow-lg p-6 transition-transform transform hover:scale-105">
            <h3 className="text-2xl font-bold text-center text-green-600 mb-4">
              {lab.labName}
            </h3>
            <p className="text-gray-600 mb-2"><strong>Email:</strong> {lab.email}</p>
            <p className="text-gray-600 mb-2"><strong>Phone:</strong> {lab.phone}</p>
            <p className="text-gray-600 mb-2"><strong>Location:</strong> {lab.address}</p>
            <p className="text-gray-600 mb-4"><strong>Tests:</strong> {lab.services}</p>
            <button
              onClick={() => handleBookNow(lab)}
              className="w-full bg-green-600 text-white py-2 rounded-lg hover:bg-green-700 transition"
            >
              Book Now
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Services;
