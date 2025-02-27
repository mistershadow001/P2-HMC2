import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";

function ProfileRedirect() {
  const navigate = useNavigate();

  useEffect(() => {
    const role = localStorage.getItem("role");

    if (role === "USER") {
      navigate("/user-profile");
    } else if (role === "LAB") {
      navigate("/lab-profile");
    } else {
      navigate("/choose-login"); // Default to choosing login
    }
  }, [navigate]);

  return <p className="text-center">Redirecting...</p>;
}

export default ProfileRedirect;
