package Backend;

import IO.FileIO;
import IO.ObjectIO;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Student extends User implements StudentDao {

    public Student(String userName, String password, String firstName, String midName, String lastName) {
        super(userName, password, firstName, midName, lastName);
        courseList = new ArrayList<>();
    }

    @Override
    public String getUserName() {
        return super.getUserName();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getFirstName() {
        return super.getFirstName();
    }

    @Override
    public String getMidName() {
        return super.getMidName();
    }

    @Override
    public String getLastName() {
        return super.getLastName();
    }

    public void studentMenu() throws Exception {
        boolean isPicked = false;
        while (!isPicked) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Make your choice?");
            System.out.println("1. View all course");
            System.out.println("2. View course that are not full");
            System.out.println("3. Register course");
            System.out.println("4. Withdraw course");
            System.out.println("5. View all course that has register");
            System.out.println("6. Exit");
            int otp = Integer.parseInt(scanner.next());
            switch (otp) {
                case 1 -> {
                    viewAllCourse();
                }
                case 2 -> {
                    coursesNotFull();
                }
                case 3 -> {
                    registerCourse();
                }
                case 4 -> {
                    withdrawCourse();
                }
                case 5 -> {
                    viewAllCourseThatRegistered();
                } case 6 -> {
                    System.out.println("Goodbye, hope you don't fail on your course!");
                    isPicked = true;
                }
                default -> System.out.println("Invalid option");
            }
        }

    }

    List<Course> courseList;


    public void viewAllCourse() {
        List<Course> courses = (List<Course>) ObjectIO.ReadObjectFromFile("courseArray.txt");
        for (var i : courses) {
            System.out.println(i);
        }
    }

    public void coursesNotFull() {
        List<Course> courses = (List<Course>) ObjectIO.ReadObjectFromFile("courseArray.txt");
        for (var i : courses) {
            if (i.getMaxQuantityRegister() > i.getNameOfStudent().size()) {
                System.out.println(i);
            }
        }
    }

    public void registerCourse() throws Exception {
        System.out.println("Enter course name");
        String courseName = scanner.nextLine();
        String studentName = getFirstName() + getMidName() + getLastName();
        FileIO.writeFile("CourseStudent.csv", courseName + " " + studentName + "\n");
        courseList.add(new Course(1, courseName, 50, studentName, "default", "default"));
    }

    public void withdrawCourse() throws Exception {
        System.out.println("Please enter course name?");
        String courseName = scanner.nextLine();
        StringBuilder s = new StringBuilder();
        for (var i : courseList) {
            if (i.getCourseName().equals(courseName)) {
                courseList.remove(i);
            } else {
                s.append(i.getCourseName() + " " + i.getCourseSection() + " " + (getFirstName() + getMidName() + getLastName())).append("\n");
            }
        }
        new FileWriter("Course.csv", false).close();
        FileIO.writeFile("Course.csv", s.toString());
    }

    public void viewAllCourseThatRegistered() {
        for (var i : courseList) {
            System.out.println(i);
        }
    }
}

interface StudentDao {
    public void viewAllCourse();

    public void coursesNotFull();

    public void registerCourse() throws Exception;

    public void withdrawCourse() throws Exception;

    public void viewAllCourseThatRegistered();
}