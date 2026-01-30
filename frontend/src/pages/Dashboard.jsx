import { useState, useEffect } from "react";
import api from "../api/axios";
import { getRole, logout } from "../utils/auth";

export default function Dashboard() {
  const role = getRole();
  const [file, setFile] = useState(null);
  const [msg, setMsg] = useState("");

  const register = async () => {
    const f = new FormData();
    f.append("file", file);

    try {
      await api.post("/upload", f);
      setMsg("Document registered ✅");
    } catch {
      setMsg("Not authorized ❌");
    }
  };

  const verify = async () => {
    const f = new FormData();
    f.append("file", file);

    const res = await api.post("/verify", f);
    setMsg(res.data.status);
  };

  return (
    <div>
      <h2>Dashboard ({role})</h2>

      <input type="file" onChange={e => setFile(e.target.files[0])} />

      {role === "ADMIN" && (
        <button onClick={register}>Register Document</button>
      )}

      <button onClick={verify}>Verify Document</button>

      <button onClick={() => { logout(); window.location.reload(); }}>
        Logout
      </button>

      {msg && <p>{msg}</p>}
    </div>
  );
}
