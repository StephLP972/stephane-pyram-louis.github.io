import java.util.ArrayList;

// Exercise 3 - School Gradebook

abstract class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class Student extends Person {
    private String studentId;

    public Student(String name, String studentId) {
        super(name);
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }
}

class Course {
    // -1 means "no score yet" (missing assessment)
    public static final int MISSING = -1;
    public static final int MAX_STUDENTS = 30;

    private ArrayList<Student> students = new ArrayList<>();
    private int[][] scores = new int[MAX_STUDENTS][3];
    private double[] weights = {0.20, 0.30, 0.50};

    public boolean addStudent(Student s) {
        if (students.size() >= MAX_STUDENTS) {
            System.out.println("Course is full, cannot add " + s.getStudentId());
            return false;
        }
        int row = students.size();
        students.add(s);
        for (int i = 0; i < 3; i++) {
            scores[row][i] = MISSING;
        }
        return true;
    }

    private int findIndex(String studentId) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId().equals(studentId)) {
                return i;
            }
        }
        return -1;
    }

    // assessment is 1, 2 or 3
    public boolean setScore(String studentId, int assessment, int score) {
        int index = findIndex(studentId);
        if (index == -1) {
            System.out.println("Rejected: unknown student " + studentId);
            return false;
        }
        if (assessment < 1 || assessment > 3) {
            System.out.println("Rejected: assessment must be 1, 2 or 3");
            return false;
        }
        if (score < 0 || score > 100) {
            System.out.println("Rejected: score " + score + " for " + studentId + " (must be 0-100)");
            return false;
        }
        scores[index][assessment - 1] = score;
        return true;
    }

    public boolean setAllScores(String studentId, int s1, int s2, int s3) {
        boolean ok1 = setScore(studentId, 1, s1);
        boolean ok2 = setScore(studentId, 2, s2);
        boolean ok3 = setScore(studentId, 3, s3);
        return ok1 && ok2 && ok3;
    }

    public boolean isComplete(int index) {
        for (int i = 0; i < 3; i++) {
            if (scores[index][i] == MISSING) {
                return false;
            }
        }
        return true;
    }

    public double weightedAverage(int index) {
        double total = 0;
        for (int i = 0; i < 3; i++) {
            total = total + scores[index][i] * weights[i];
        }
        return total;
    }

    public static String letterGrade(double average) {
        if (average >= 90) return "A";
        if (average >= 80) return "B";
        if (average >= 70) return "C";
        if (average >= 60) return "D";
        return "F";
    }

    // returns null if no complete student
    public Student findTopStudent() {
        Student best = null;
        double bestAvg = -1;
        for (int i = 0; i < students.size(); i++) {
            if (!isComplete(i)) {
                continue;
            }
            double avg = weightedAverage(i);
            Student s = students.get(i);
            if (best == null || avg > bestAvg
                    || (avg == bestAvg && s.getStudentId().compareTo(best.getStudentId()) < 0)) {
                best = s;
                bestAvg = avg;
            }
        }
        return best;
    }

    public void printReport() {
        System.out.println("---------------- GRADE REPORT ----------------");
        if (students.isEmpty()) {
            System.out.println("No students in this course.");
        }
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            System.out.print(s.getStudentId() + " " + s.getName() + " scores: ");
            for (int j = 0; j < 3; j++) {
                if (scores[i][j] == MISSING) {
                    System.out.print("-- ");
                } else {
                    System.out.print(scores[i][j] + " ");
                }
            }
            if (isComplete(i)) {
                double avg = weightedAverage(i);
                System.out.printf("| average = %.2f | grade = %s%n", avg, letterGrade(avg));
            } else {
                System.out.println("| INCOMPLETE (no final grade)");
            }
        }
        Student top = findTopStudent();
        if (top == null) {
            System.out.println("Top student: none (no complete record)");
        } else {
            System.out.println("Top student: " + top.getStudentId() + " " + top.getName());
        }
        System.out.println("----------------------------------------------");
    }
}

public class GradebookApp {

    public static void main(String[] args) {
        demoExample();
        testBoundaries();
        testTieBreak();
        testEmptyAndIncomplete();
    }

    static void demoExample() {
        System.out.println("=== Demo: example from the exercise ===");
        Course course = new Course();
        course.addStudent(new Student("Alice", "S001"));
        course.addStudent(new Student("Bob", "S002"));
        course.addStudent(new Student("Chloe", "S003"));
        course.setAllScores("S001", 90, 80, 100);
        course.setAllScores("S002", 80, 90, 100);
        course.setAllScores("S003", 60, 70, 80);
        course.printReport();
    }

    static void testBoundaries() {
        System.out.println("\n=== Test: scores 0 and 100 valid, -1 and 101 invalid ===");
        Course course = new Course();
        course.addStudent(new Student("David", "S010"));
        course.addStudent(new Student("Emma", "S011"));
        course.setAllScores("S010", 0, 0, 0);
        course.setAllScores("S011", 100, 100, 100);
        course.setScore("S010", 1, -1);
        course.setScore("S011", 2, 101);
        course.printReport();
    }

    static void testTieBreak() {
        System.out.println("\n=== Test: equal averages -> smallest student ID wins ===");
        Course course = new Course();
        course.addStudent(new Student("Zoe", "S200"));
        course.addStudent(new Student("Yann", "S105"));
        course.setAllScores("S200", 85, 85, 85);
        course.setAllScores("S105", 85, 85, 85);
        course.printReport();
    }

    static void testEmptyAndIncomplete() {
        System.out.println("\n=== Test: empty course ===");
        Course empty = new Course();
        empty.printReport();

        System.out.println("\n=== Test: incomplete record ===");
        Course course = new Course();
        course.addStudent(new Student("Hugo", "S300"));
        course.setScore("S300", 1, 75);
        course.setScore("S300", 2, 88);
        course.printReport();
    }
}
