import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import toast from "react-hot-toast";
import "./Login.css";

const API = "https://blockchain-document-verification.onrender.com"; // Update with your backend API URL

export default function CollegeRequestPage() {
  const navigate = useNavigate();
  const location = useLocation();

  const [form, setForm] = useState({
    collegeName: "",
    contactName: "",
    contactPhone: ""
  });
  const [loading, setLoading] = useState(false);

  const googleToken = location.state?.googleToken || localStorage.getItem("pendingGoogleToken");
  const email = location.state?.email || localStorage.getItem("pendingEmail");

  const onChange = (e) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const submit = async (e) => {
    e.preventDefault();

    if (!googleToken) {
      toast.error("Please login with Google first");
      navigate("/");
      return;
    }

    try {
      setLoading(true);
      await axios.post(`${API}/access/request`, {
        token: googleToken,
        ...form
      });

      toast.success("Request submitted. Wait for super admin approval.");
      localStorage.removeItem("pendingGoogleToken");
      localStorage.removeItem("pendingEmail");
      navigate("/");
    } catch {
      toast.error("Could not submit request");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container" style={{ minHeight: "100vh" }}>
      <div className="login-card" style={{ maxWidth: 560 }}>
        <h2 className="title">🏫 College Access Request</h2>
        <p className="subtitle">Submit this form to request upload access.</p>
        {email && <p className="subtitle"><b>Email:</b> {email}</p>}

        <form onSubmit={submit} style={{ display: "grid", gap: 12, marginTop: 16 }}>
          <input
            name="collegeName"
            placeholder="College Name"
            value={form.collegeName}
            onChange={onChange}
            required
            style={{ padding: 12, borderRadius: 10, border: "1px solid #334155", background: "#0f172a", color: "#fff" }}
          />
          <input
            name="contactName"
            placeholder="Contact Person Name"
            value={form.contactName}
            onChange={onChange}
            required
            style={{ padding: 12, borderRadius: 10, border: "1px solid #334155", background: "#0f172a", color: "#fff" }}
          />
          <input
            name="contactPhone"
            placeholder="Contact Phone"
            value={form.contactPhone}
            onChange={onChange}
            style={{ padding: 12, borderRadius: 10, border: "1px solid #334155", background: "#0f172a", color: "#fff" }}
          />

          <button
            type="submit"
            disabled={loading}
            className="register-btn"
            style={{ width: "100%" }}
          >
            {loading ? "Submitting..." : "Submit Request"}
          </button>
        </form>
      </div>
    </div>
  );
}