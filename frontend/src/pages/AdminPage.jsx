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
const handleLogout = () => {
  localStorage.clear();
  toast.success("Logged out successfully 👋");
  setTimeout(() => {
    window.location.href = "/";
  }, 800);
};

  const handleUpload = async () => {
    if (!file) {
      setMsg("Please select a file");
      return;
    }
    const allowedTypes = [
    "application/pdf",
    "image/jpeg",
    "image/png",
    "application/msword",
    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
  ];

  const maxSize = 5 * 1024 * 1024; // 5MB

  if (!allowedTypes.includes(file.type)) {
    toast.error("Only PDF / Image / DOC allowed");
    return;
  }

  if (file.size > maxSize) {
    toast.error("Max file size is 5MB");
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
  /* ================= EXPORT CSV ================= */
const exportCSV = () => {
  if (!docs.length) {
    toast.error("No documents to export");
    return;
  }

  const headers = ["Name", "Hash", "Registered At (IST)"];

  const rows = docs.map((d) => [
    d.name,
    d.hash,
    d.createdAt || "—"
  ]);

  const csv =
    [headers, ...rows]
      .map((r) => r.join(","))
      .join("\n");

  const blob = new Blob([csv], {
    type: "text/csv;charset=utf-8;"
  });

  const link = document.createElement("a");
  link.href = URL.createObjectURL(blob);
  link.download = "documents.csv";
  link.click();

  toast.success("CSV downloaded 📥");
};

  return (
    <div className="admin-page">

      {/* HEADER */}
      <div className="admin-header">
  <div>
    <h1>Admin Dashboard</h1>
    <p>Authorized document registration</p>
  </div>

  <button className="logout-btn" onClick={handleLogout}>
    Logout
  </button>
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

          <div className="upload-actions">
  <button className="register-btn" onClick={handleUpload}>
    Register
  </button>

  <button className="export-btn" onClick={exportCSV}>
     Export CSV ⬇
  </button>
</div>

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
