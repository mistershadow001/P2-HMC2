import { useState } from "react";

function RegisterLab() {
  const [formData, setFormData] = useState({
    labName: "",
    ownerName: "",
    phone: "",
    email: "",
    address: "",
    services: [],
    password: "",
    paymentId: "",
  });

  const [errors, setErrors] = useState({});
  const [newService, setNewService] = useState("");

  const validate = () => {
    let newErrors = {};
    if (!formData.labName) newErrors.labName = "Lab Name is required";
    if (!formData.ownerName) newErrors.ownerName = "Owner Name is required";
    if (!formData.phone.match(/^\d{10}$/)) newErrors.phone = "Enter a valid 10-digit phone number";
    if (!formData.email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) newErrors.email = "Enter a valid email";
    if (!formData.address) newErrors.address = "Address is required";
    if (formData.services.length < 1) newErrors.services = "At least one service is required";
    if (formData.password.length < 6) newErrors.password = "Password must be at least 6 characters";

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleAddService = () => {
    if (newService.trim()) {
      setFormData((prev) => ({
        ...prev,
        services: [...prev.services, newService.trim()],
      }));
      setNewService("");
    }
  };

  const handleRemoveService = (index) => {
    setFormData((prev) => ({
      ...prev,
      services: prev.services.filter((_, i) => i !== index),
    }));
  };

  const handlePayment = async () => {
    if (!validate()) return;
    const script = document.createElement("script");
    script.src = "https://checkout.razorpay.com/v1/checkout.js";
    script.async = true;
    document.body.appendChild(script);

    script.onload = () => {
      const options = {
        key: "rzp_test_5sMOmi0UPxB3yR",
        amount: 50000,
        currency: "INR",
        name: "Home-Med-Care",
        description: "Lab Registration - 28 Days Subscription",
        handler: function (response) {
          alert(`Payment Successful! Payment ID: ${response.razorpay_payment_id}`);
          setFormData((prev) => ({ ...prev, paymentId: response.razorpay_payment_id }));
        },
        prefill: {
          name: formData.ownerName,
          email: formData.email,
          contact: formData.phone,
        },
        theme: {
          color: "#4F46E5",
        },
      };
      const rzp = new window.Razorpay(options);
      rzp.open();
    };
  };

  const registerLab = () => {
    if (!formData.paymentId) {
      alert("Complete the payment first!");
      return;
    }

    fetch("http://localhost:8080/labs/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(formData),
    })
      .then((response) => {
        if (response.ok) {
          alert("Lab Registered Successfully!");
          window.location.href = "/login";
        } else {
          alert("Registration failed. Try again!");
        }
      })
      .catch(() => alert("Error registering the lab."));
  };

  return (
    <div className="max-w-sm mx-auto mt-8 p-6 bg-white shadow-md rounded-lg border border-gray-200">
      <h2 className="text-xl font-semibold text-center text-gray-800 mb-4">Lab Registration</h2>
      <form className="space-y-4">
        {["labName", "ownerName", "phone", "email", "address", "password"].map((key) => (
          <div key={key}>
            <label className="block text-gray-600 text-sm font-medium capitalize">{key.replace(/([A-Z])/g, ' $1')}</label>
            <input type={key === "email" ? "email" : key === "password" ? "password" : "text"} 
              name={key} value={formData[key]} onChange={handleChange} 
              className="w-full mt-1 p-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none transition" required />
            {errors[key] && <p className="text-red-500 text-xs">{errors[key]}</p>}
          </div>
        ))}

        {/* Services Section */}
        <div>
          <label className="block text-gray-600 text-sm font-medium">Services</label>
          <div className="flex">
            <input type="text" value={newService} onChange={(e) => setNewService(e.target.value)}
              className="w-full mt-1 p-2 border rounded-md focus:ring-2 focus:ring-blue-500 outline-none transition" 
              placeholder="Enter a service" />
            <button type="button" onClick={handleAddService} className="ml-2 bg-green-500 text-white px-3 py-2 rounded-md">Add</button>
          </div>
          {errors.services && <p className="text-red-500 text-xs">{errors.services}</p>}
          <ul className="mt-2">
            {formData.services.map((service, index) => (
              <li key={index} className="flex justify-between items-center bg-gray-100 p-2 rounded-md mt-1">
                {service}
                <button type="button" onClick={() => handleRemoveService(index)} className="text-red-600 font-bold">X</button>
              </li>
            ))}
          </ul>
        </div>

        <button type="button" onClick={handlePayment} disabled={Object.keys(errors).length > 0}
          className="w-full bg-green-600 text-white py-2 rounded-md text-base font-medium hover:bg-blue-700 transition disabled:opacity-50">
          Pay & Register
        </button>
        <button type="button" onClick={registerLab} disabled={!formData.paymentId}
          className="w-full bg-blue-600 text-white py-2 rounded-md text-base font-medium hover:bg-blue-700 transition disabled:opacity-50 mt-2">
          Register Lab
        </button>
      </form>
    </div>
  );
}

export default RegisterLab;
