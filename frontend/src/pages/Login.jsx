import { GoogleLogin } from "@react-oauth/google";
import axios from "axios";
import { useState, useEffect } from "react";
import "./Login.css";
import toast from "react-hot-toast";

const API = "https://blockchain-document-verification.onrender.com";

export default function Login() {
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const [stats, setStats] = useState({
    totalDocs: 0,
    totalVerifications: 0
  });

  const [display, setDisplay] = useState({
    totalDocs: 0,
    totalVerifications: 0
  });

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

  const onSuccess = async (cred) => {
    try {
      setLoading(true);

      const res = await axios.post(`${API}/auth/google`, {
        token: cred.credential
      });

      if (res.data.status === "PENDING_APPROVAL") {
        localStorage.setItem("pendingGoogleToken", cred.credential);
        localStorage.setItem("pendingEmail", res.data.email || "");
        toast("Please submit your college access request");
        window.location.href = "/college-request";
        return;
      }

      localStorage.setItem("token", res.data.token);
      localStorage.setItem("role", res.data.role);

      if (res.data.role === "SUPER_ADMIN") {
        toast.success("Welcome Super Admin");
        window.location.href = "/super-admin";
      } else {
        toast.success("Welcome Admin");
        window.location.href = "/admin";
      }
    } catch {
      toast.error("Not authorized");
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="orb-decoration orb-1"></div>
      <div className="orb-decoration orb-2"></div>

      <div className="login-card">

        <h1 className="title">
          🔐 Blockchain Document Verification
        </h1>

        <div className="status-badge">
          System Online
        </div>

        <p className="subtitle">
          Secure tamper-proof academic document verification using blockchain
        </p>

        {/* Simple login instruction */}
        <p className="login-instruction">
          Already registered? Login with Google. New institution? Request access.
        </p>

        {loading ? (
          <div className="loader"></div>
        ) : (
          <div style={{ display: "flex", justifyContent: "center", marginTop: "20px" }}>
            <GoogleLogin
              onSuccess={onSuccess}
              onError={() => toast.error("Google login failed")}
              theme="filled_black"
              size="large"
              shape="pill"
              width="320"
            />
          </div>
        )}

        {error && <p className="error-text">{error}</p>}

        <div
          className="verify-link"
          onClick={() => (window.location.href = "/verify")}
        >
          Verify a document →
        </div>
      </div>

      <div className="stats-grid">
        <div className="stat-card glow-blue">
          <div className="stat-icon">📄</div>
          <h2>{display.totalDocs.toLocaleString()}</h2>
          <p>Registered Docs</p>
        </div>

        <div className="stat-card glow-green">
          <div className="stat-icon">✅</div>
          <h2>{display.totalVerifications.toLocaleString()}</h2>
          <p>Total Verifications</p>
        </div>

        <div className="stat-card glow-purple">
          <div className="stat-icon">⛓️</div>
          <h2>{display.totalDocs.toLocaleString()}</h2>
          <p>Blockchain Hashes</p>
        </div>
      </div>
    </div>
  );
}
