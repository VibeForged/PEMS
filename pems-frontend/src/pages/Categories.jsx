import { useEffect, useState } from 'react';
import api from '../api/axios';

export default function Categories() {
    const [expenseCats, setExpenseCats] = useState([]);
    const [incomeCats, setIncomeCats] = useState([]);
    const [expenseName, setExpenseName] = useState('');
    const [incomeName, setIncomeName] = useState('');

    const fetchData = async () => {
        const [e, i] = await Promise.all([
            api.get('/categories'),
            api.get('/income-categories'),
        ]);
        setExpenseCats(e.data);
        setIncomeCats(i.data);
    };

    useEffect(() => { fetchData(); }, []);

    const addExpenseCat = async (e) => {
        e.preventDefault();
        await api.post('/categories', { name: expenseName });
        setExpenseName('');
        fetchData();
    };

    const addIncomeCat = async (e) => {
        e.preventDefault();
        await api.post('/income-categories', { name: incomeName });
        setIncomeName('');
        fetchData();
    };

    const deleteExpenseCat = async (id) => {
        if (!window.confirm('Delete?')) return;
        await api.delete(`/categories/${id}`);
        fetchData();
    };

    const deleteIncomeCat = async (id) => {
        if (!window.confirm('Delete?')) return;
        await api.delete(`/income-categories/${id}`);
        fetchData();
    };

    return (
        <div className="page">
            <h2>Categories</h2>
            <p className="page-description">Categories are managed by the administrator.</p>

            <div className="two-columns">
                <div>
                    <h3>Expense Categories</h3>
                    <form onSubmit={addExpenseCat} className="form-row" hidden>
                        <input
                            placeholder="New category"
                            value={expenseName}
                            onChange={(e) => setExpenseName(e.target.value)}
                            required
                        />
                        <button type="submit">Add</button>
                    </form>
                    <ul className="list">
                        {expenseCats.map((c) => (
                            <li key={c.id}>
                                {c.name}
                                <button onClick={() => deleteExpenseCat(c.id)}>✖</button>
                            </li>
                        ))}
                    </ul>
                </div>

                <div>
                    <h3>Income Categories</h3>
                    <form onSubmit={addIncomeCat} className="form-row" hidden>
                        <input
                            placeholder="New category"
                            value={incomeName}
                            onChange={(e) => setIncomeName(e.target.value)}
                            required
                        />
                        <button type="submit">Add</button>
                    </form>
                    <ul className="list">
                        {incomeCats.map((c) => (
                            <li key={c.id}>
                                {c.name}
                                <button onClick={() => deleteIncomeCat(c.id)}>✖</button>
                            </li>
                        ))}
                    </ul>
                </div>
            </div>
        </div>
    );
}
