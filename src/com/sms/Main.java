package com.sms;

import com.sms.dao.StudentDAO;
import com.sms.model.Student;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private final StudentDAO studentDAO = new StudentDAO();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    public void run() {
        int choice;
        do {
            displayMenu();
            try {
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    processChoice(choice);
                } else {
                    System.out.println("❌ Invalid input. Please enter a number from the menu.");
                    scanner.nextLine(); // Consume the invalid line
                    choice = -1;
                }
            } catch (Exception e) {
                System.err.println("An unexpected error occurred. Restarting menu.");
                e.printStackTrace();
                choice = -1;
            }
        } while (choice != 0);
        System.out.println("👋 Student Management System closed. Goodbye!");
    }

    private void displayMenu() {
        System.out.println("\n==========================================");
        System.out.println("   🚀 Student Management System (JDBC) 🚀");
        System.out.println("==========================================");
        System.out.println("1. Add New Student");
        System.out.println("2. View All Students");
        System.out.println("3. Search Student by ID");
        System.out.println("4. Search Students by Name (Custom)");
        System.out.println("5. Update Student Details");
        System.out.println("6. Delete Student");
        System.out.println("0. Exit Application");
        System.out.println("==========================================");
        System.out.print("Enter your choice: ");
    }

    private void processChoice(int choice) {
        switch (choice) {
            case 1: addStudent(); break;
            case 2: viewAllStudents(); break;
            case 3: searchStudentById(); break;
            case 4: searchStudentsByName(); break;
            case 5: updateStudent(); break;
            case 6: deleteStudent(); break;
            case 0: break;
            default: System.out.println("❌ Invalid choice. Please try again.");
        }
    }

    private void addStudent() {
        try {
            System.out.println("\n--- Add New Student ---");
            System.out.print("Enter Student ID (e.g., 102): ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Age: ");
            int age = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter Address: ");
            String address = scanner.nextLine();

            Student newStudent = new Student(id, name, age, address);
            if (studentDAO.addStudent(newStudent)) {
                System.out.println("✅ Student added successfully!");
            } else {
                System.out.println("❌ Failed to add student. Check for duplicate ID or database connection.");
            }
        } catch (InputMismatchException e) {
            System.out.println("❌ Input Error: ID and Age must be numbers.");
            scanner.nextLine();
        }
    }

    private void viewAllStudents() {
        System.out.println("\n--- All Student Records ---");
        List<Student> students = studentDAO.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("ℹ️ No students found in the database.");
            return;
        }

        // Print header
        System.out.println("+-----+-----------------+------+--------------------------------+");
        System.out.println("| ID  | Name            | Age  | Address                        |");
        System.out.println("+-----+-----------------+------+--------------------------------+");
        students.forEach(System.out::println);
        System.out.println("+-----+-----------------+------+--------------------------------+");
    }

    private void searchStudentById() {
        try {
            System.out.println("\n--- Search Student by ID ---");
            System.out.print("Enter Student ID to search: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            Student student = studentDAO.getStudentById(id);
            if (student != null) {
                System.out.println("✅ Student Found:");
                System.out.println("+-----+-----------------+------+--------------------------------+");
                System.out.println("| ID  | Name            | Age  | Address                        |");
                System.out.println("+-----+-----------------+------+--------------------------------+");
                System.out.println(student);
                System.out.println("+-----+-----------------+------+--------------------------------+");
            } else {
                System.out.println("❌ Student with ID " + id + " not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("❌ Input Error: ID must be a number.");
            scanner.nextLine();
        }
    }

    private void searchStudentsByName() {
        System.out.println("\n--- Search Students by Name ---");
        System.out.print("Enter name (or part of name) to search: ");
        String nameQuery = scanner.nextLine();

        List<Student> students = studentDAO.searchStudentsByName(nameQuery);
        if (students.isEmpty()) {
            System.out.println("ℹ️ No students found matching '" + nameQuery + "'.");
            return;
        }

        System.out.println("✅ Students Found matching '" + nameQuery + "':");
        System.out.println("+-----+-----------------+------+--------------------------------+");
        System.out.println("| ID  | Name            | Age  | Address                        |");
        System.out.println("+-----+-----------------+------+--------------------------------+");
        students.forEach(System.out::println);
        System.out.println("+-----+-----------------+------+--------------------------------+");
    }

    private void updateStudent() {
        try {
            System.out.println("\n--- Update Student Details ---");
            System.out.print("Enter Student ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            Student existingStudent = studentDAO.getStudentById(id);
            if (existingStudent == null) {
                System.out.println("❌ Student with ID " + id + " not found. Cannot update.");
                return;
            }

            System.out.println("\nUpdating Student: " + existingStudent.getName() + " (ID: " + id + ")");
            System.out.print("Enter NEW Name (Current: " + existingStudent.getName() + "): ");
            String newName = scanner.nextLine();

            System.out.print("Enter NEW Age (Current: " + existingStudent.getAge() + "): ");
            int newAge = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter NEW Address (Current: " + existingStudent.getAddress() + "): ");
            String newAddress = scanner.nextLine();

            Student updatedStudent = new Student(id, newName, newAge, newAddress);

            if (studentDAO.updateStudent(updatedStudent)) {
                System.out.println("✅ Student details updated successfully!");
            } else {
                System.out.println("❌ Failed to update student.");
            }
        } catch (InputMismatchException e) {
            System.out.println("❌ Input Error: Age must be a number.");
            scanner.nextLine();
        }
    }

    private void deleteStudent() {
        try {
            System.out.println("\n--- Delete Student ---");
            System.out.print("Enter Student ID to delete: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            if (studentDAO.deleteStudent(id)) {
                System.out.println("✅ Student with ID " + id + " deleted successfully!");
            } else {
                System.out.println("❌ Failed to delete student. Check if the ID exists.");
            }
        } catch (InputMismatchException e) {
            System.out.println("❌ Input Error: ID must be a number.");
            scanner.nextLine();
        }
    }
}