// Central place to resolve backend API base URL
// Priority:
// 1. Build-time env variable REACT_APP_API_BASE_URL
// 2. Derive from current window location (assumes frontend on :3003 and backend on :8083)
// 3. Fallback default http://localhost:8083
// This lets us avoid having to rebuild the frontend every time the backend host/port changes.
// (Longer term: switch to a runtime-loaded /config.json served by nginx.)
// eslint-disable-next-line no-undef
const buildTime = process.env.REACT_APP_API_BASE_URL;
let derived;
try {
  if (typeof window !== 'undefined') {
    const { protocol, hostname, port } = window.location;
    if (port === '3003') {
      derived = `${protocol}//${hostname}:8083`;
    } else if (!port) { // e.g., default ports 80/443
      // If served via reverse proxy same origin, we can just use relative path later
      // but keep explicit for now
      derived = `${protocol}//${hostname}:8083`;
    }
  }
} catch (e) { /* ignore */ }
export const API_BASE = buildTime || derived || 'http://localhost:8083';

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
