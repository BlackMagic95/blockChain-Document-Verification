import { useState, useEffect } from "react";
import api from "./api/axios";          // 🔐 JWT-enabled axios
import { getRole, logout } from "./utils/auth";
import "./App.css";

export default function App() {
  const [mode, setMode] = useState("verify"); // register | verify
  const [file, setFile] = useState(null);
  const [msg, setMsg] = useState("");
  const [resultType, setResultType] = useState("info");
  const [docs, setDocs] = useState([]);

  const role = getRole(); // ADMIN / USER

  // ================= LOAD DOCS =================
  useEffect(() => {
    loadDocs();
  }, []);

  const loadDocs = async () => {
    try {
      const res = await api.get("/docs");
      setDocs(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      console.error("Failed to load docs", err);
    }
  };

  // ================= REGISTER (ADMIN ONLY) =================
  const handleRegister = async () => {
    if (!file) {
      setMsg("Please select a file");
      setResultType("info");
      return;
    }

    try {
      setMsg("Registering document on blockchain...");
      setResultType("info");

      const f = new FormData();
      f.append("file", file);

      await api.post("/upload", f); // 🔐 JWT auto-attached

      setMsg("Document registered successfully ✅");
      setResultType("success");
      setFile(null);
      await loadDocs();

    } catch (err) {
      console.error(err);
      setMsg("Not authorized or failed ❌");
      setResultType("error");
    }
  };

  // ================= VERIFY (PUBLIC) =================
  const handleVerify = async () => {
    if (!file) {
      setMsg("Please upload a file");
      setResultType("info");
      return;
    }

    try {
      setMsg("Verifying document...");
      setResultType("info");

      const f = new FormData();
      f.append("file", file);

      const res = await api.post("/verify", f);

      if (res.data.status === "VERIFIED") {
        setMsg(`VERIFIED ✅ (at ${res.data.verifiedAt})`);
        setResultType("success");
      } else if (res.data.status === "NOT_REGISTERED") {
        setMsg("NOT REGISTERED ❌");
        setResultType("error");
      } else if (res.data.status === "CHAIN_MISSING") {
        setMsg("BLOCKCHAIN RECORD MISSING ⚠️");
        setResultType("error");
      } else {
        setMsg("Verification failed ❌");
        setResultType("error");
      }

      setFile(null);

    } catch (err) {
      console.error(err);
      setMsg("Verification failed ❌");
      setResultType("error");
    }
  };

  // ================= UI =================
  return (
    <div className="page">

      {/* HEADER */}
      <div className="header">
        <div>
          <span className="status">🟢 Node active · Sepolia</span>
          <h1>Document Verification</h1>
          <p className="subtitle">
            Blockchain-backed document registry using SHA-256
          </p>
          <p><b>Role:</b> {role}</p>
        </div>

        <div className="toggle">
          {role === "ADMIN" && (
            <button
              className={mode === "register" ? "active" : ""}
              onClick={() => {
                setMode("register");
                setMsg("");
                setFile(null);
              }}
            >
              📄 Register
            </button>
          )}

          <button
            className={mode === "verify" ? "active" : ""}
            onClick={() => {
              setMode("verify");
              setMsg("");
              setFile(null);
            }}
          >
            ✅ Verify
          </button>

          <button onClick={() => {
            logout();
            window.location.reload();
          }}>
            🚪 Logout
          </button>
        </div>
      </div>

      {/* MAIN GRID */}
      <div className="main">

        {/* LEFT CARD */}
        <div className="card big">
          <h2>
            {mode === "register"
              ? "Register a document"
              : "Verify a document"}
          </h2>

          <div className="dropzone">
            <div className="fileIcon">📄</div>
            <p>{file ? file.name : "Drop a file here or click to browse"}</p>
            <input
              type="file"
              onChange={e => setFile(e.target.files[0])}
            />
          </div>

          <button
            className="primary full"
            onClick={mode === "register" ? handleRegister : handleVerify}
          >
            {mode === "register" ? "Register Document" : "Verify Document"}
          </button>

          {msg && (
            <div className={`result ${resultType}`}>
              {msg}
            </div>
          )}
        </div>

        {/* RIGHT CARD */}
        <div className="card side">
          <h2>Chain overview</h2>

          <div className="stats">
            <div className="stat">
              <span>{docs.length}</span>
              Documents registered
            </div>
            <div className="stat">
              <span>{docs.length + 1}</span>
              Total blocks
            </div>
          </div>

          <div className="valid">
            Blockchain is valid ✅
          </div>

          <div className="explorer">
            <h3>Recent Documents</h3>
            {docs.slice(-3).reverse().map(d => (
              <div key={d.id} className="block">
                <strong>{d.name}</strong>
                <div className="hash">{d.hash}</div>
              </div>
            ))}
          </div>
        </div>

      </div>
    </div>
  );
}
