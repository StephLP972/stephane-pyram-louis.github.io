import java.util.ArrayList;

// Exercise 7 - Appointment Scheduling System

abstract class Appointment {
    private String id;
    private int startMinute;
    private int duration;

    public Appointment(String id, int startMinute, int duration) {
        if (startMinute < 0 || startMinute >= 1440) {
            throw new IllegalArgumentException("Start time must be between 00:00 and 23:59");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be > 0");
        }
        if (startMinute + duration > 1440) {
            throw new IllegalArgumentException("Appointment cannot go past midnight");
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

    public int getDuration() {
        return duration;
    }

    public int getEndMinute() {
        return startMinute + duration;
    }

    public abstract String getRoom();

    public abstract String description();

    // 570 -> "09:30"
    public static String formatTime(int minutes) {
        return String.format("%02d:%02d", minutes / 60, minutes % 60);
    }

    // "09:30" -> 570
    public static int toMinutes(int hours, int minutes) {
        return hours * 60 + minutes;
    }
}

class Consultation extends Appointment {
    private String room;

    public Consultation(String id, int startMinute, int duration, String room) {
        super(id, startMinute, duration);
        this.room = room;
    }

    @Override
    public String getRoom() {
        return room;
    }

    @Override
    public String description() {
        return "Consultation in room " + room;
    }
}

class Workshop extends Appointment {
    private String room;
    private int maxCapacity;

    public Workshop(String id, int startMinute, int duration, String room, int maxCapacity) {
        super(id, startMinute, duration);
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("Workshop capacity must be positive");
        }
        this.room = room;
        this.maxCapacity = maxCapacity;
    }

    @Override
    public String getRoom() {
        return room;
    }

    @Override
    public String description() {
        return "Workshop in room " + room + " (max " + maxCapacity + " people)";
    }
}

class Scheduler {
    private ArrayList<Appointment> appointments = new ArrayList<>();

    public boolean addAppointment(Appointment a) {
        for (int i = 0; i < appointments.size(); i++) {
            Appointment other = appointments.get(i);
            if (other.getId().equals(a.getId())) {
                System.out.println("Rejected " + a.getId() + ": duplicate ID");
                return false;
            }
            boolean sameRoom = other.getRoom().equals(a.getRoom());
            boolean overlap = a.getStartMinute() < other.getEndMinute()
                    && other.getStartMinute() < a.getEndMinute();
            if (sameRoom && overlap) {
                System.out.println("Rejected " + a.getId() + ": overlaps " + other.getId()
                        + " in room " + a.getRoom());
                return false;
            }
        }
        appointments.add(a);
        System.out.println("Added " + a.getId() + " " + Appointment.formatTime(a.getStartMinute())
                + "-" + Appointment.formatTime(a.getEndMinute()) + " room " + a.getRoom());
        return true;
    }

    // sorts a copy, so the original list keeps insertion order
    public ArrayList<Appointment> getSortedCopy() {
        ArrayList<Appointment> copy = new ArrayList<>(appointments);
        for (int i = 0; i < copy.size() - 1; i++) {
            for (int j = 0; j < copy.size() - 1 - i; j++) {
                Appointment a = copy.get(j);
                Appointment b = copy.get(j + 1);
                boolean swap = a.getStartMinute() > b.getStartMinute()
                        || (a.getStartMinute() == b.getStartMinute() && a.getId().compareTo(b.getId()) > 0);
                if (swap) {
                    copy.set(j, b);
                    copy.set(j + 1, a);
                }
            }
        }
        return copy;
    }

    public void printSchedule() {
        System.out.println("------------- SCHEDULE (chronological) -------------");
        ArrayList<Appointment> sorted = getSortedCopy();
        if (sorted.isEmpty()) {
            System.out.println("No appointments.");
        }
        for (int i = 0; i < sorted.size(); i++) {
            Appointment a = sorted.get(i);
            System.out.println(Appointment.formatTime(a.getStartMinute()) + "-"
                    + Appointment.formatTime(a.getEndMinute()) + "  " + a.getId() + "  " + a.description());
        }
        System.out.println("----------------------------------------------------");
    }

    public void printInsertionOrder() {
        System.out.print("Insertion order: ");
        for (int i = 0; i < appointments.size(); i++) {
            System.out.print(appointments.get(i).getId() + " ");
        }
        System.out.println();
    }
}

public class AppointmentApp {

    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();
        demoExample(scheduler);
        testDuplicateId(scheduler);
        testInvalidTimes();
        testChronologicalListing(scheduler);
    }

    static void demoExample(Scheduler scheduler) {
        System.out.println("=== Demo: example from the exercise ===");
        // Room A: Consultation 09:00-10:00
        scheduler.addAppointment(new Consultation("C1", Appointment.toMinutes(9, 0), 60, "A"));
        // Room A: Workshop 09:30-11:00 -> must be rejected (same room overlap)
        scheduler.addAppointment(new Workshop("W1", Appointment.toMinutes(9, 30), 90, "A", 20));
        // Room B: Workshop 09:30-11:00 -> allowed (different room)
        scheduler.addAppointment(new Workshop("W2", Appointment.toMinutes(9, 30), 90, "B", 20));
        // Room A: Consultation 10:00-10:30 -> allowed (touches 10:00 end, no overlap)
        scheduler.addAppointment(new Consultation("C2", Appointment.toMinutes(10, 0), 30, "A"));
    }

    static void testDuplicateId(Scheduler scheduler) {
        System.out.println("\n=== Test: duplicate ID ===");
        scheduler.addAppointment(new Consultation("C1", Appointment.toMinutes(14, 0), 30, "C"));
    }

    static void testInvalidTimes() {
        System.out.println("\n=== Test: invalid duration / time past midnight ===");
        tryCreate("X1", Appointment.toMinutes(10, 0), 0, "A");      // duration 0
        tryCreate("X2", Appointment.toMinutes(10, 0), -15, "A");    // negative duration
        tryCreate("X3", Appointment.toMinutes(23, 30), 60, "A");    // ends after midnight
        tryCreate("X4", 1440, 10, "A");                             // start = 24:00
        try {
            new Workshop("X5", Appointment.toMinutes(8, 0), 60, "B", 0);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected X5: " + e.getMessage());
        }
        // edge case that is valid: ends exactly at midnight
        tryCreate("X6", Appointment.toMinutes(23, 0), 60, "A");
    }

    static void tryCreate(String id, int start, int duration, String room) {
        try {
            Consultation c = new Consultation(id, start, duration, room);
            System.out.println("Created " + id + ": " + Appointment.formatTime(c.getStartMinute())
                    + "-" + Appointment.formatTime(c.getEndMinute()));
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected " + id + ": " + e.getMessage());
        }
    }

    static void testChronologicalListing(Scheduler scheduler) {
        System.out.println("\n=== Test: chronological listing ===");
        scheduler.addAppointment(new Consultation("C0", Appointment.toMinutes(8, 0), 45, "C"));
        scheduler.addAppointment(new Consultation("B9", Appointment.toMinutes(9, 30), 30, "C")); // same start as W2 -> tie by ID
        scheduler.printInsertionOrder();
        scheduler.printSchedule();
    }
}
