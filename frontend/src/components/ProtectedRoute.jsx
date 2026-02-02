import { Navigate } from "react-router-dom";
import { getToken, getRole } from "../auth";

export default function ProtectedRoute({ children, role }) {
  const token = getToken();
  const userRole = getRole();

  if (!token) return <Navigate to="/" />;
  if (role && userRole !== role) return <Navigate to="/verify" />;

  return children;
}
