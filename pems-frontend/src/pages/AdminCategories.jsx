import { useEffect, useState } from 'react';
import api from '../api/axios';

function CategoryList({ title, endpoint }) {
    const [categories, setCategories] = useState([]);
    const [name, setName] = useState('');
    const load = async () => setCategories((await api.get(endpoint)).data);
    useEffect(() => { load(); }, []);
    const add = async (event) => { event.preventDefault(); await api.post(endpoint, { name }); setName(''); load(); };
    const rename = async (category) => { const nextName = window.prompt('Category name', category.name); if (nextName?.trim()) { await api.put(`${endpoint}/${category.id}`, { name: nextName.trim() }); load(); } };
    const setStatus = async (category) => { await api.patch(`${endpoint}/${category.id}/status`, { active: !category.active }); load(); };
    return <div><h3>{title}</h3><form className="form-row" onSubmit={add}><input value={name} onChange={(event) => setName(event.target.value)} placeholder="New category" required /><button>Add</button></form><ul className="list">{categories.map((category) => <li key={category.id}><span>{category.name} {!category.active && '(Archived)'}</span><span><button onClick={() => rename(category)}>Edit</button><button onClick={() => setStatus(category)}>{category.active ? 'Archive' : 'Reactivate'}</button></span></li>)}</ul></div>;
}

export default function AdminCategories() {
    return <div className="page"><h2>Category Management</h2><p className="page-description">Archived categories remain visible in historical transactions and cannot be selected for new entries.</p><div className="two-columns"><CategoryList title="Expense Categories" endpoint="/admin/categories/expenses" /><CategoryList title="Income Categories" endpoint="/admin/categories/incomes" /></div></div>;
}
