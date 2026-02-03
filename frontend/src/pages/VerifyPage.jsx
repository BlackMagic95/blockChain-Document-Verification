import { useState, useRef } from "react";
import axios from "axios";
import "./VerifyPage.css";

const API = "http://localhost:8080";

export default function VerifyPage() {
  const [file, setFile] = useState(null);
  const [msg, setMsg] = useState("");
  const [type, setType] = useState("");
  const [loading, setLoading] = useState(false);

  const fileRef = useRef();

  const handleVerify = async () => {
    if (!file) {
      setMsg("Please select a document");
      setType("error");
      return;
    }

    try {
      setLoading(true);
      setMsg("Verifying document integrity...");
      setType("info");

      const form = new FormData();
      form.append("file", file);

      const res = await axios.post(`${API}/verify`, form);

      if (res.data.status === "VERIFIED") {
        setMsg(`VERIFIED ✅ (Timestamp: ${res.data.verifiedAt})`);
        setType("success");
      } else if (res.data.status === "NOT_REGISTERED") {
        setMsg("Document not registered ❌");
        setType("error");
      } else if (res.data.status === "CHAIN_MISSING") {
        setMsg("Blockchain record missing ⚠️");
        setType("error");
      } else {
        setMsg("Verification failed");
        setType("error");
      }

      setFile(null);
    } catch {
      setMsg("Verification error ❌");
      setType("error");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="verify-page">

      <div className="verify-card">

        <div className="verify-header">
          <h1>📄 Document Verification</h1>
          <p>Secure blockchain based authenticity check</p>
        </div>

        <div
          className={`upload-tile ${file ? "selected" : ""}`}
          onClick={() => fileRef.current.click()}
        >
          <div className="plus">＋</div>
          <p>{file ? file.name : "Click or drop your document here"}</p>

          <input
            ref={fileRef}
            type="file"
            hidden
            onChange={(e) => setFile(e.target.files[0])}
          />
        </div>

        <button
          className="primary-btn"
          onClick={handleVerify}
          disabled={loading}
        >
          {loading ? "Verifying..." : "Verify Document"}
        </button>

        {msg && <div className={`result ${type}`}>{msg}</div>}

      </div>

    </div>
  );
}
