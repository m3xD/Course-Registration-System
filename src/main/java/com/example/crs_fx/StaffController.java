package com.example.crs_fx;

import Backend.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class StaffController implements Initializable {


    @FXML
    private Button logOutBnt;

    @FXML
    private Label nameStaff;

    @FXML
    private Button removeStudent_Staff;

    @FXML
    private Button showAvailable_Staff;

    @FXML
    private TableColumn<Course, String> staffCourseID;

    @FXML
    private TableColumn<Course, String> staffCourseLocation;

    @FXML
    private TableColumn<Course, String> staffCourseName;

    @FXML
    private TableColumn<Course, String> staffCourseSec;

    @FXML
    private TableColumn<Course, String> staffMax;

    @FXML
    private Label staffRole;

    @FXML
    private TableColumn<Course, String> staffStudent;

    @FXML
    private TableView<Course> staffTableView;

    @FXML
    private Button addStudent_Staff;

    @FXML
    private ComboBox<String> listStudent_Staff;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public ObservableList<Course> staffList() {
        ObservableList<Course> list = FXCollections.observableArrayList();
        String query = "select c.id, c.name, c.max, S.name, c.courseSection, c.courseLocation from Courses c " +
                "JOIN register r on c.id = r.courseID " +
                "JOIN Students S on r.studentID = S.id " +
                "JOIN Staffs S2 on r.staffID = S2.id " +
                "WHERE S2.name = ?";
        connection = Database.connect();
        try {

            preparedStatement = connection.prepareStatement(query);

            String s = Controller.name;
            String q = "select s.name, users.role from Staffs s JOIN users on s.userID = users.id where users.username = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(q);
            preparedStatement1.setString(1, s);
            ResultSet r = preparedStatement1.executeQuery();
            String v = null;
            String role = null;
            if (r.next()) {
                v = r.getString("s.name");
                role = r.getString("users.role");
            }
            nameStaff.setText(v);
            staffRole.setText(role);

            preparedStatement.setString(1, v);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new Course(resultSet.getInt("c.id"), resultSet.getString("c.name"),
                        resultSet.getInt("c.max"), resultSet.getString("S.name"),
                        resultSet.getString("c.courseSection"), resultSet.getString("c.courseLocation")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void studentList() {
        List<String> list = new ArrayList<>();
        connection = Database.connect();

        String sql = "select * from Students WHERE id not in (select studentID from register join Courses on register.courseID = Courses.id where courseID = ?)";

        try {
            if (staffTableView.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Please choose course");
                alert.showAndWait();
            } else {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, String.valueOf(staffTableView.getSelectionModel().getSelectedItem().getCourseID()));
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    list.add(resultSet.getString("name"));
                }
                listStudent_Staff.setItems(FXCollections.observableList(list));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setAddStudent_Staff() {
        connection = Database.connect();

        try {
            if (listStudent_Staff.getSelectionModel().getSelectedItem() != null) {
                String staffID = null, studentID = null, courseID = null;
                String sql = "insert into register(staffID, studentID, courseID, adminID) value (?, ?, ?, 1)";

                String s = Controller.name;
                String q = "select s.id from Staffs s JOIN users on s.userID = users.id where users.username = ?";
                PreparedStatement preparedStatement1 = connection.prepareStatement(q);
                preparedStatement1.setString(1, s);
                ResultSet r = preparedStatement1.executeQuery();
                if (r.next()) staffID = r.getString("s.id");

                q = "select id from Students where name = ?";
                preparedStatement1 = connection.prepareStatement(q);
                preparedStatement1.setString(1, listStudent_Staff.getSelectionModel().getSelectedItem());
                r = preparedStatement1.executeQuery();
                if (r.next()) studentID = r.getString("id");

                q = "select id from Courses where name = ?";
                preparedStatement1 = connection.prepareStatement(q);
                preparedStatement1.setString(1, staffTableView.getSelectionModel().getSelectedItem().getCourseName());
                r = preparedStatement1.executeQuery();
                if (r.next()) courseID = r.getString("id");

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, staffID);
                preparedStatement.setString(2, studentID);
                preparedStatement.setString(3, courseID);
                preparedStatement.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information message");
                alert.setHeaderText(null);
                alert.setContentText("Add course successfully");
                alert.showAndWait();

                staffShowData();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Please choose student enroll this course");
                alert.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ObservableList<Course> courses;

    public void staffShowData() {
        courses = staffList();

        staffCourseID.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        staffCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        staffMax.setCellValueFactory(new PropertyValueFactory<>("maxQuantityRegister"));
        staffStudent.setCellValueFactory(new PropertyValueFactory<>("courseInstructor"));
        staffCourseSec.setCellValueFactory(new PropertyValueFactory<>("courseSection"));
        staffCourseLocation.setCellValueFactory(new PropertyValueFactory<>("courseLocation"));

        staffTableView.setItems(courses);
    }

    public void setRemoveStudent_Staff() {
        connection = Database.connect();

        try {
            String sql = "DELETE FROM register WHERE studentID = (SELECT id FROM Students WHERE name = ?) " +
                    "AND courseID = (SELECT id FROM Courses WHERE name = ?)";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure to remove this student?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Course c = staffTableView.getSelectionModel().getSelectedItem();
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, c.getCourseInstructor());
                preparedStatement.setString(2, c.getCourseName());

                preparedStatement.executeUpdate();
                staffShowData();
            } else {
                System.out.println("User choose cancel");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAvailableCourse() throws Exception {
        showAvailable_Staff.getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("availableCourseStaff.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }

    public void logOut() throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to log out?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            logOutBnt.getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("User choose cancel");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        staffShowData();

        staffTableView.setRowFactory(tv -> {
            TableRow<Course> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY && !row.isEmpty()) {
                    studentList();
                }
            });
            return row;
        });
    }
}
