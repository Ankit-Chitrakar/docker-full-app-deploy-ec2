import { useAuth } from '../context/AuthContext';

export default function Home() {
  const { user, logout } = useAuth();

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h1>Dashboard</h1>
        <p>Welcome back, <strong>{user?.fullName}</strong>!</p>
        <p>Email: {user?.email}</p>
        <button style={styles.button} onClick={logout}>Sign Out</button>
      </div>
    </div>
  );
}

const styles = {
  container: { display: 'flex', justifyContent: 'center', alignItems: 'center', height: '80vh' },
  card: { width: '400px', padding: '30px', border: '1px solid #ddd', borderRadius: '8px', textAlign: 'center', boxShadow: '0 4px 6px rgba(0,0,0,0.1)' },
  button: { marginTop: '20px', padding: '10px 20px', backgroundColor: '#dc3545', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer' }
};