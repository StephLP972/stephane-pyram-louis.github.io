import java.util.ArrayList;

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
    private ArrayList<Student> students = new ArrayList<>();
    private int[][] scores = new int[30][3];
    private double[] weights = {0.20, 0.30, 0.50};

    // a score of -1 means the assessment is missing
    public void addStudent(Student s) {
        int row = students.size();
        students.add(s);
        for (int i = 0; i < 3; i++) {
            scores[row][i] = -1;
        }
    }

    public void setScore(String studentId, int assessment, int score) {
        if (score < 0 || score > 100) {
            System.out.println("Invalid score: " + score);
            return;
        }
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId().equals(studentId)) {
                scores[i][assessment - 1] = score;
                return;
            }
        }
        System.out.println("Student not found: " + studentId);
    }

    public void setScores(String studentId, int s1, int s2, int s3) {
        setScore(studentId, 1, s1);
        setScore(studentId, 2, s2);
        setScore(studentId, 3, s3);
    }

    public boolean isComplete(int index) {
        for (int i = 0; i < 3; i++) {
            if (scores[index][i] == -1) {
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

    public String letterGrade(double avg) {
        if (avg >= 90) {
            return "A";
        } else if (avg >= 80) {
            return "B";
        } else if (avg >= 70) {
            return "C";
        } else if (avg >= 60) {
            return "D";
        } else {
            return "F";
        }
    }

    public Student getTopStudent() {
        Student best = null;
        double bestAvg = 0;
        for (int i = 0; i < students.size(); i++) {
            if (isComplete(i)) {
                double avg = weightedAverage(i);
                Student s = students.get(i);
                if (best == null || avg > bestAvg) {
                    best = s;
                    bestAvg = avg;
                } else if (avg == bestAvg && s.getStudentId().compareTo(best.getStudentId()) < 0) {
                    best = s;
                }
            }
        }
        return best;
    }

    public void printReport() {
        if (students.size() == 0) {
            System.out.println("No students");
            return;
        }
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            if (isComplete(i)) {
                double avg = weightedAverage(i);
                System.out.println(s.getStudentId() + " " + s.getName() + " average: " + String.format("%.2f", avg) + " grade: " + letterGrade(avg));
            } else {
                System.out.println(s.getStudentId() + " " + s.getName() + " incomplete");
            }
        }
        Student top = getTopStudent();
        if (top == null) {
            System.out.println("Top student: none");
        } else {
            System.out.println("Top student: " + top.getStudentId() + " " + top.getName());
        }
    }
}

public class GradebookApp {

    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
    }

    static void test1() {
        System.out.println("Test 1: example");
        Course c = new Course();
        c.addStudent(new Student("Alice", "S1"));
        c.addStudent(new Student("Bob", "S2"));
        c.addStudent(new Student("Chloe", "S3"));
        c.setScores("S1", 90, 80, 100);
        c.setScores("S2", 80, 90, 100);
        c.setScores("S3", 60, 70, 80);
        c.printReport();
        System.out.println();
    }

    static void test2() {
        System.out.println("Test 2: scores 0, 100, -1, 101");
        Course c = new Course();
        c.addStudent(new Student("David", "S4"));
        c.addStudent(new Student("Emma", "S5"));
        c.setScores("S4", 0, 0, 0);
        c.setScores("S5", 100, 100, 100);
        c.setScore("S4", 1, -1);
        c.setScore("S5", 1, 101);
        c.printReport();
        System.out.println();
    }

    static void test3() {
        System.out.println("Test 3: same average");
        Course c = new Course();
        c.addStudent(new Student("Zoe", "S9"));
        c.addStudent(new Student("Yann", "S7"));
        c.setScores("S9", 85, 85, 85);
        c.setScores("S7", 85, 85, 85);
        c.printReport();
        System.out.println();
    }

    static void test4() {
        System.out.println("Test 4: empty and incomplete");
        Course empty = new Course();
        empty.printReport();
        Course c = new Course();
        c.addStudent(new Student("Hugo", "S8"));
        c.setScore("S8", 1, 75);
        c.printReport();
    }
}
