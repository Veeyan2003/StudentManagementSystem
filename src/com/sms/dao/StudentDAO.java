package com.sms.dao;

import com.sms.model.Student;
import com.sms.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    private static final String INSERT_STUDENT = "INSERT INTO student (id, name, age, address) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_STUDENTS = "SELECT * FROM student ORDER BY id";
    private static final String SELECT_STUDENT_BY_ID = "SELECT * FROM student WHERE id = ?";
    private static final String UPDATE_STUDENT = "UPDATE student SET name = ?, age = ?, address = ? WHERE id = ?";
    private static final String DELETE_STUDENT = "DELETE FROM student WHERE id = ?";
    private static final String SEARCH_BY_NAME = "SELECT * FROM student WHERE name LIKE ?";

    public boolean addStudent(Student student) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_STUDENT)) {

            // Set the values for the '?' placeholders
            ps.setInt(1, student.getId());
            ps.setString(2, student.getName());
            ps.setInt(3, student.getAge());
            ps.setString(4, student.getAddress());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Database error during ADD operation: " + e.getMessage());
            if (e.getErrorCode() == 1062) {
                System.err.println("Error: Student ID " + student.getId() + " already exists.");
            }
            return false;
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL_STUDENTS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("address")
                );
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Database error during VIEW ALL operation: " + e.getMessage());
        }
        return students;
    }

    public Student getStudentById(int id) {
        Student student = null;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_STUDENT_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    student = new Student(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getString("address")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during SEARCH BY ID operation: " + e.getMessage());
        }
        return student;
    }

    public List<Student> searchStudentsByName(String nameQuery) {
        List<Student> students = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SEARCH_BY_NAME)) {

            ps.setString(1, "%" + nameQuery + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getString("address")
                    );
                    students.add(student);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during SEARCH BY NAME operation: " + e.getMessage());
        }
        return students;
    }

    public boolean updateStudent(Student student) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_STUDENT)) {

            ps.setString(1, student.getName());
            ps.setInt(2, student.getAge());
            ps.setString(3, student.getAddress());
            ps.setInt(4, student.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Database error during UPDATE operation: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteStudent(int id) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_STUDENT)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Database error during DELETE operation: " + e.getMessage());
            return false;
        }
    }
}