import { useEffect, useState } from "react";
import api from "../api";
import "./AdminPage.css";
import toast from "react-hot-toast";

export default function AdminPage() {
  const [file, setFile] = useState(null);
  const [docs, setDocs] = useState([]);
  const [msg, setMsg] = useState("");

  useEffect(() => {
    loadDocs();
  }, []);

  const loadDocs = async () => {
    const res = await api.get("/docs");
    setDocs(res.data || []);
  };

  const handleUpload = async () => {
    if (!file) {
      setMsg("Please select a file");
      return;
    }

    try {
      toast.loading("Registering document...");
      const form = new FormData();
      form.append("file", file);

      await api.post("/upload", form);

      toast.dismiss();
toast.success("Document registered successfully ✅");

      setFile(null);
      loadDocs();
    } catch {
      toast.dismiss();
toast.error("Upload failed ❌");

    }
  };

  return (
    <div className="admin-page">

      {/* HEADER */}
      <div className="admin-header">
        <div>
          <h1>Admin Dashboard</h1>
          <p>Authorized document registration</p>
        </div>
      </div>


      {/* UPLOAD CARD */}
      <div className="upload-card">
        <div className="upload-box">

          <label className="upload-plus">
            +
            <input
              type="file"
              hidden
              onChange={(e) => setFile(e.target.files[0])}
            />
          </label>

          <div className="upload-info">
            <h3>{file ? file.name : "Add document"}</h3>
            <p>SHA-256 hash will be stored on blockchain</p>
          </div>

          <button onClick={handleUpload}>
            Register
          </button>
        </div>

        {msg && <div className="status">{msg}</div>}
      </div>


      {/* DOCUMENTS */}
      <div className="docs-section">
        <h2>Registered Documents</h2>

        <div className="docs-table">

          <div className="docs-head">
            <span>Name</span>
            <span>Hash</span>
            <span>Date (IST)</span>
          </div>

          {docs.map((d) => (
            <div key={d.id} className="docs-row">

              <span>{d.name}</span>

              <span className="hash">
                {d.hash.slice(0, 14)}...
              </span>

              {/* ✅ DIRECT STRING (no parsing) */}
              <span>{d.createdAt || "—"}</span>

            </div>
          ))}

        </div>
      </div>

    </div>
  );
}
