import { useEffect, useState } from 'react';
import api from '../api/axios';

export default function AdminUsers() {
    const [users, setUsers] = useState([]);
    const [search, setSearch] = useState('');
    const loadUsers = async () => {
        const response = await api.get('/admin/users', { params: { search, size: 50, sort: 'createdAt,desc' } });
        setUsers(response.data.content);
    };
    useEffect(() => { loadUsers(); }, []);
    const setStatus = async (user) => { await api.patch(`/admin/users/${user.id}/status`, { active: !user.active }); loadUsers(); };
    const deleteUser = async (user) => {
        if (!window.confirm(`Permanently delete ${user.name} and all their transactions?`)) return;
        await api.delete(`/admin/users/${user.id}`, { data: { confirmed: true } });
        loadUsers();
    };

    return <div className="page"><h2>User Management</h2>
        <form className="form-row" onSubmit={(event) => { event.preventDefault(); loadUsers(); }}><input value={search} onChange={(event) => setSearch(event.target.value)} placeholder="Search name or email" /><button type="submit">Search</button></form>
        <table className="data-table"><thead><tr><th>Name</th><th>Email</th><th>Role</th><th>Status</th><th>Transactions</th><th>Joined</th><th>Actions</th></tr></thead>
            <tbody>{users.map((user) => <tr key={user.id}><td>{user.name}</td><td>{user.email}</td><td>{user.role}</td><td>{user.active ? 'Active' : 'Blocked'}</td><td>{user.transactionCount}</td><td>{new Date(user.createdAt).toLocaleDateString()}</td><td><button onClick={() => setStatus(user)}>{user.active ? 'Block' : 'Activate'}</button><button onClick={() => deleteUser(user)}>Delete</button></td></tr>)}</tbody>
        </table>
    </div>;
}
