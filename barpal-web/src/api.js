// Central place to resolve backend API base URL
// Uses build-time env variable REACT_APP_API_BASE_URL injected via docker-compose
export const API_BASE = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080';

export async function apiGet(path) {
  const res = await fetch(`${API_BASE}${path}`);
  if (!res.ok) throw new Error(`GET ${path} failed ${res.status}`);
  return res.json();
}

export async function apiPost(path, body) {
  const res = await fetch(`${API_BASE}${path}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body || {})
  });
  if (!res.ok) throw new Error(`POST ${path} failed ${res.status}`);
  return res.json();
}
