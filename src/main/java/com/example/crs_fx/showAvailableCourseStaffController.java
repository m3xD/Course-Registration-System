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
import java.util.ResourceBundle;

public class showAvailableCourseStaffController implements Initializable {

    @FXML
    private Button addBntAvail;

    @FXML
    private Button goBackBnt;

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
    private TableView<Course> staffAvailTableView;

    @FXML
    private ComboBox<String> listStudent_AvailStaff;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public ObservableList<Course> staffAvailableList() {
        ObservableList<Course> list = FXCollections.observableArrayList();
        String query = "SELECT id, name, max, courseSection, courseLocation from Courses WHERE id NOT IN " +
                "(select c.id from Courses c " +
                "join register r on c.id = r.courseID " +
                "join Staffs S on r.staffID = S.id " +
                "where S.id = ?);";
        connection = Database.connect();
        try {

            preparedStatement = connection.prepareStatement(query);

            // getStaffID
            String staffID = null;
            String s = Controller.name;
            String q = "select s.id from Staffs s JOIN users on s.userID = users.id where users.username = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(q);
            preparedStatement1.setString(1, s);
            ResultSet r = preparedStatement1.executeQuery();
            if (r.next()) staffID = r.getString("s.id");

            preparedStatement.setString(1, staffID);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new Course(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getInt("max"), "none",
                        resultSet.getString("courseSection"), resultSet.getString("courseLocation")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private ObservableList<Course> courses;

    public void staffAvailShowData() {
        courses = staffAvailableList();

        staffCourseID.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        staffCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        staffMax.setCellValueFactory(new PropertyValueFactory<>("maxQuantityRegister"));
        staffCourseSec.setCellValueFactory(new PropertyValueFactory<>("courseSection"));
        staffCourseLocation.setCellValueFactory(new PropertyValueFactory<>("courseLocation"));

        staffAvailTableView.setItems(courses);
    }

    public void goBack() throws Exception {
        goBackBnt.getScene().getWindow().hide();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Staff.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void studentList() {
        List<String> list = new ArrayList<>();
        connection = Database.connect();

        String sql = "select * from Students WHERE id not in (select studentID from register join Courses on register.courseID = Courses.id where courseID = ?)";

        try {
            if (staffAvailTableView.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Please choose course");
                alert.showAndWait();
            } else {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, String.valueOf(staffAvailTableView.getSelectionModel().getSelectedItem().getCourseID()));
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    list.add(resultSet.getString("name"));
                }
                listStudent_AvailStaff.setItems(FXCollections.observableList(list));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addCourse() {

        connection = Database.connect();

        try {
            if (listStudent_AvailStaff.getSelectionModel().getSelectedItem() != null) {
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
                preparedStatement1.setString(1, listStudent_AvailStaff.getSelectionModel().getSelectedItem());
                r = preparedStatement1.executeQuery();
                if (r.next()) studentID = r.getString("id");

                q = "select id from Courses where name = ?";
                preparedStatement1 = connection.prepareStatement(q);
                preparedStatement1.setString(1, staffAvailTableView.getSelectionModel().getSelectedItem().getCourseName());
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

                staffAvailShowData();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        staffAvailShowData();

        staffAvailTableView.setRowFactory(tv -> {
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
