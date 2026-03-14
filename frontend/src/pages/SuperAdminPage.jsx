import { useEffect, useState } from "react";
import api from "../api";
import toast from "react-hot-toast";
import "./AdminPage.css";

export default function SuperAdminPage() {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(false);

  const loadRequests = async () => {
    try {
      const res = await api.get("/access/requests");
      setRequests(res.data || []);
    } catch {
      toast.error("Could not load requests");
    }
  };

  useEffect(() => {
    loadRequests();
  }, []);

  const updateRequest = async (id, action) => {
    try {
      setLoading(true);
      await api.post(`/access/requests/${id}/${action}`);
      toast.success(`Request ${action}d`);
      loadRequests();
    } catch {
      toast.error(`Failed to ${action}`);
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    localStorage.clear();
    window.location.href = "/";
  };

  return (
    <div className="admin-page">
      <div className="admin-header">
        <div>
          <h1>Super Admin Dashboard</h1>
          <p>Approve or reject college onboarding requests</p>
        </div>
        <button className="logout-btn" onClick={logout}>Logout</button>
      </div>

      <div className="docs-section">
        <h2>Pending Access Requests</h2>
        <div className="docs-table">
          <div className="docs-head" style={{ gridTemplateColumns: "1.2fr 1fr 1fr 1fr 1fr" }}>
            <span>Email</span>
            <span>College</span>
            <span>Contact</span>
            <span>Requested At</span>
            <span>Actions</span>
          </div>

          {requests.length === 0 && (
            <div className="docs-row" style={{ gridTemplateColumns: "1fr" }}>
              <span>No pending requests.</span>
            </div>
          )}

          {requests.map((r) => (
            <div key={r.id} className="docs-row" style={{ gridTemplateColumns: "1.2fr 1fr 1fr 1fr 1fr" }}>
              <span>{r.email}</span>
              <span>{r.collegeName}</span>
              <span>{r.contactName} {r.contactPhone ? `(${r.contactPhone})` : ""}</span>
              <span>{r.requestedAt}</span>
              <span style={{ display: "flex", gap: 8 }}>
                <button className="register-btn" disabled={loading} onClick={() => updateRequest(r.id, "approve")}>Approve</button>
                <button className="export-btn" disabled={loading} onClick={() => updateRequest(r.id, "reject")}>Reject</button>
              </span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}