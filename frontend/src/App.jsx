import { Routes, Route, Navigate } from "react-router-dom";
import { Toaster } from "react-hot-toast";

import Login from "./pages/Login";
import VerifyPage from "./pages/VerifyPage";
import AdminPage from "./pages/AdminPage";

export default function App() {
  return (
    <>
      <Toaster
        position="top-right"
        reverseOrder={false}
        toastOptions={{
          duration: 5000, 
          style: {
            background: "#0f172a",
            color: "#fff",
            border: "1px solid #334155",
            padding: "14px 18px",
            fontSize: "14px",
            borderRadius: "12px",
            boxShadow: "0 10px 30px rgba(0,0,0,0.4)"
          },
          success: {
            duration: 4000
          },
          error: {
            duration: 6000
          }
        }}
      />

      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/verify" element={<VerifyPage />} />
        <Route path="/admin" element={<AdminPage />} />
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </>
  );
}
