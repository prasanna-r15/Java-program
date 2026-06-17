import java.util.*;
import java.util.stream.Collectors;

class TaxiBooking {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Location> lList = new ArrayList<>();
        List<Customer> cList = new ArrayList<>();
        List<BookingDetail> bList = new ArrayList<>();
        List<Taxi> tList = new ArrayList<>();
        char[] location = { 'A', 'B', 'C', 'D', 'E', 'F' };
        int[] taxi = { 1, 2, 3, 4 };

        for (char c : location) {
            lList.add(new Location(c));
        }
        for (int i : taxi) {
            Location cLocation = lList.get(0);
            tList.add(new Taxi(i, cLocation));
        }

        while (true) {
            System.out.println("1) Call taxi booking \n2) Display the Taxi details \n3) Exit");
            int type = sc.nextInt();
            if (type == 1) {
                System.out.println("Customer ID:");
                int customerId = sc.nextInt();
                System.out.println("Pickup Point:");
                char pick = sc.next().charAt(0);
                System.out.println("Drop Point:");
                char drop = sc.next().charAt(0);
                System.out.println("Time:");
                int time = sc.nextInt();
                String result = BookingDetail.allocateBooking(tList, customerId, pick, drop, time, cList, lList, bList);
                System.out.println(result);
            } else if (type == 2) {
                displayTaxiDetails(tList, bList);
            } else if (type == 3) {
                break;
            } else {
                System.out.println("Invalid Choice. Please select either 1, 2 or 3");
            }
        }
        sc.close();
    }

    private static void displayTaxiDetails(List<Taxi> tList, List<BookingDetail> bList) {
        for (Taxi taxi : tList) {
            System.out.println("Taxi-" + taxi.getId() + " Total Earnings: Rs. " + taxi.getAmount());
            for (BookingDetail booking : bList) {
                if (booking.getTaxi().getId() == taxi.getId()) {
                    System.out.println(
                            booking.getBookingId() + " " +
                            booking.getCustomer().getCustomerId() + " " +
                            booking.getPickUpPoint().getName() + " " +
                            booking.getDropPoint().getName() + " " +
                            booking.getPickUpTime() + " " +
                            booking.getDroppedTime() + " " +
                            booking.getAmount());
                }
            }
        }
    }
}

class Customer {
    int customerId;

    public Customer(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }
}

class Taxi {
    int id;
    double amount;
    boolean isAvailable;
    Location location;

    public Taxi(int id, Location location) {
        this.id = id;
        this.amount = 0.0;
        this.isAvailable = true;
        this.location = location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public Location getLocation() {
        return location;
    }

    public void addAmount(double amount) {
        this.amount += amount;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public double calculateAmount(char pick, char drop) {
        int p = (int) pick;
        int d = (int) drop;
        int n = Math.abs(p - d) * 15;

        double amount = 100;
        if (n > 5) {
            amount += (n - 5) * 10;
        }
        return amount;
    }
}

class BookingDetail {
    static int bookingCounter = 0;

    int bookingId;
    Customer customer;
    Location pickUpPoint;
    Location dropPoint;
    int pickUpTime;
    int droppedTime;
    double amount;
    Taxi taxi;

    public BookingDetail(Customer customer, Location pickUpPoint, Location dropPoint, int pickUpTime, double amount,
            Taxi taxi, int droppedTime) {
        this.bookingId = ++bookingCounter;
        this.customer = customer;
        this.pickUpPoint = pickUpPoint;
        this.dropPoint = dropPoint;
        this.pickUpTime = pickUpTime;
        this.amount = amount;
        this.taxi = taxi;
        this.droppedTime = droppedTime;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getPickUpTime() {
        return pickUpTime;
    }

    public int getDroppedTime() {
        return droppedTime;
    }

    public double getAmount() {
        return amount;
    }

    public Taxi getTaxi() {
        return taxi;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Location getPickUpPoint() {
        return pickUpPoint;
    }

    public Location getDropPoint() {
        return dropPoint;
    }

    
    public static String allocateBooking(List<Taxi> tList, int customerId, char pick, char drop, int time,
            List<Customer> cList, List<Location> lList, List<BookingDetail> bList) {
        List<Taxi> availableTaxis = tList.stream().filter(Taxi::getIsAvailable).collect(Collectors.toList());
        availableTaxis.addAll(bList.stream().filter(b -> b.getDroppedTime() <= time).map(BookingDetail::getTaxi)
                .collect(Collectors.toList()));

        if (availableTaxis.isEmpty()) {
            return "Booking is rejected";
        }

        Taxi allocatedTaxi = findNearestAvailableTaxi(availableTaxis, pick);

        if (allocatedTaxi == null) {
            return "Booking is rejected";
        }

        double amount = allocatedTaxi.calculateAmount(pick, drop);
        Customer customer = cList.stream().filter(c -> c.getCustomerId() == customerId).findFirst().orElse(null);
        if (customer == null) {
            customer = new Customer(customerId);
            cList.add(customer);
        }

        Location pickUpLocation = lList.stream().filter(l -> l.getName() == pick).findFirst().get();
        Location dropLocation = lList.stream().filter(l -> l.getName() == drop).findFirst().get();
        int dropTime = time + Math.abs(pick - drop);

        BookingDetail booking = new BookingDetail(customer, pickUpLocation, dropLocation, time, amount, allocatedTaxi,
                dropTime);
        bList.add(booking);

        allocatedTaxi.setAvailable(false);
        allocatedTaxi.setLocation(dropLocation);
        allocatedTaxi.addAmount(amount);

        return "Taxi can be allotted.\nTaxi-" + allocatedTaxi.getId() + " is allotted";
    }

    private static Taxi findNearestAvailableTaxi(List<Taxi> availableTaxis, char pick) {
        Taxi nearestTaxi = null;
        int minDistance = Integer.MAX_VALUE;

        for (Taxi taxi : availableTaxis) {
            int distance = Math.abs(pick - taxi.getLocation().getName());
            if (distance < minDistance || (distance == minDistance && taxi.getAmount() < nearestTaxi.getAmount())) {
                minDistance = distance;
                nearestTaxi = taxi;
            }
        }

        return nearestTaxi;
    }
}

class Location {
    char name;

    public Location(char name) {
        this.name = name;
    }

    public char getName() {
        return name;
    }
}
