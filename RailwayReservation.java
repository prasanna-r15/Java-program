import java.util.*;

public class RailwayReservation {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        RailwaySystem railwaySystem = new RailwaySystem();

        while (true) {
            System.out.println("""
                    Modules:
                     1. Book Ticket
                     2. Check Availability
                     3. Cancel Ticket
                     4. Prepare Chart
                     5. Exit
                    """);
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> railwaySystem.bookTicket(sc);
                case 2 -> railwaySystem.checkAvailability(sc);
                case 3 -> railwaySystem.cancelTicket(sc);
                case 4 -> railwaySystem.prepareChart();
                case 5 -> {
                    System.out.println("Exiting... Thank you for using the Railway Reservation System.");
                    return;
                }
                default -> System.out.println("Invalid input. Please try again.");
            }
        }
    }
}

// Main Railway System Class
class RailwaySystem {
    private final Map<String, SeatManager> seatManagers = new HashMap<>();

    public RailwaySystem() {
        // Initialize seat managers for each type
        seatManagers.put("AC Coach", new SeatManager(60, 10));
        seatManagers.put("Non-AC Coach", new SeatManager(60, 10));
        seatManagers.put("Seater", new SeatManager(60, 10));
    }

    public void bookTicket(Scanner sc) {
        System.out.println("Available Seat Types: AC Coach, Non-AC Coach, Seater");
        System.out.println("Enter Seat Type:");
        String seatType = sc.nextLine();

        SeatManager manager = seatManagers.get(seatType);
        if (manager == null) {
            System.out.println("Invalid seat type. Please try again.");
            return;
        }

        System.out.println("Enter Passenger Name:");
        String passengerName = sc.nextLine();

        System.out.println("Enter Age:");
        int age = sc.nextInt();
        sc.nextLine();

        String status = manager.bookSeat(passengerName, age);
        System.out.println(status);
    }

    public void checkAvailability(Scanner sc) {
        System.out.println("Available Seat Types: AC Coach, Non-AC Coach, Seater");
        System.out.println("Enter Seat Type:");
        String seatType = sc.nextLine();

        SeatManager manager = seatManagers.get(seatType);
        if (manager == null) {
            System.out.println("Invalid seat type. Please try again.");
            return;
        }

        System.out.println("Available Seats: " + manager.getAvailableSeats());
        System.out.println("Waiting List: " + manager.getWaitingListSize());
    }

    public void cancelTicket(Scanner sc) {
        System.out.println("Available Seat Types: AC Coach, Non-AC Coach, Seater");
        System.out.println("Enter Seat Type:");
        String seatType = sc.nextLine();

        SeatManager manager = seatManagers.get(seatType);
        if (manager == null) {
            System.out.println("Invalid seat type. Please try again.");
            return;
        }

        System.out.println("Enter Passenger Name:");
        String passengerName = sc.nextLine();

        String status = manager.cancelBooking(passengerName);
        System.out.println(status);
    }

    public void prepareChart() {
        System.out.println("Reservation Chart:");
        for (String seatType : seatManagers.keySet()) {
            System.out.println("Seat Type: " + seatType);
            seatManagers.get(seatType).displayChart();
            System.out.println("----------------------------------------");
        }
    }
}

// Seat Manager Class
class SeatManager {
    private final int capacity;
    private final int waitingListCapacity;
    private final List<Passenger> bookedSeats = new ArrayList<>();
    private final Queue<Passenger> waitingList = new LinkedList<>();

    public SeatManager(int capacity, int waitingListCapacity) {
        this.capacity = capacity;
        this.waitingListCapacity = waitingListCapacity;
    }

    public String bookSeat(String name, int age) {
        if (bookedSeats.size() < capacity) {
            Passenger passenger = new Passenger(name, age, "Confirmed");
            bookedSeats.add(passenger);
            return "Booking Confirmed for " + name;
        } else if (waitingList.size() < waitingListCapacity) {
            Passenger passenger = new Passenger(name, age, "Waiting");
            waitingList.add(passenger);
            return "Added to Waiting List for " + name;
        } else {
            return "No seats available. Booking failed for " + name;
        }
    }

    public String cancelBooking(String name) {
        // Check in booked seats
        for (Passenger passenger : bookedSeats) {
            if (passenger.name.equalsIgnoreCase(name)) {
                bookedSeats.remove(passenger);

                // Promote the first in the waiting list
                if (!waitingList.isEmpty()) {
                    Passenger waitingPassenger = waitingList.poll();
                    waitingPassenger.status = "Confirmed";
                    bookedSeats.add(waitingPassenger);
                }

                return "Booking cancelled for " + name;
            }
        }

        // Check in waiting list
        for (Passenger passenger : waitingList) {
            if (passenger.name.equalsIgnoreCase(name)) {
                waitingList.remove(passenger);
                return "Booking cancelled from waiting list for " + name;
            }
        }

        return "No booking found for " + name;
    }

    public int getAvailableSeats() {
        return capacity - bookedSeats.size();
    }

    public int getWaitingListSize() {
        return waitingList.size();
    }

    public void displayChart() {
        System.out.println("Confirmed Bookings:");
        for (Passenger passenger : bookedSeats) {
            System.out.println(passenger);
        }

        System.out.println("Waiting List:");
        for (Passenger passenger : waitingList) {
            System.out.println(passenger);
        }
    }
}

// Passenger Class
class Passenger {
    String name;
    int age;
    String status;

    public Passenger(String name, int age, String status) {
        this.name = name;
        this.age = age;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Passenger{name='" + name + "', age=" + age + ", status='" + status + "'}";
    }
}
