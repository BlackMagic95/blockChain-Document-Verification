import { GoogleLogin } from "@react-oauth/google";
import axios from "axios";
import { useState, useEffect } from "react";
import "./Login.css";
import toast from "react-hot-toast";

const API = "http://localhost:8080";

export default function Login() {
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [dark, setDark] = useState(true);

  const [stats, setStats] = useState({
    totalDocs: 0,
    totalVerifications: 0
  });

  const [display, setDisplay] = useState({
    totalDocs: 0,
    totalVerifications: 0
  });

  /* ================= FETCH STATS ================= */
  const fetchStats = async () => {
    try {
      const res = await axios.get(`${API}/stats`);
      setStats(res.data);
    } catch {}
  };

  useEffect(() => {
    fetchStats();
    const i = setInterval(fetchStats, 5000);
    return () => clearInterval(i);
  }, []);

  /* ================= ANIMATE COUNTERS ================= */
  useEffect(() => {
    const animate = (key) => {
      let start = 0;
      const end = stats[key];
      const step = Math.ceil(end / 20);

      const interval = setInterval(() => {
        start += step;
        if (start >= end) {
          start = end;
          clearInterval(interval);
        }
        setDisplay((p) => ({ ...p, [key]: start }));
      }, 20);
    };

    animate("totalDocs");
    animate("totalVerifications");
  }, [stats]);

  /* ================= LOGIN ================= */
  const onSuccess = async (cred) => {
    try {
      setLoading(true);

      const res = await axios.post(`${API}/auth/google`, {
        token: cred.credential
      });

      localStorage.setItem("token", res.data.token);
      localStorage.setItem("role", res.data.role);

      window.location.href = "/admin";
    } catch {
      toast.error("Not authorized as admin");
      setLoading(false);
    }
  };

  return (
    <div className={`login-container ${dark ? "dark" : "light"}`}>


      {/* ================= LOGIN CARD ================= */}
      <div className="login-card">

        <h1 className="title">🔐 Admin Portal</h1>

        <div className="status-badge">
          🟢 System Online
        </div>

        <p className="subtitle">
          Secure blockchain document verification
        </p>

        {loading ? (
          <div className="loader"></div>
        ) : (
          <GoogleLogin
            onSuccess={onSuccess}
            onError={() => toast.error("Google login failed")}
          />
        )}

        {error && <p className="error-text">{error}</p>}

        <div
          className="verify-link"
          onClick={() => (window.location.href = "/verify")}
        >
          Verify a document →
        </div>
      </div>

      {/* ================= STATS ================= */}
      <div className="stats-grid">

        <div className="stat-card glow-blue">
          <div className="stat-icon">📄</div>
          <h2>{display.totalDocs}</h2>
          <p>Registered Docs</p>
        </div>

        <div className="stat-card glow-green">
          <div className="stat-icon">✅</div>
          <h2>{display.totalVerifications}</h2>
          <p>Total Verifications</p>
        </div>

        <div className="stat-card glow-purple">
          <div className="stat-icon">⛓</div>
          <h2>{display.totalDocs}</h2>
          <p>Blockchain Hashes</p>
        </div>

      </div>

    </div>
  );
}
