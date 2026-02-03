import { GoogleLogin } from "@react-oauth/google";
import axios from "axios";
import { useState } from "react";
import "./Login.css";

const API = "http://localhost:8080";

export default function Login() {
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const onSuccess = async (cred) => {
    try {
      setLoading(true);
      setError("");

      const res = await axios.post(`${API}/auth/google`, {
        token: cred.credential
      });

      localStorage.setItem("token", res.data.token);
      localStorage.setItem("role", res.data.role);

      window.location.href = "/admin";

    } catch {
      setError("You are not authorized as admin");
      setLoading(false);
    }
  };

  return (
    <div className="login-container">

      <div className="login-card">

        <h1 className="title">🔐 Admin Portal</h1>
        <p className="subtitle">Secure access for authorized users only</p>

        {loading ? (
          <div className="loader"></div>
        ) : (
          <GoogleLogin
            onSuccess={onSuccess}
            onError={() => setError("Google login failed")}
          />
        )}

        {error && <p className="error-text">{error}</p>}

        <p
          className="verify-link"
          onClick={() => (window.location.href = "/verify")}
        >
          Verify a document →
        </p>

      </div>
    </div>
  );
}
