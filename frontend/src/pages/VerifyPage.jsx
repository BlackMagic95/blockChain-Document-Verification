import { useState, useRef } from "react";
import axios from "axios";
import "./VerifyPage.css";
import toast from "react-hot-toast";

const API = "http://localhost:8080";

export default function VerifyPage() {
  const [file, setFile] = useState(null);
  const [msg, setMsg] = useState("");
  const [type, setType] = useState("");
  const [loading, setLoading] = useState(false);

  const fileRef = useRef();

  /* ================= IST FORMATTER ================= */
  const formatIST = (isoString) => {
    const date = new Date(isoString);

    return date.toLocaleString("en-IN", {
      timeZone: "Asia/Kolkata",
      day: "2-digit",
      month: "short",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
      hour12: true
    });
  };

  /* ================= VERIFY ================= */
  const handleVerify = async () => {
    if (!file) {
      setMsg("Please select a document");
      setType("error");
      return;
    }

    try {
      setLoading(true);
      toast.loading("Verifying document...");
      setType("info");

      const form = new FormData();
      form.append("file", file);

      const res = await axios.post(`${API}/verify`, form);

      if (res.data.status === "VERIFIED") {

        // ⭐⭐ FIXED DATE HERE ⭐⭐
        const formattedTime = formatIST(res.data.verifiedAt);
        toast.dismiss();
toast.success(`Verified successfully ✅ (${formattedTime} IST)`);
        setType("success");

      } else if (res.data.status === "NOT_REGISTERED") {
        toast.dismiss();
toast.error("Document not registered ❌");

        setType("error");

      } else if (res.data.status === "CHAIN_MISSING") {
        toast.dismiss();
toast.error("Blockchain record missing ⚠️");

        setType("error");

      } else {
        toast.dismiss();
toast.error("Verification failed ❌");

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

  /* ================= UI ================= */
  return (
    <div className="verify-page">
      <div className="verify-container">

        <div className="verify-header">
          <h1>Verify Document</h1>
          <p>Check document authenticity using blockchain</p>
        </div>

        <div className="card">

          <div
            className="upload-tile"
            onClick={() => fileRef.current.click()}
          >
            <div className="plus">＋</div>
            <p>{file ? file.name : "Click to upload document"}</p>

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
    </div>
  );
}
