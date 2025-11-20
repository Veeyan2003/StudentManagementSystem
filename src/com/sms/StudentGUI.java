package com.sms;

import com.sms.dao.StudentDAO;
import com.sms.model.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class StudentGUI extends JFrame {

    private final StudentDAO studentDAO = new StudentDAO();
    private final DefaultTableModel tableModel;
    private final JTable studentTable;
    private final JTabbedPane tabbedPane;

    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 12);
    private static final Color PRIMARY_COLOR = new Color(50, 150, 250);
    private static final Color SECONDARY_COLOR = new Color(240, 240, 240);

    private JTextField idField, nameField, ageField, addressField;
    private JTextField updateIdField, updateNameField, updateAgeField, updateAddressField;
    private JTextField deleteIdField;
    private JTextField searchNameField;

    public StudentGUI() {
        setTitle("Student Management System (Java JDBC)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Name", "Age", "Address"};
        tableModel = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(tableModel);

        studentTable.setFont(LABEL_FONT);
        studentTable.getTableHeader().setFont(HEADER_FONT);
        studentTable.getTableHeader().setBackground(PRIMARY_COLOR);
        studentTable.getTableHeader().setForeground(Color.WHITE);
        studentTable.setRowHeight(25);
        studentTable.setGridColor(SECONDARY_COLOR);
        studentTable.setSelectionBackground(new Color(190, 225, 255));

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(HEADER_FONT);
        tabbedPane.setBackground(SECONDARY_COLOR);
        tabbedPane.setForeground(new Color(50, 50, 50));

        tabbedPane.addTab("View Records", createViewPanel());
        tabbedPane.addTab("Add New Student", createAddPanel());
        tabbedPane.addTab("Update Record", createUpdatePanel());
        tabbedPane.addTab("Delete Record", createDeletePanel());
        tabbedPane.addTab("Search Records", createSearchPanel());

        add(tabbedPane);

        loadStudentData(null);

        setVisible(true);
    }

    private JPanel createViewPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(SECONDARY_COLOR);

        JLabel title = new JLabel("All Current Student Records");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(PRIMARY_COLOR);
        panel.add(title, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.setFont(HEADER_FONT);
        refreshButton.setBackground(PRIMARY_COLOR);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(e -> loadStudentData(null));

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBackground(SECONDARY_COLOR);
        southPanel.add(refreshButton);
        panel.add(southPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAddPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));

        JPanel form = createFormPanel("Enter New Student Details");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        idField = new JTextField(20);
        nameField = new JTextField(20);
        ageField = new JTextField(20);
        addressField = new JTextField(20);

        addItem(form, new JLabel("Student ID (Unique):"), idField, 0, 0);
        addItem(form, new JLabel("Name:"), nameField, 1, 0);
        addItem(form, new JLabel("Age:"), ageField, 2, 0);
        addItem(form, new JLabel("Address:"), addressField, 3, 0);

        JButton addButton = new JButton("Add Student to Database");
        addButton.setFont(HEADER_FONT);
        addButton.setBackground(PRIMARY_COLOR);
        addButton.setForeground(Color.WHITE);
        addButton.setPreferredSize(new Dimension(250, 40));
        addButton.addActionListener(this::handleAddAction);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(form, gbc);

        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(addButton, gbc);

        return panel;
    }

    private JPanel createUpdatePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));

        JPanel form = createFormPanel("Update Existing Student Record");
        updateIdField = new JTextField(20);
        updateNameField = new JTextField(20);
        updateAgeField = new JTextField(20);
        updateAddressField = new JTextField(20);

        JButton loadButton = new JButton("Load Details by ID");
        loadButton.setFont(LABEL_FONT);
        loadButton.setBackground(new Color(255, 180, 0));
        loadButton.setForeground(Color.BLACK);
        loadButton.addActionListener(this::handleLoadForUpdate);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        addItem(form, new JLabel("Student ID to Update:"), updateIdField, 0, 0);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        form.add(loadButton, gbc);

        addItem(form, new JLabel("New Name:"), updateNameField, 1, 0);
        addItem(form, new JLabel("New Age:"), updateAgeField, 2, 0);
        addItem(form, new JLabel("New Address:"), updateAddressField, 3, 0);

        JButton updateButton = new JButton("Apply Update");
        updateButton.setFont(HEADER_FONT);
        updateButton.setBackground(PRIMARY_COLOR);
        updateButton.setForeground(Color.WHITE);
        updateButton.setPreferredSize(new Dimension(250, 40));
        updateButton.addActionListener(this::handleUpdateAction);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(form, gbc);

        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(updateButton, gbc);

        return panel;
    }

    private JPanel createDeletePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));

        JPanel form = createFormPanel("Delete Student Record");
        deleteIdField = new JTextField(20);

        addItem(form, new JLabel("Student ID to Delete:"), deleteIdField, 0, 0);

        JButton deleteButton = new JButton("Permanently Delete Record");
        deleteButton.setFont(HEADER_FONT);
        deleteButton.setBackground(new Color(255, 60, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setPreferredSize(new Dimension(250, 40));
        deleteButton.addActionListener(this::handleDeleteAction);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(form, gbc);

        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(deleteButton, gbc);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(SECONDARY_COLOR);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        topPanel.setBackground(SECONDARY_COLOR);

        searchNameField = new JTextField(20);
        searchNameField.setFont(LABEL_FONT);

        JButton searchButton = new JButton("Search by Name");
        searchButton.setFont(LABEL_FONT);
        searchButton.setBackground(new Color(0, 170, 0));
        searchButton.setForeground(Color.WHITE);
        searchButton.addActionListener(this::handleSearchAction);

        JLabel searchLabel = new JLabel("Enter Name (Partial or Full):");
        searchLabel.setFont(HEADER_FONT); // Set font directly on creation

        topPanel.add(searchLabel);
        topPanel.add(searchNameField);
        topPanel.add(searchButton);

        panel.add(topPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFormPanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                title,
                0, 0,
                HEADER_FONT, PRIMARY_COLOR
        ));
        return panel;
    }

    private void addItem(JPanel panel, JComponent label, JComponent field, int y, int xOffset) {
        // Set the font for the JLabel to ensure the correct style is applied
        if (label instanceof JLabel) {
            label.setFont(LABEL_FONT);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0 + xOffset;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(label, gbc);

        gbc.gridx = 1 + xOffset;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void loadStudentData(List<Student> students) {
        List<Student> data = (students != null) ? students : studentDAO.getAllStudents();

        tableModel.setRowCount(0);

        for (Student student : data) {
            tableModel.addRow(new Object[]{
                    student.getId(),
                    student.getName(),
                    student.getAge(),
                    student.getAddress()
            });
        }
    }

    private void handleAddAction(ActionEvent e) {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String address = addressField.getText();

            Student newStudent = new Student(id, name, age, address);
            if (studentDAO.addStudent(newStudent)) {
                JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadStudentData(null);
                idField.setText("");
                nameField.setText("");
                ageField.setText("");
                addressField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add student. ID might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID and Age must be valid numbers.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleLoadForUpdate(ActionEvent e) {
        try {
            int id = Integer.parseInt(updateIdField.getText());
            Student student = studentDAO.getStudentById(id);
            if (student != null) {
                updateNameField.setText(student.getName());
                updateAgeField.setText(String.valueOf(student.getAge()));
                updateAddressField.setText(student.getAddress());
            } else {
                JOptionPane.showMessageDialog(this, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
                updateNameField.setText("");
                updateAgeField.setText("");
                updateAddressField.setText("");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID must be a valid number.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleUpdateAction(ActionEvent e) {
        try {
            int id = Integer.parseInt(updateIdField.getText());
            String name = updateNameField.getText();
            int age = Integer.parseInt(updateAgeField.getText());
            String address = updateAddressField.getText();

            Student updatedStudent = new Student(id, name, age, address);

            if (studentDAO.updateStudent(updatedStudent)) {
                JOptionPane.showMessageDialog(this, "Student details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadStudentData(null);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update student. ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID and Age must be valid numbers.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleDeleteAction(ActionEvent e) {
        try {
            int id = Integer.parseInt(deleteIdField.getText());
            if (studentDAO.deleteStudent(id)) {
                JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadStudentData(null);
                deleteIdField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete student. ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID must be a valid number.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleSearchAction(ActionEvent e) {
        String nameQuery = searchNameField.getText();

        List<Student> results = studentDAO.searchStudentsByName(nameQuery);
        loadStudentData(results);

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students found matching '" + nameQuery + "'.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }

        tabbedPane.setSelectedIndex(0);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentGUI::new);
    }
}
