# Personal Expense Tracker

A lightweight, robust, and offline desktop application developed in Java using the **Java Swing** framework for the GUI and **Java File I/O** for local flat-file storage data persistence. This application allows individuals to effectively track their income, monitor categorical expense outflows, and check real-time cash positions to prevent overspending.

---

## 👤 Student Information
- **Student Name:** Alina Farooq
- **Roll Number:** L1F23BSSE0361
- **Course:** BS Software Engineering (BSSE)
- **Subject:** Software Construction & Development

---

## 🚀 Key Features
- **Interactive Dashboard:** View real-time computing updates of overall net cash holdings balance.
- **Transaction Logger:** Seamless entry forms to track both incoming cash streams and outgoing expenditure records.
- **Categorized Management:** Group financials by custom types/categories for clearer tracking.
- **Tabular History Grid:** View all previously logged financial transactions in a neat, responsive `JTable` layout.
- **Input Validation Security:** Front-end logic handles invalid data types or negative amounts to prevent bad entries.
- **Persistent Flat-File Architecture:** Automatically reads and appends data streams securely to a localized `expenses.txt` plain text storage without requiring heavy database dependencies.

---

## 🏗️ System Architecture & OOP Concepts
This system strictly reflects clean coding practices and maps the main Object-Oriented Programming (OOP) requirements of the course:

1. **Abstraction:** Implemented via the abstract base class `Transaction.java`, hiding internal business logic and exposing only necessary interfaces.
2. **Inheritance:** Specialized entities `Income.java` and `Expense.java` cleanly extend structural fields directly out of the parent `Transaction` base class.
3. **Encapsulation:** All internal transactional attributes are marked `protected` or `private` with explicit exposure handling done through public Getter/Setter accessors.
4. **Polymorphism:** Realized dynamically through method overriding where subclasses provide their own unique implementation for the abstract method `getTransactionType()`.

---

## 📁 Repository Directory Structure

```text
expense-tracker/
│
├── src/
│   ├── model/
│   │   ├── Transaction.java     # Abstract Parent Class
│   │   ├── Income.java          # Child Class (Inheritance)
│   │   └── Expense.java         # Child Class (Inheritance)
│   │
│   ├── database/
│   │   └── FileHandler.java     # Flat File I/O Parsing Engine
│   │
│   └── ui/
│       └── Dashboard.java       # Swing Main UI Window Layout
│
├── expenses.txt                 # Auto-generated runtime data persistence
└── README.md                    # System documentation