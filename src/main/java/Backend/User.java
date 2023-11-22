package Backend;

import java.util.Scanner;

public class User {
    Scanner scanner = new Scanner(System.in);

    private String userName;
    private String password;
    private String firstName;
    private String midName;
    private String lastName;

    public User(String userName, String password, String firstName, String midName, String lastName) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.midName = midName;
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMidName() {
        return midName;
    }

    public String getLastName() {
        return lastName;
    }

    public Course collectInformation() {
        System.out.println("Please enter information about the course");
        System.out.println("Name of course?");
        String name = scanner.nextLine();
        System.out.println("Enter course ID?");
        int id = scanner.nextInt();
        System.out.println("Max number of student can register");
        int max = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Course instructor?");
        String courseInstructor = scanner.nextLine();
        System.out.println("Course section");
        String courseSection = scanner.nextLine();
        System.out.println("Course location");
        String courseLocation = scanner.nextLine();
        return new Course(id, name, max, courseInstructor, courseSection, courseLocation);
    }


    public Student collectInformationStudent() {
        System.out.println("Please enter information about the student");
        System.out.println("User name of student?");
        String name = scanner.next();
        System.out.println("Enter student password?");
        String id = scanner.next();
        System.out.println("Enter first name?");
        String firstName = scanner.next();
        System.out.println("Enter middle name");
        String midName = scanner.next();
        System.out.println("Enter last name");
        String lastName = scanner.next();
        return new Student(name, id, firstName, midName, lastName);
    }
}
