package Backend;

import IO.FileIO;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Faculty extends User implements FacultyDao {
    Scanner scanner = new Scanner(System.in);

    public Faculty(String userName, String password, String firstName, String midName, String lastName) {
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

    public void facultyMenu() throws Exception {
        boolean isPicked = false;
        while (!isPicked) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Make your choice?");
            System.out.println("1. View all course");
            System.out.println("2. Register teaching");
            System.out.println("3. Withdraw course");
            System.out.println("4. View all course that has register");
            System.out.println("5. Exit");
            int otp = Integer.parseInt(scanner.next());
            switch (otp) {
                case 1 -> {
                    viewAllCourse();
                }
                case 2 -> {
                    registerTeaching();
                }
                case 3 -> {
                    withdrawCourse();
                }
                case 4 -> {
                    viewAllCourseThatRegistered();
                } case 5 -> {
                    System.out.println("Goodbye, good to see you then <3");
                    isPicked = true;
                }
                default -> System.out.println("Invalid option");
            }
        }

    }

    List<Course> courseList;


    public void viewAllCourse() {
        for (var i : courseList) {
            System.out.println(i);
        }
    }


    public void registerTeaching() {
        System.out.println("Enter course name");
        String courseName = scanner.nextLine();
        System.out.println("Enter a section");
        String section = scanner.nextLine();
        String staffName = getFirstName() + getMidName() + getLastName();
        FileIO.writeFile("CourseStaff.csv", courseName + " " + section + " " + staffName + "\n");
        //courseList.add(new Course(, "default", 50, staffName, section, "default"));
    }

    public void withdrawCourse() throws Exception {
        String name = getFirstName() + getMidName() + getLastName();
        System.out.println("Please enter course name?");
        String courseName = scanner.nextLine();
        StringBuilder s = new StringBuilder();
        for (var i : courseList) {
            if (i.getCourseName().equals(courseName)) {
                courseList.remove(i);
            } else {
                s.append(i.getCourseName() + " " + i.getCourseSection() + " " + i.getCourseInstructor()).append("\n");
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

interface FacultyDao {
    public void viewAllCourse();

    public void registerTeaching() throws Exception;

    public void withdrawCourse() throws Exception;

    public void viewAllCourseThatRegistered();
}


