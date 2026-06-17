import java.util.*;
import java.util.stream.Collectors;

public class OnlineShopping {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ProfileService profileService = new ProfileService();
        InventoryService inventoryService = new InventoryService();
        OrderService orderService = new OrderService();
        PaymentService paymentService = new PaymentService();

        while (true) {
            System.out.println("""
                    Modules:
                     1. Register/Login
                     2. Manage Inventory
                     3. Shop and Place Orders
                     4. Exit
                    """);
            int module = sc.nextInt();
            sc.nextLine();

            switch (module) {
                case 1 -> profileService.manageProfiles(sc);
                case 2 -> inventoryService.manageInventory(sc, profileService);
                case 3 -> orderService.shopAndPlaceOrders(sc, profileService, inventoryService, paymentService);
                case 4 -> {
                    System.out.println("Exiting... Thank you for using the application.");
                    return;
                }
                default -> System.out.println("Invalid input. Please try again.");
            }
        }
    }
}

// Profile Service
class ProfileService {
    private final Map<String, User> users = new HashMap<>();
    private User currentUser;

    public void manageProfiles(Scanner sc) {
        System.out.println("""
                1. Register
                2. Login
                3. Logout
                """);
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> registerUser(sc);
            case 2 -> loginUser(sc);
            case 3 -> logoutUser();
            default -> System.out.println("Invalid choice.");
        }
    }

    private void registerUser(Scanner sc) {
        System.out.println("Enter username:");
        String username = sc.nextLine();
        System.out.println("Enter password:");
        String password = sc.nextLine();

        if (users.containsKey(username)) {
            System.out.println("Username already exists. Please try a different one.");
        } else {
            users.put(username, new User(username, encryptPassword(password)));
            System.out.println("Registration successful.");
        }
    }

    private void loginUser(Scanner sc) {
        System.out.println("Enter username:");
        String username = sc.nextLine();
        System.out.println("Enter password:");
        String password = sc.nextLine();

        User user = users.get(username);
        if (user != null && user.password.equals(encryptPassword(password))) {
            currentUser = user;
            System.out.println("Login successful. Welcome, " + username);
        } else {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    private void logoutUser() {
        if (currentUser != null) {
            System.out.println("Logged out successfully.");
            currentUser = null;
        } else {
            System.out.println("No user is logged in.");
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private String encryptPassword(String password) {
        return Integer.toHexString(password.hashCode());
    }
}

// Inventory Service
class InventoryService {
    private final Map<User, List<Item>> sellerInventory = new HashMap<>();

    public void manageInventory(Scanner sc, ProfileService profileService) {
        User currentUser = profileService.getCurrentUser();
        if (currentUser == null) {
            System.out.println("Please log in as a seller to manage inventory.");
            return;
        }

        System.out.println("""
                1. Add Item
                2. Update Item
                """);
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> addItem(sc, currentUser);
            case 2 -> updateItem(sc, currentUser);
            default -> System.out.println("Invalid choice.");
        }
    }

    private void addItem(Scanner sc, User seller) {
        System.out.println("Enter item name:");
        String itemName = sc.nextLine();
        System.out.println("Enter price:");
        double price = sc.nextDouble();
        System.out.println("Enter quantity:");
        int quantity = sc.nextInt();
        sc.nextLine();

        sellerInventory.putIfAbsent(seller, new ArrayList<>());
        sellerInventory.get(seller).add(new Item(itemName, price, quantity));
        System.out.println("Item added successfully.");
    }

    private void updateItem(Scanner sc, User seller) {
        List<Item> inventory = sellerInventory.getOrDefault(seller, new ArrayList<>());
        if (inventory.isEmpty()) {
            System.out.println("No items found in inventory.");
            return;
        }

        System.out.println("Current Inventory:");
        for (int i = 0; i < inventory.size(); i++) {
            System.out.println(i + 1 + ". " + inventory.get(i));
        }

        System.out.println("Select item number to update:");
        int itemIndex = sc.nextInt() - 1;
        sc.nextLine();

        if (itemIndex < 0 || itemIndex >= inventory.size()) {
            System.out.println("Invalid item number.");
            return;
        }

        Item item = inventory.get(itemIndex);
        System.out.println("Enter new price:");
        item.price = sc.nextDouble();
        System.out.println("Enter new quantity:");
        item.quantity = sc.nextInt();
        sc.nextLine();

        System.out.println("Item updated successfully.");
    }

    public List<Item> getInventory() {
        return sellerInventory.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }
}

// Order Service
class OrderService {
    private final Map<User, List<Item>> carts = new HashMap<>();

    public void shopAndPlaceOrders(Scanner sc, ProfileService profileService, InventoryService inventoryService,
                                   PaymentService paymentService) {
        User currentUser = profileService.getCurrentUser();
        if (currentUser == null) {
            System.out.println("Please log in as a buyer to place orders.");
            return;
        }

        System.out.println("""
                1. List Inventory
                2. Add to Cart
                3. Make Payment
                """);
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> listInventory(inventoryService);
            case 2 -> addToCart(sc, inventoryService, currentUser);
            case 3 -> makePayment(paymentService, currentUser);
            default -> System.out.println("Invalid choice.");
        }
    }

    private void listInventory(InventoryService inventoryService) {
        List<Item> inventory = inventoryService.getInventory();
        if (inventory.isEmpty()) {
            System.out.println("No items available.");
        } else {
            System.out.println("Available Items:");
            inventory.forEach(System.out::println);
        }
    }

    private void addToCart(Scanner sc, InventoryService inventoryService, User buyer) {
        List<Item> inventory = inventoryService.getInventory();
        if (inventory.isEmpty()) {
            System.out.println("No items available.");
            return;
        }

        System.out.println("Available Items:");
        for (int i = 0; i < inventory.size(); i++) {
            System.out.println(i + 1 + ". " + inventory.get(i));
        }

        System.out.println("Select item number to add to cart:");
        int itemIndex = sc.nextInt() - 1;
        sc.nextLine();

        if (itemIndex < 0 || itemIndex >= inventory.size()) {
            System.out.println("Invalid item number.");
            return;
        }

        Item item = inventory.get(itemIndex);
        carts.putIfAbsent(buyer, new ArrayList<>());
        carts.get(buyer).add(item);
        System.out.println("Item added to cart.");
    }

    private void makePayment(PaymentService paymentService, User buyer) {
        List<Item> cart = carts.getOrDefault(buyer, new ArrayList<>());
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        double totalAmount = cart.stream().mapToDouble(item -> item.price).sum();
        System.out.println("Total amount: Rs." + totalAmount);
        paymentService.processPayment(buyer, totalAmount);

        carts.remove(buyer);
        System.out.println("Order placed successfully.");
    }
}

// Payment Service
class PaymentService {
    public void processPayment(User buyer, double amount) {
        System.out.println("Payment of Rs." + amount + " processed for user: " + buyer.username);
    }
}

// Supporting Classes
class User {
    String username;
    String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

class Item {
    String name;
    double price;
    int quantity;

    public Item(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return name + " (Rs." + price + ", Qty: " + quantity + ")";
    }
}
