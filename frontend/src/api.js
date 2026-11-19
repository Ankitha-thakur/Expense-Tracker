
import axios from 'axios';

const API = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
});

export const AUTH_API = axios.create({
  baseURL: import.meta.env.VITE_API_URL.replace(/\/trade\/?$/, ''),
});

// Attach the access token to requests
const addToken = (config) => {
  const token = localStorage.getItem('token');

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
};

API.interceptors.request.use(addToken);
AUTH_API.interceptors.request.use(addToken);

// Automatically refresh an expired access token
API.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (
      error.response?.status === 401 &&
      originalRequest &&
      !originalRequest._retry
    ) {
      originalRequest._retry = true;

      try {
        const refreshToken = localStorage.getItem('refreshToken');

        if (!refreshToken) {
          throw new Error('No refresh token found');
        }

        const response = await AUTH_API.post('/auth/refresh', {
          refreshToken,
        });

        const newToken = response.data.token;

        localStorage.setItem('token', newToken);

        originalRequest.headers.Authorization = `Bearer ${newToken}`;

        return API(originalRequest);
      } catch (refreshError) {
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');

        window.dispatchEvent(new Event('auth:logout'));

        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export default API;
