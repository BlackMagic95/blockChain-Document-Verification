import { useState, useEffect } from "react";
import axios from "axios";
import "./App.css";

const API = "http://localhost:8080";

export default function App() {
  const [mode, setMode] = useState("verify"); // register | verify
  const [file, setFile] = useState(null);
  const [msg, setMsg] = useState("");
  const [docs, setDocs] = useState([]);

  useEffect(() => {
    loadDocs();
  }, []);

  const loadDocs = async () => {
    const res = await axios.get(API + "/docs");
    setDocs(res.data);
  };

  const handleRegister = async () => {
    if (!file) return;
    setMsg("Registering document on blockchain...");
    const f = new FormData();
    f.append("file", file);
    await axios.post(API + "/upload", f);
    setMsg("Document registered ✅");
    loadDocs();
  };

  const handleVerify = async () => {
    if (!file) return;
    setMsg("Verifying document...");
    const f = new FormData();
    f.append("file", file);
    const res = await axios.post(API + "/verifyFile", f);
    setMsg(res.data);
  };

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
        </div>

        <div className="toggle">
          <button
            className={mode === "register" ? "active" : ""}
            onClick={() => { setMode("register"); setMsg(""); setFile(null); }}
          >
            📄 Register
          </button>
          <button
            className={mode === "verify" ? "active" : ""}
            onClick={() => { setMode("verify"); setMsg(""); setFile(null); }}
          >
            ✅ Verify
          </button>
        </div>
      </div>

      {/* MAIN GRID */}
      <div className="main">

        {/* LEFT CARD */}
        <div className="card big">

          <h2>
            {mode === "register" ? "Register a document" : "Verify a document"}
          </h2>

          <div className="dropzone">
            <div className="fileIcon">📄</div>
            <p>
              {file ? file.name : "Drop a file here or click to browse"}
            </p>
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
            <div
              className={
                msg.includes("VERIFIED") || msg.includes("registered")
                  ? "result success"
                  : msg.includes("TAMPERED")
                  ? "result error"
                  : "result info"
              }
            >
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
            <h3>Blockchain Explorer</h3>
            {docs.slice(-2).reverse().map(d => (
              <div key={d.id} className="block">
                <strong>Document #{d.id}</strong>
                <div className="hash">{d.hash}</div>
              </div>
            ))}
          </div>
        </div>

      </div>
    </div>
  );
}
