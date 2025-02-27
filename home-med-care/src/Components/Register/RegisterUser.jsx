import { useState } from "react";

function RegisterUser() {
  const [formData, setFormData] = useState({
    username: "",
    email: "",
    password: "",
    phone: "",
    address: "",
  });

  const [errors, setErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState("");

  // ✅ Validate Inputs
  const validate = () => {
    let newErrors = {};
    if (!formData.username.trim()) newErrors.username = "Username is required";
    if (!formData.email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) newErrors.email = "Enter a valid email";
    if (formData.password.length < 6) newErrors.password = "Password must be at least 6 characters";
    if (!formData.phone.match(/^\d{10}$/)) newErrors.phone = "Enter a valid 10-digit phone number";
    if (!formData.address.trim()) newErrors.address = "Address is required";

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // ✅ Handle Input Changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  // ✅ Register User
  const registerUser = () => {
    if (!validate()) return;

    fetch("http://localhost:8080/users/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(formData),
    })
      .then((response) => {
        if (response.ok) {
          setSuccessMessage("User Registered Successfully!");
          setFormData({ username: "", email: "", password: "", phone: "", address: "" });
          window.location.href = "/Userlogin";
        } else {
          setSuccessMessage("Registration failed. Try again!");
        }
      })
      .catch(() => setSuccessMessage("Error registering the user."));
  };

  return (
    <div className="max-w-sm mx-auto mt-8 p-6 bg-white shadow-md rounded-lg border border-gray-200">
      <h2 className="text-xl font-semibold text-center text-gray-800 mb-4">User Registration</h2>

      <form className="space-y-4">
        {Object.entries(formData).map(([key, value]) => (
          <div key={key}>
            <label className="block text-gray-600 text-sm font-medium capitalize">
              {key}
            </label>
            <input
              type={key === "email" ? "email" : key === "password" ? "password" : key === "phone" ? "tel" : "text"}
              name={key}
              value={value}
              onChange={handleChange}
              className="w-full mt-1 p-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none transition"
              required
            />
            {errors[key] && <p className="text-red-500 text-xs">{errors[key]}</p>}
          </div>
        ))}

        <button
          type="button"
          onClick={registerUser}
          className="w-full bg-blue-600 text-white py-2 rounded-md text-base font-medium hover:bg-blue-700 transition"
        >
          Register
        </button>
      </form>

      {successMessage && <p className="mt-4 text-center text-green-600">{successMessage}</p>}
    </div>
  );
}

export default RegisterUser;
