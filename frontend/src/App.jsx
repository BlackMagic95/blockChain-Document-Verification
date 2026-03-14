import { Routes, Route, Navigate } from "react-router-dom";
import { Toaster } from "react-hot-toast";

import Login from "./pages/Login";
import VerifyPage from "./pages/VerifyPage";
import AdminPage from "./pages/AdminPage";
import SuperAdminPage from "./pages/SuperAdminPage";
import CollegeRequestPage from "./pages/CollegeRequestPage";
import ProtectedRoute from "./components/ProtectedRoute";

export default function App() {
  return (
    <>
      <Toaster position="top-right" reverseOrder={false} />
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/verify" element={<VerifyPage />} />
        <Route path="/college-request" element={<CollegeRequestPage />} />

        <Route
          path="/admin"
          element={
            <ProtectedRoute roles={["COLLEGE_ADMIN", "SUPER_ADMIN"]}>
              <AdminPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/super-admin"
          element={
            <ProtectedRoute role="SUPER_ADMIN">
              <SuperAdminPage />
            </ProtectedRoute>
          }
        />

        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </>
  );
}