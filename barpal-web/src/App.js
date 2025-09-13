import './App.css';
import { useEffect, useState } from 'react';
import { apiGet, apiPost, API_BASE } from './api';

function App() {
  const [dbStatus, setDbStatus] = useState(null);
  const [ollamaStatus, setOllamaStatus] = useState(null);
  const [prompt, setPrompt] = useState('Say hello from the frontend');
  const [loadingDb, setLoadingDb] = useState(false);
  const [loadingOllama, setLoadingOllama] = useState(false);
  const [error, setError] = useState(null);

  const loadDb = async () => {
    setLoadingDb(true); setError(null);
    try { setDbStatus(await apiGet('/api/status/db')); } catch(e){ setError(e.message); }
    finally { setLoadingDb(false); }
  };
  const callOllama = async () => {
    setLoadingOllama(true); setError(null);
    try { setOllamaStatus(await apiPost('/api/status/ollama-test', { prompt })); } catch(e){ setError(e.message); }
    finally { setLoadingOllama(false); }
  };

  useEffect(() => { loadDb(); }, []);

  return (
    <div className="App" style={{padding: '2rem', fontFamily:'sans-serif'}}>
      <h1>BarPal Connectivity Dashboard</h1>
      <p>Backend API base: <code>{API_BASE}</code></p>
      {error && <div style={{color:'red'}}>Error: {error}</div>}
      <section style={{marginBottom:'2rem'}}>
        <h2>Database Status</h2>
        <button onClick={loadDb} disabled={loadingDb}>{loadingDb ? 'Checking...' : 'Re-check DB'}</button>
        <pre style={{background:'#111', color:'#0f0', padding:'1rem', overflowX:'auto'}}>{dbStatus ? JSON.stringify(dbStatus, null, 2): 'Loading...'}</pre>
      </section>
      <section>
        <h2>Ollama Test</h2>
        <div style={{display:'flex', gap:'0.5rem', flexWrap:'wrap'}}>
          <input style={{flex:'1 1 320px'}} value={prompt} onChange={e=>setPrompt(e.target.value)} />
          <button onClick={callOllama} disabled={loadingOllama}>{loadingOllama ? 'Calling...' : 'Send Prompt'}</button>
        </div>
        <pre style={{background:'#111', color:'#0ff', padding:'1rem', marginTop:'1rem', overflowX:'auto'}}>{ollamaStatus ? JSON.stringify(ollamaStatus, null, 2) : 'No call yet'}</pre>
      </section>
    </div>
  );
}

export default App;
