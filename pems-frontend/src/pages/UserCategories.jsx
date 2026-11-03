import { useEffect, useState } from 'react';
import api from '../api/axios';

export default function UserCategories() {
    const [expenseCategories, setExpenseCategories] = useState([]);
    const [incomeCategories, setIncomeCategories] = useState([]);

    useEffect(() => {
        Promise.all([
            api.get('/categories'),
            api.get('/income-categories'),
        ]).then(([expenseResponse, incomeResponse]) => {
            setExpenseCategories(expenseResponse.data);
            setIncomeCategories(incomeResponse.data);
        });
    }, []);

    return (
        <div className="page">
            <h2>Categories</h2>
            <p className="page-description">Categories are managed by the administrator.</p>
            <div className="two-columns">
                <div>
                    <h3>Expense Categories</h3>
                    <ul className="list">
                        {expenseCategories.map((category) => <li key={category.id}>{category.name}</li>)}
                    </ul>
                </div>
                <div>
                    <h3>Income Categories</h3>
                    <ul className="list">
                        {incomeCategories.map((category) => <li key={category.id}>{category.name}</li>)}
                    </ul>
                </div>
            </div>
        </div>
    );
}
