import { useState, useRef } from "react";
import axios from "axios";
import "./VerifyPage.css";
import toast from "react-hot-toast";
import { useNavigate } from "react-router-dom";

const API = "http://localhost:8080";

export default function VerifyPage() {

  const [file, setFile] = useState(null);
  const [msg, setMsg] = useState("");
  const [type, setType] = useState("");
  const [loading, setLoading] = useState(false);

  // ⭐ NEW
  const [downloadUrl, setDownloadUrl] = useState("");

  const fileRef = useRef();


  /* ================= IST FORMATTER ================= */
  const formatIST = (isoString) => {
    if (!isoString) return "";

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

const navigate = useNavigate();

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
      setDownloadUrl(""); // reset previous link

      const form = new FormData();
      form.append("file", file);

      const res = await axios.post(`${API}/verify`, form);
      const status = res.data.status;

      toast.dismiss();


      /* ===== HYBRID STATUS HANDLING ===== */

      if (status === "VERIFIED") {

        const formattedTime = formatIST(res.data.verifiedAt);

        toast.success(`Verified successfully ✅ (${formattedTime} IST)`);

        setMsg("Mongo + Blockchain verified");
        setType("success");

        // ⭐ SET DOWNLOAD LINK
        setDownloadUrl(res.data.fileUrl);
      }

      else if (status === "TAMPERED_DB") {
        toast.error("⚠ Database mismatch detected (tampered)");
        setMsg("Database record corrupted");
        setType("error");
      }

      else if (status === "BLOCKCHAIN_ONLY") {
        toast("Exists on blockchain but DB missing ⚠️", { icon: "⚠️" });
        setMsg("Blockchain verified only");
        setType("info");
      }

      else if (status === "NOT_REGISTERED") {
        toast.error("Document not registered ❌");
        setMsg("Not registered");
        setType("error");
      }

      else {
        toast.error("Verification failed ❌");
        setType("error");
      }

      setFile(null);

    } catch {
      toast.dismiss();
      toast.error("Verification error ❌");
      setMsg("Verification error");
      setType("error");

    } finally {
      setLoading(false);
    }
  };


  /* ================= UI ================= */
  return (
    <div className="verify-page">
      <div className="verify-container">

  {/* BACK BUTTON */}
  <button
    className="back-btn"
    onClick={() => navigate("/")}
  >
    ← Back
  </button>


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


          {/* ⭐ DOWNLOAD BUTTON */}
          {downloadUrl && (
            <a
              href={downloadUrl}
              target="_blank"
              rel="noreferrer"
              className="download-btn"
            >
              ⬇ Download Verified File
            </a>
          )}


          {msg && <div className={`result ${type}`}>{msg}</div>}

        </div>
      </div>
    </div>
  );
}
