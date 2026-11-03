import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import Expenses from './pages/Expenses';
import Incomes from './pages/Incomes';
import Categories from './pages/UserCategories';
import AdminDashboard from './pages/AdminDashboard';
import AdminUsers from './pages/AdminUsers';
import AdminCategories from './pages/AdminCategories';
import ProtectedRoute from './components/ProtectedRoute';
import AdminRoute from './components/AdminRoute';
import Navbar from './components/Navbar';

function HomeRedirect() {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    return <Navigate to={user.role === 'ADMIN' ? '/admin' : '/dashboard'} replace />;
}

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />

                <Route
                    path="/dashboard"
                    element={
                        <ProtectedRoute>
                            <Navbar />
                            <Dashboard />
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/expenses"
                    element={
                        <ProtectedRoute>
                            <Navbar />
                            <Expenses />
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/incomes"
                    element={
                        <ProtectedRoute>
                            <Navbar />
                            <Incomes />
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/categories"
                    element={
                        <ProtectedRoute>
                            <Navbar />
                            <Categories />
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/admin"
                    element={<ProtectedRoute><AdminRoute><Navbar /><AdminDashboard /></AdminRoute></ProtectedRoute>}
                />
                <Route
                    path="/admin/users"
                    element={<ProtectedRoute><AdminRoute><Navbar /><AdminUsers /></AdminRoute></ProtectedRoute>}
                />
                <Route
                    path="/admin/categories"
                    element={<ProtectedRoute><AdminRoute><Navbar /><AdminCategories /></AdminRoute></ProtectedRoute>}
                />

                <Route path="/" element={<HomeRedirect />} />
                <Route path="*" element={<HomeRedirect />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
