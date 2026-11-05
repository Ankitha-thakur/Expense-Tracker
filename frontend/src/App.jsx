
import { useState, useEffect } from 'react';
import './App.css';
import API from './api';

import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend,
  CategoryScale,
  LinearScale,
  BarElement,
  Title
} from 'chart.js';

import { Pie, Bar } from 'react-chartjs-2';

ChartJS.register(
  ArcElement,
  Tooltip,
  Legend,
  CategoryScale,
  LinearScale,
  BarElement,
  Title
);

function App() {
  const [expenses, setExpenses] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('All');
  const [selectedDate, setSelectedDate] = useState('');

  const [title, setTitle] = useState('');
  const [amount, setAmount] = useState('');
  const [category, setCategory] = useState('');
  const [date, setDate] = useState('');

  const [loading, setLoading] = useState(true);

  // Load expenses from Spring Boot when page opens
  useEffect(() => {
    fetchExpenses();
  }, []);

  const fetchExpenses = async () => {
    try {
      const response = await API.get('/all');
      setExpenses(response.data);
    } catch (error) {
      console.error('Error fetching expenses:', error);
      alert('Unable to load expenses. Please check your backend.');
    } finally {
      setLoading(false);
    }
  };

  // Add expense to PostgreSQL through Spring Boot
  const addExpense = async (e) => {
    e.preventDefault();

    if (!title || !amount || Number(amount) <= 0 || !category || !date) {
      alert('Please fill all fields with valid values');
      return;
    }

    const newExpense = {
      title: title,
      amount: Number(amount),
      category: category,
      date: date
    };

    try {
      const response = await API.post('/save', newExpense);

      setExpenses([...expenses, response.data]);

      setTitle('');
      setAmount('');
      setCategory('');
      setDate('');

      alert('Expense saved successfully!');
    } catch (error) {
      console.error('Error saving expense:', error);
      alert('Unable to save expense. Please check your backend.');
    }
  };

  // Calculate total expenses
  const total = expenses.reduce(
    (sum, expense) => sum + Number(expense.amount),
    0
  );

  // Calculate current month expenses
  const currentMonth = new Date()
    .toLocaleDateString('en-CA')
    .slice(0, 7);

  const monthlyTotal = expenses
    .filter((expense) => expense.date.startsWith(currentMonth))
    .reduce(
      (sum, expense) => sum + Number(expense.amount),
      0
    );

  // Delete expense from PostgreSQL
  const deleteExpense = async (id) => {
    try {
      await API.delete(`/delete/${id}`);

      setExpenses(expenses.filter((expense) => expense.id !== id));

      alert('Expense deleted successfully!');
    } catch (error) {
      console.error('Error deleting expense:', error);
      alert('Unable to delete expense.');
    }
  };

  // Filter by category and date
  const filteredExpenses = expenses.filter((expense) => {
    const categoryMatch =
      selectedCategory === 'All' ||
      expense.category === selectedCategory;

    const dateMatch =
      selectedDate === '' ||
      expense.date === selectedDate;

    return categoryMatch && dateMatch;
  });

  // Category-wise totals for charts
  const categories = [
    'Food',
    'Travel',
    'Shopping',
    'Education',
    'Other'
  ];

  const categoryTotals = categories.map((cat) =>
    expenses
      .filter((expense) => expense.category === cat)
      .reduce(
        (sum, expense) => sum + Number(expense.amount),
        0
      )
  );

  // Pie chart data
  const pieData = {
    labels: categories,
    datasets: [
      {
        label: 'Expenses',
        data: categoryTotals,
        backgroundColor: [
          '#3b82f6',
          '#22c55e',
          '#f59e0b',
          '#a855f7',
          '#ef4444'
        ],
        borderWidth: 1
      }
    ]
  };

  // Bar chart data
  const barData = {
    labels: categories,
    datasets: [
      {
        label: 'Amount Spent (₹)',
        data: categoryTotals,
        backgroundColor: '#3b82f6'
      }
    ]
  };

  const barOptions = {
    responsive: true,
    plugins: {
      legend: {
        display: false
      },
      title: {
        display: true,
        text: 'Category-wise Expenses'
      }
    }
  };

  return (
    <div className="container">

      <header>
        <h1>Trade Expense Tracker</h1>
        <p>Manage and track your daily expenses easily</p>
      </header>

      {/* Summary */}
      <div className="summary">

        <div className="summary-card">
          <h3>Total Expenses</h3>
          <h2>₹{total.toFixed(2)}</h2>
        </div>

        <div className="summary-card">
          <h3>Monthly Expenses</h3>
          <h2>₹{monthlyTotal.toFixed(2)}</h2>
        </div>

        <div className="summary-card">
          <h3>Total Transactions</h3>
          <h2>{expenses.length}</h2>
        </div>

      </div>

      {/* Charts */}
      <div className="expense-card">
        <h2>Expense Dashboard</h2>

        <div className="charts-container">

          <div className="chart-box">
            <h3>Expenses by Category - Pie Chart</h3>

            {expenses.length > 0 ? (
              <Pie data={pieData} />
            ) : (
              <p>Add expenses to view the pie chart.</p>
            )}
          </div>

          <div className="chart-box">
            <h3>Expenses by Category - Bar Chart</h3>

            {expenses.length > 0 ? (
              <Bar data={barData} options={barOptions} />
            ) : (
              <p>Add expenses to view the bar chart.</p>
            )}
          </div>

        </div>
      </div>

      {/* Add Expense */}
      <div className="expense-card">

        <h2>Add New Expense</h2>

        <form onSubmit={addExpense}>

          <label>Expense Title</label>
          <input
            type="text"
            placeholder="Enter expense title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
          />

          <label>Amount (₹)</label>
          <input
            type="number"
            placeholder="Enter amount"
            min="0.01"
            step="0.01"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            required
          />

          <label>Category</label>
          <select
            value={category}
            onChange={(e) => setCategory(e.target.value)}
            required
          >
            <option value="">Select Category</option>
            <option value="Food">Food</option>
            <option value="Travel">Travel</option>
            <option value="Shopping">Shopping</option>
            <option value="Education">Education</option>
            <option value="Other">Other</option>
          </select>

          <label>Date</label>
          <input
            type="date"
            value={date}
            onChange={(e) => setDate(e.target.value)}
            required
          />

          <button type="submit">Add Expense</button>

        </form>

      </div>

      {/* Recent Expenses */}
      <div className="expense-card">

        <h2>Recent Expenses</h2>

        <label>Filter by Category</label>

        <select
          value={selectedCategory}
          onChange={(e) => setSelectedCategory(e.target.value)}
        >
          <option value="All">All Categories</option>
          <option value="Food">Food</option>
          <option value="Travel">Travel</option>
          <option value="Shopping">Shopping</option>
          <option value="Education">Education</option>
          <option value="Other">Other</option>
        </select>

        <label>Filter by Date</label>

        <input
          type="date"
          value={selectedDate}
          onChange={(e) => setSelectedDate(e.target.value)}
        />

        {loading ? (

          <p>Loading expenses...</p>

        ) : filteredExpenses.length === 0 ? (

          <p className="empty-message">
            No expenses found.
          </p>

        ) : (

          filteredExpenses.map((expense) => (

            <div className="expense-item" key={expense.id}>

              <h3>{expense.title}</h3>

              <p>Category: {expense.category}</p>
              <p>Date: {expense.date}</p>

              <strong>
                ₹{Number(expense.amount).toFixed(2)}
              </strong>

              <br />

              <button
                type="button"
                onClick={() => deleteExpense(expense.id)}
              >
                Delete
              </button>

            </div>

          ))

        )}

      </div>

    </div>
  );
}

export default App;
