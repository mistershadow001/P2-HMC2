import { useState } from "react";

function UserLogin() {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });

  const [errors, setErrors] = useState({});
  const [message, setMessage] = useState("");

  // ✅ Validate Inputs
  const validate = () => {
    let newErrors = {};
    if (!formData.username) newErrors.username = "Username is required";
    if (!formData.password) newErrors.password = "Password is required";

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // ✅ Handle Input Changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  // ✅ Handle Login Request
  const handleLogin = () => {
    if (!validate()) return;

    fetch("http://localhost:8080/users/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(formData),
    })
      .then(async (response) => {
        if (!response.ok) {
          throw new Error("Invalid Username or Password!");
        }
        return response.json();
      })
      .then((data) => {
        if (data.token) {
          localStorage.setItem("token", data.token); 
          localStorage.setItem("role", "USER");// ✅ Store JWT token
          alert("Login Successful!");
          window.location.href = "/"; // ✅ Redirect to Profile Page
        } else {
          setMessage("Unexpected response from server.");
        }
      })
      .catch((error) => setMessage(error.message || "Error logging in. Try again!"));
  };

  return (
    <div className="max-w-sm mx-auto mt-8 p-6 bg-white shadow-md rounded-lg border border-gray-200">
      <h2 className="text-xl font-semibold text-center text-gray-800 mb-4">User Login</h2>
      <form className="space-y-4">
        {/* Username Input */}
        <div>
          <label className="block text-gray-600 text-sm font-medium">Username</label>
          <input
            type="text"
            name="username"
            value={formData.username}
            onChange={handleChange}
            className="w-full mt-1 p-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none transition"
            required
          />
          {errors.username && <p className="text-red-500 text-xs">{errors.username}</p>}
        </div>

        {/* Password Input */}
        <div>
          <label className="block text-gray-600 text-sm font-medium">Password</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            className="w-full mt-1 p-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none transition"
            required
          />
          {errors.password && <p className="text-red-500 text-xs">{errors.password}</p>}
        </div>

        {/* Error Message */}
        {message && <p className="text-red-500 text-xs text-center">{message}</p>}

        {/* Login Button */}
        <button
          type="button"
          onClick={handleLogin}
          className="w-full bg-blue-600 text-white py-2 rounded-md text-base font-medium hover:bg-blue-700 transition"
        >
          Login
        </button>
      </form>
    </div>
  );
}

export default UserLogin;
