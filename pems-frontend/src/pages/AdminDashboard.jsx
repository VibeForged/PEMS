import { useEffect, useState } from 'react';
import api from '../api/axios';

export default function AdminDashboard() {
    const [data, setData] = useState(null);

    useEffect(() => {
        api.get('/admin/dashboard').then((response) => setData(response.data));
    }, []);

    if (!data) return <p className="page">Loading admin dashboard...</p>;

    const cards = [
        ['Total Users', data.totalUsers], ['Active Users', data.activeUsers],
        ['Blocked Users', data.blockedUsers], ['New This Month', data.newUsersThisMonth],
        ['Transactions', data.totalTransactions], ['Total Income', `₹ ${data.totalIncome}`],
        ['Total Expense', `₹ ${data.totalExpense}`], ['Monthly Income', `₹ ${data.monthlyIncome}`],
        ['Monthly Expense', `₹ ${data.monthlyExpense}`],
    ];

    return <div className="page"><h2>Admin Dashboard</h2>
        <p className="page-description">Platform-wide totals only. Individual financial records remain private.</p>
        <div className="admin-cards">{cards.map(([label, value]) => <div className="card" key={label}><h4>{label}</h4><p>{value}</p></div>)}</div>
    </div>;
}
