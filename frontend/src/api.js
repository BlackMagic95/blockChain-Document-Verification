import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL
,
  withCredentials: false
});

/* 🔐 attach JWT to EVERY request */
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

/* 🔥 show real backend errors */
api.interceptors.response.use(
  (res) => res,
  (err) => {
    console.error("API ERROR →", err.response?.data || err.message);
    return Promise.reject(err);
  }
);

export default api;
