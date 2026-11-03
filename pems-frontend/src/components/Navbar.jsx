import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';

export default function Navbar() {
    const navigate = useNavigate();
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    const isAdmin = user.role === 'ADMIN';

    const [theme, setTheme] = useState(() => {
        return localStorage.getItem('theme') || 'light';
    });

    // Apply theme to body whenever it changes
    useEffect(() => {
        document.body.classList.remove('light', 'dark');
        document.body.classList.add(theme);
        localStorage.setItem('theme', theme);
    }, [theme]);

    const toggleTheme = () => {
        setTheme((prev) => (prev === 'dark' ? 'light' : 'dark'));
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        // theme ko rakhna hai ya nahi — apni marzi
        navigate('/login');
    };

    return (
        <nav className="navbar">
            <div className="nav-brand">💰 PEMS</div>

            <div className="nav-links">
                {isAdmin ? <>
                    <Link to="/admin">Admin Dashboard</Link>
                    <Link to="/admin/users">Users</Link>
                    <Link to="/admin/categories">Categories</Link>
                </> : <>
                    <Link to="/dashboard">Dashboard</Link>
                    <Link to="/expenses">Expenses</Link>
                    <Link to="/incomes">Incomes</Link>
                    <Link to="/categories">Categories</Link>
                </>}
            </div>

            <div className="nav-user">
                <span>Hi, {user.name || 'User'}</span>

                <button
                    className="theme-toggle"
                    onClick={toggleTheme}
                    title={theme === 'dark' ? 'Switch to light mode' : 'Switch to dark mode'}
                    aria-label="Toggle theme"
                >
                    {theme === 'dark' ? '☀️' : '🌙'}
                </button>

                <button onClick={handleLogout}>Logout</button>
            </div>
        </nav>
    );
}
