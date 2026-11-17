
import { useState } from 'react';
import API from '../api';

function Register({ onShowLogin }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const handleRegister = async (e) => {
    e.preventDefault();
    setMessage('');
    setError('');

    try {
      await API.post('/auth/register', {
        username,
        password
      });

      setMessage('Registration successful! Please login.');
      setUsername('');
      setPassword('');
    } catch (error) {
      console.error('Registration error:', error);
      setError('Registration failed. Username may already exist.');
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h1>Create Account</h1>
        <p>Start tracking your expenses today!</p>

        <form onSubmit={handleRegister}>
          <label>Username</label>
          <input
            type="text"
            placeholder="Choose a username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />

          <label>Password</label>
          <input
            type="password"
            placeholder="Choose a password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          {message && <p>{message}</p>}
          {error && <p className="auth-error">{error}</p>}

          <button type="submit">Register</button>
        </form>

        <p>
          Already have an account?{' '}
          <button type="button" onClick={onShowLogin}>
            Login
          </button>
        </p>
      </div>
    </div>
  );
}

export default Register;
