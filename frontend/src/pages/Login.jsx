import { useState } from "react";
import api from "../api/axios";
import { setAuth } from "../utils/auth";

export default function Login({ onLogin }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [msg, setMsg] = useState("");

  const handleLogin = async () => {
    try {
      const res = await api.post("/auth/login", {
        email,
        password,
      });

      setAuth(res.data.token, res.data.role);
      onLogin(); // refresh app
    } catch (err) {
      setMsg("Invalid credentials");
    }
  };

  return (
    <div className="login">
      <h2>Login</h2>

      <input
        placeholder="Email"
        value={email}
        onChange={e => setEmail(e.target.value)}
      />

      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={e => setPassword(e.target.value)}
      />

      <button onClick={handleLogin}>Login</button>

      {msg && <p>{msg}</p>}
    </div>
  );
}
