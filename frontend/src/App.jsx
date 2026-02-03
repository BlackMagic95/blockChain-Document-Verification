import { Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import VerifyPage from "./pages/VerifyPage";
import AdminPage from "./pages/AdminPage";

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/verify" element={<VerifyPage />} />
      <Route path="/admin" element={<AdminPage />} />
      <Route path="*" element={<Navigate to="/" />} />
    </Routes>
  );
}
