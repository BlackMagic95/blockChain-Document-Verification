import { Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import VerifyPage from "./pages/VerifyPage";
import AdminPage from "./pages/AdminPage";
import ProtectedRoute from "./components/ProtectedRoute";

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />

      <Route
        path="/verify"
        element={
          <ProtectedRoute>
            <VerifyPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/admin"
        element={
          <ProtectedRoute role="ROLE_ADMIN">
            <AdminPage />
          </ProtectedRoute>
        }
      />
    </Routes>
  );
}
