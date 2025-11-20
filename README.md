# 🚀 Student Management System (SMS) - GUI Application

A robust, graphical CRUD (Create, Read, Update, Delete) application designed to manage student records efficiently. This project demonstrates foundational Java development skills, structured programming, and seamless database connectivity.

## 🛠 Tech Stack

* **Language:** Java (JDK 8+)
* **Database:** MySQL
* **Connectivity:** Pure JDBC (Java Database Connectivity)
* **User Interface:** Java Swing (GUI)
* **Design Pattern:** Model-DAO (Data Access Object)

## ✨ Core Features

The system offers a user-friendly, multi-tabbed window interface for managing student data:

1.  **View Records:** Displays all students in a sortable table.
2.  **Add Student:** Creates new student records (ID, Name, Age, Address).
3.  **Update Record:** Loads existing student details and allows modification.
4.  **Delete Record:** Permanently removes a student record from the database.
5.  **Search Records (Custom):** Allows searching for students using a partial name match (demonstrating the SQL `LIKE` operator).

## 🌟 Customizations and Skills Demonstrated

* **GUI Implementation:** Upgraded the application from a basic console interface to a professional, multi-tabbed **Java Swing GUI**.
* **Layered Architecture:** Implemented the **Model-DAO (Data Access Object)** pattern to ensure a clean separation between the database logic (`StudentDAO.java`) and the user interface logic (`StudentGUI.java`). 
* **JDBC Proficiency:** Handled all five core JDBC steps manually, including driver loading and connection management using `try-with-resources`.
* **Troubleshooting:** Successfully resolved the critical **`ClassNotFoundException`** error by correctly linking the MySQL Connector/J JAR file to the project build path.

## 💻 Setup and Run Locally

### Prerequisites

1.  Java Development Kit (JDK 8 or higher)
2.  MySQL Server (must be running in the background, e.g., via Windows Services or XAMPP).
3.  **MySQL Connector/J JDBC Driver** (The `.jar` file must be added to the project's libraries).

### Steps

1.  **Database Creation (MySQL Workbench/CLI):**
    Run the following SQL script to create the necessary structure:

    ```sql
    CREATE DATABASE IF NOT EXISTS student_management_db;
    USE student_management_db;
    CREATE TABLE IF NOT EXISTS student (
        id INT PRIMARY KEY,
        name VARCHAR(100) NOT NULL,
        age INT,
        address VARCHAR(255)
    );
    ```

2.  **Configuration:**
    Verify and update the database credentials in the file `com/sms/util/DBConnection.java`. The default password is often blank.

    ```java
    private static final String USER = "root";       
    private static final String PASSWORD = ""; // Update this if you set a password
    ```

3.  **Run:**
    Ensure the MySQL Connector/J JAR is linked in your IDE's libraries. Then, run the **`com.sms.StudentGUI`** class from your IDE.
