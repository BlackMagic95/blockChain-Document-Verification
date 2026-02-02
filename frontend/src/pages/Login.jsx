import { useState } from "react";
import axios from "axios";
import "./Login.css";
import { useNavigate, Link } from "react-router-dom";
const API = "http://localhost:8080";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [msg, setMsg] = useState("");
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const handleLogin = async () => {
    if (!email || !password) {
      setMsg("Enter email and password");
      return;
    }

    try {
      setLoading(true);
      setMsg("");

      const res = await axios.post(`${API}/auth/login`, {
        email,
        password,
      });

      localStorage.setItem("token", res.data.token);
      localStorage.setItem("role", res.data.role);

      // 🔥 role-based redirect
      if (res.data.role === "ROLE_ADMIN") {
        navigate("/admin");
      } else {
        navigate("/verify");
      }

    } catch (err) {
      setMsg("Invalid credentials ❌");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-card">

        <h1>Blockchain Document Verification</h1>
        <p className="subtitle">
          Secure · Immutable · Trusted
        </p>

        <input
          type="email"
          placeholder="Admin email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <button onClick={handleLogin} disabled={loading}>
          {loading ? "Signing in..." : "Admin Login"}
        </button>

        {msg && <div className="error">{msg}</div>}

        {/* 👇 PUBLIC VERIFY LINK */}
        <div className="public-link">
          <span>or</span>
          <Link to="/verify">Verify a document without login</Link>
        </div>

      </div>
    </div>
  );
}
