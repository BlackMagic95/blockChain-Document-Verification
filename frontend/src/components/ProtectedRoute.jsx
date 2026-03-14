import { Navigate } from "react-router-dom";
import { getToken, getRole } from "../auth";

export default function ProtectedRoute({ children, role, roles }) {
  const token = getToken();
  const userRole = getRole();

  if (!token) return <Navigate to="/" />;
  if (role && userRole !== role) return <Navigate to="/verify" />;
  if (roles && Array.isArray(roles) && !roles.includes(userRole)) return <Navigate to="/verify" />;

  return children;
}