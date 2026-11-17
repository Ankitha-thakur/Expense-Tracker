
import axios from 'axios';

const API = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
});

export const AUTH_API = axios.create({
  baseURL: import.meta.env.VITE_API_URL.replace(/\/trade\/?$/, ''),
});

// Automatically attach JWT token to every request
const addToken = (config) => {
  const token = localStorage.getItem('token');

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
};

API.interceptors.request.use(addToken);
AUTH_API.interceptors.request.use(addToken);

export default API;
