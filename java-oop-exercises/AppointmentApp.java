import java.util.ArrayList;

abstract class Appointment {
    private String id;
    private int startMinute;
    private int duration;

    public Appointment(String id, int startMinute, int duration) {
        if (startMinute < 0 || startMinute >= 1440 || duration <= 0 || startMinute + duration > 1440) {
            throw new IllegalArgumentException("Invalid time or duration");
        }
        this.id = id;
        this.startMinute = startMinute;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public int getEndMinute() {
        return startMinute + duration;
    }

    public abstract String getRoom();

    public abstract String description();

    public static String time(int minutes) {
        return String.format("%02d:%02d", minutes / 60, minutes % 60);
    }
}

class Consultation extends Appointment {
    private String room;

    public Consultation(String id, int startMinute, int duration, String room) {
        super(id, startMinute, duration);
        this.room = room;
    }

    public String getRoom() {
        return room;
    }

    public String description() {
        return "Consultation room " + room;
    }
}

class Workshop extends Appointment {
    private String room;
    private int capacity;

    public Workshop(String id, int startMinute, int duration, String room, int capacity) {
        super(id, startMinute, duration);
        if (capacity <= 0) {
            throw new IllegalArgumentException("Invalid capacity");
        }
        this.room = room;
        this.capacity = capacity;
    }

    public String getRoom() {
        return room;
    }

    public String description() {
        return "Workshop room " + room + " capacity " + capacity;
    }
}

class Scheduler {
    private ArrayList<Appointment> list = new ArrayList<>();

    public void addAppointment(Appointment a) {
        for (int i = 0; i < list.size(); i++) {
            Appointment b = list.get(i);
            if (b.getId().equals(a.getId())) {
                System.out.println("Rejected " + a.getId() + ": duplicate id");
                return;
            }
            if (b.getRoom().equals(a.getRoom())
                    && a.getStartMinute() < b.getEndMinute() && b.getStartMinute() < a.getEndMinute()) {
                System.out.println("Rejected " + a.getId() + ": overlap with " + b.getId());
                return;
            }
        }
        list.add(a);
        System.out.println("Added " + a.getId());
    }

    public void printSchedule() {
        ArrayList<Appointment> copy = new ArrayList<>(list);
        for (int i = 0; i < copy.size(); i++) {
            for (int j = 0; j < copy.size() - 1; j++) {
                Appointment a = copy.get(j);
                Appointment b = copy.get(j + 1);
                if (a.getStartMinute() > b.getStartMinute()
                        || (a.getStartMinute() == b.getStartMinute() && a.getId().compareTo(b.getId()) > 0)) {
                    copy.set(j, b);
                    copy.set(j + 1, a);
                }
            }
        }
        for (int i = 0; i < copy.size(); i++) {
            Appointment a = copy.get(i);
            System.out.println(Appointment.time(a.getStartMinute()) + "-" + Appointment.time(a.getEndMinute())
                    + " " + a.getId() + " " + a.description());
        }
    }
}

public class AppointmentApp {

    public static void main(String[] args) {
        Scheduler s = new Scheduler();
        test1(s);
        test2(s);
        test3();
        test4(s);
    }

    static void test1(Scheduler s) {
        System.out.println("Test 1: example");
        s.addAppointment(new Consultation("C1", 540, 60, "A"));
        s.addAppointment(new Workshop("W1", 570, 90, "A", 20));
        s.addAppointment(new Workshop("W2", 570, 90, "B", 20));
        s.addAppointment(new Consultation("C2", 600, 30, "A"));
        System.out.println();
    }

    static void test2(Scheduler s) {
        System.out.println("Test 2: duplicate id");
        s.addAppointment(new Consultation("C1", 840, 30, "C"));
        System.out.println();
    }

    static void test3() {
        System.out.println("Test 3: invalid duration and past midnight");
        try {
            new Consultation("X1", 600, 0, "A");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        try {
            new Consultation("X2", 1410, 60, "A");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println();
    }

    static void test4(Scheduler s) {
        System.out.println("Test 4: schedule in order");
        s.addAppointment(new Consultation("C3", 480, 45, "C"));
        s.printSchedule();
    }
}
