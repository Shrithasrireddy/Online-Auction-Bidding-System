import java.util.ArrayList;
import java.util.Scanner;

/*
 * ONLINE AUCTION & BIDDING SYSTEM
 *
 * Features:
 * 1. User Registration
 * 2. User Login
 * 3. Admin Login
 * 4. Add Auction Item
 * 5. View Active Auctions
 * 6. Place Bid
 * 7. View My Bids
 * 8. Close Auction
 * 9. View Auction Result
 * 10. Search Auction Items
 */

public class bidding {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<User> users = new ArrayList<>();
    static ArrayList<Auction> auctions = new ArrayList<>();
    static ArrayList<Bid> bids = new ArrayList<>();

    static int userIdCounter = 1001;
    static int auctionIdCounter = 5001;
    static int bidIdCounter = 1;

    public static void main(String[] args) {

        // Default admin
        Admin admin = new Admin("admin", "admin123");
        
        // Sample users
        users.add(new User(userIdCounter++, "vedansh", "1234"));
        users.add(new User(userIdCounter++, "student", "1234"));

        // Sample auction
        Auction sampleAuction = new Auction(
                auctionIdCounter++,
                "Laptop",
                "Dell Inspiron 15 Laptop",
                45000,
                500,
                "admin"
        );

        auctions.add(sampleAuction);

        while (true) {

            System.out.println("\n======================================");
            System.out.println("       ONLINE AUCTION SYSTEM");
            System.out.println("======================================");
            System.out.println("1. User Registration");
            System.out.println("2. User Login");
            System.out.println("3. Admin Login");
            System.out.println("4. Exit");
            System.out.println("--------------------------------------");

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    registerUser();
                    break;

                case 2:
                    userLogin();
                    break;

                case 3:
                    adminLogin(admin);
                    break;

                case 4:
                    System.out.println("\nThank you for using Online Auction System!");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // ============================================================
    // USER REGISTRATION
    // ============================================================

    static void registerUser() {

        System.out.println("\n========== USER REGISTRATION ==========");

        String username;

        while (true) {

            username = readString("Enter username: ");

            if (username.isEmpty()) {
                System.out.println("Username cannot be empty.");
                continue;
            }

            if (findUser(username) != null) {
                System.out.println("Username already exists.");
                continue;
            }

            break;
        }

        String password;

        while (true) {

            password = readString("Enter password: ");

            if (password.length() < 4) {
                System.out.println("Password must contain at least 4 characters.");
            } else {
                break;
            }
        }

        User user = new User(userIdCounter++, username, password);

        users.add(user);

        System.out.println("\nRegistration successful!");
        System.out.println("Your User ID: " + user.getUserId());
    }

    // ============================================================
    // USER LOGIN
    // ============================================================

    static void userLogin() {

        System.out.println("\n============== USER LOGIN ==============");

        String username = readString("Username: ");
        String password = readString("Password: ");

        User user = findUser(username);

        if (user == null || !user.getPassword().equals(password)) {
            System.out.println("Invalid username or password.");
            return;
        }

        System.out.println("\nLogin successful!");
        System.out.println("Welcome, " + user.getUsername() + "!");

        userMenu(user);
    }

    // ============================================================
    // USER MENU
    // ============================================================

    static void userMenu(User user) {

        while (true) {

            System.out.println("\n======================================");
            System.out.println("             USER MENU");
            System.out.println("======================================");
            System.out.println("1. View Active Auctions");
            System.out.println("2. Search Auction");
            System.out.println("3. Place Bid");
            System.out.println("4. View My Bids");
            System.out.println("5. View Auction Result");
            System.out.println("6. Logout");
            System.out.println("--------------------------------------");

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    viewActiveAuctions();
                    break;

                case 2:
                    searchAuction();
                    break;

                case 3:
                    placeBid(user);
                    break;

                case 4:
                    viewMyBids(user);
                    break;

                case 5:
                    viewAuctionResult();
                    break;

                case 6:
                    System.out.println("Logged out successfully.");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // ============================================================
    // VIEW ACTIVE AUCTIONS
    // ============================================================

    static void viewActiveAuctions() {

        System.out.println("\n========== ACTIVE AUCTIONS ==========");

        boolean found = false;

        for (Auction auction : auctions) {

            if (auction.isActive()) {

                found = true;

                System.out.println("--------------------------------------");
                auction.displayAuction();
            }
        }

        if (!found) {
            System.out.println("No active auctions available.");
        }
    }

    // ============================================================
    // SEARCH AUCTION
    // ============================================================

    static void searchAuction() {

        System.out.println("\n========== SEARCH AUCTION ==========");

        String keyword = readString("Enter item name/category: ")
                .toLowerCase();

        boolean found = false;

        for (Auction auction : auctions) {

            if (auction.getItemName().toLowerCase().contains(keyword)
                    || auction.getCategory().toLowerCase().contains(keyword)) {

                found = true;

                System.out.println("--------------------------------------");
                auction.displayAuction();
            }
        }

        if (!found) {
            System.out.println("No matching auction found.");
        }
    }

    // ============================================================
    // PLACE BID
    // ============================================================

    static void placeBid(User user) {

        System.out.println("\n========== PLACE BID ==========");

        viewActiveAuctions();

        if (auctions.isEmpty()) {
            return;
        }

        int auctionId = readInt("\nEnter Auction ID: ");

        Auction auction = findAuction(auctionId);

        if (auction == null) {
            System.out.println("Auction not found.");
            return;
        }

        if (!auction.isActive()) {
            System.out.println("This auction is closed.");
            return;
        }

        System.out.println("\nItem: " + auction.getItemName());
        System.out.println("Current Highest Bid: ₹"
                + auction.getCurrentHighestBid());

        double minimumBid =
                auction.getCurrentHighestBid() + auction.getBidIncrement();

        System.out.println("Minimum Bid Required: ₹" + minimumBid);

        double amount = readDouble("Enter your bid amount: ₹");

        if (amount < minimumBid) {

            System.out.println(
                    "Bid rejected! Bid must be at least ₹" + minimumBid
            );

            return;
        }

        Bid bid = new Bid(
                bidIdCounter++,
                user.getUserId(),
                user.getUsername(),
                auctionId,
                amount
        );

        bids.add(bid);

        auction.setCurrentHighestBid(amount);
        auction.setHighestBidder(user.getUsername());

        System.out.println("\n*** BID PLACED SUCCESSFULLY ***");
        System.out.println("Bid ID: " + bid.getBidId());
        System.out.println("Bid Amount: ₹" + amount);
    }

    // ============================================================
    // VIEW MY BIDS
    // ============================================================

    static void viewMyBids(User user) {

        System.out.println("\n========== MY BIDS ==========");

        boolean found = false;

        for (Bid bid : bids) {

            if (bid.getUserId() == user.getUserId()) {

                found = true;

                System.out.println("--------------------------------------");
                System.out.println("Bid ID       : " + bid.getBidId());
                System.out.println("Auction ID   : " + bid.getAuctionId());
                System.out.println("Amount       : ₹" + bid.getAmount());
                System.out.println("Status       : " +
                        getBidStatus(bid));
            }
        }

        if (!found) {
            System.out.println("You haven't placed any bids.");
        }
    }

    // ============================================================
    // VIEW AUCTION RESULT
    // ============================================================

    static void viewAuctionResult() {

        System.out.println("\n========== AUCTION RESULT ==========");

        int auctionId = readInt("Enter Auction ID: ");

        Auction auction = findAuction(auctionId);

        if (auction == null) {
            System.out.println("Auction not found.");
            return;
        }

        System.out.println("\nItem: " + auction.getItemName());

        if (auction.isActive()) {

            System.out.println("Status: ACTIVE");
            System.out.println(
                    "Current Highest Bid: ₹"
                            + auction.getCurrentHighestBid()
            );

            System.out.println(
                    "Current Highest Bidder: "
                            + auction.getHighestBidder()
            );

        } else {

            System.out.println("Status: CLOSED");

            if (auction.getHighestBidder() != null) {

                System.out.println(
                        "Winner: "
                                + auction.getHighestBidder()
                );

                System.out.println(
                        "Winning Bid: ₹"
                                + auction.getCurrentHighestBid()
                );

            } else {

                System.out.println("No bids were placed.");
            }
        }
    }

    // ============================================================
    // ADMIN LOGIN
    // ============================================================

    static void adminLogin(Admin admin) {

        System.out.println("\n============== ADMIN LOGIN ==============");

        String username = readString("Username: ");
        String password = readString("Password: ");

        if (admin.login(username, password)) {

            System.out.println("\nAdmin login successful!");

            adminMenu();

        } else {

            System.out.println("Invalid admin credentials.");
        }
    }

    // ============================================================
    // ADMIN MENU
    // ============================================================

    static void adminMenu() {

        while (true) {

            System.out.println("\n======================================");
            System.out.println("             ADMIN MENU");
            System.out.println("======================================");
            System.out.println("1. Add Auction");
            System.out.println("2. View All Auctions");
            System.out.println("3. Close Auction");
            System.out.println("4. View All Users");
            System.out.println("5. View All Bids");
            System.out.println("6. Logout");
            System.out.println("--------------------------------------");

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    addAuction();
                    break;

                case 2:
                    viewAllAuctions();
                    break;

                case 3:
                    closeAuction();
                    break;

                case 4:
                    viewAllUsers();
                    break;

                case 5:
                    viewAllBids();
                    break;

                case 6:
                    System.out.println("Admin logged out.");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // ============================================================
    // ADD AUCTION
    // ============================================================

    static void addAuction() {

        System.out.println("\n========== ADD AUCTION ==========");

        String category =
                readString("Enter item category: ");

        String itemName =
                readString("Enter item name: ");

        String description =
                readString("Enter item description: ");

        double startingPrice;

        while (true) {

            startingPrice =
                    readDouble("Enter starting price: ₹");

            if (startingPrice <= 0) {
                System.out.println("Price must be greater than 0.");
            } else {
                break;
            }
        }

        double bidIncrement;

        while (true) {

            bidIncrement =
                    readDouble("Enter minimum bid increment: ₹");

            if (bidIncrement <= 0) {
                System.out.println(
                        "Bid increment must be greater than 0."
                );
            } else {
                break;
            }
        }

        Auction auction = new Auction(
                auctionIdCounter++,
                category,
                itemName,
                startingPrice,
                bidIncrement,
                "admin"
        );

        auctions.add(auction);

        System.out.println("\nAuction created successfully!");
        System.out.println("Auction ID: "
                + auction.getAuctionId());
    }

    // ============================================================
    // VIEW ALL AUCTIONS
    // ============================================================

    static void viewAllAuctions() {

        System.out.println("\n========== ALL AUCTIONS ==========");

        if (auctions.isEmpty()) {

            System.out.println("No auctions available.");
            return;
        }

        for (Auction auction : auctions) {

            System.out.println("--------------------------------------");
            auction.displayAuction();
        }
    }

    // ============================================================
    // CLOSE AUCTION
    // ============================================================

    static void closeAuction() {

        System.out.println("\n========== CLOSE AUCTION ==========");

        int auctionId =
                readInt("Enter Auction ID: ");

        Auction auction =
                findAuction(auctionId);

        if (auction == null) {

            System.out.println("Auction not found.");
            return;
        }

        if (!auction.isActive()) {

            System.out.println("Auction is already closed.");
            return;
        }

        auction.closeAuction();

        System.out.println("\nAuction closed successfully!");

        if (auction.getHighestBidder() != null) {

            System.out.println(
                    "Winner: "
                            + auction.getHighestBidder()
            );

            System.out.println(
                    "Winning Bid: ₹"
                            + auction.getCurrentHighestBid()
            );

        } else {

            System.out.println("No bids were placed.");
        }
    }

    // ============================================================
    // VIEW ALL USERS
    // ============================================================

    static void viewAllUsers() {

        System.out.println("\n========== ALL USERS ==========");

        for (User user : users) {

            System.out.println("--------------------------------------");
            System.out.println("User ID  : " + user.getUserId());
            System.out.println("Username : " + user.getUsername());
        }
    }

    // ============================================================
    // VIEW ALL BIDS
    // ============================================================

    static void viewAllBids() {

        System.out.println("\n========== ALL BIDS ==========");

        if (bids.isEmpty()) {

            System.out.println("No bids have been placed.");
            return;
        }

        for (Bid bid : bids) {

            System.out.println("--------------------------------------");
            System.out.println("Bid ID       : " + bid.getBidId());
            System.out.println("User         : " + bid.getUsername());
            System.out.println("Auction ID   : " + bid.getAuctionId());
            System.out.println("Amount       : ₹" + bid.getAmount());
        }
    }

    // ============================================================
    // HELPER METHODS
    // ============================================================

    static User findUser(String username) {

        for (User user : users) {

            if (user.getUsername()
                    .equalsIgnoreCase(username)) {

                return user;
            }
        }

        return null;
    }

    static Auction findAuction(int auctionId) {

        for (Auction auction : auctions) {

            if (auction.getAuctionId() == auctionId) {

                return auction;
            }
        }

        return null;
    }

    static String getBidStatus(Bid bid) {

        Auction auction =
                findAuction(bid.getAuctionId());

        if (auction == null) {
            return "UNKNOWN";
        }

        if (auction.isActive()) {

            if (auction.getHighestBidder()
                    .equals(bid.getUsername())
                    && auction.getCurrentHighestBid()
                    == bid.getAmount()) {

                return "CURRENT HIGHEST BID";
            }

            return "OUTBID";
        }

        if (auction.getHighestBidder() != null
                && auction.getHighestBidder()
                .equals(bid.getUsername())
                && auction.getCurrentHighestBid()
                == bid.getAmount()) {

            return "WON";
        }

        return "LOST";
    }

    static int readInt(String message) {

        while (true) {

            try {

                System.out.print(message);

                String input = sc.nextLine();

                return Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid integer."
                );
            }
        }
    }

    static double readDouble(String message) {

        while (true) {

            try {

                System.out.print(message);

                String input = sc.nextLine();

                return Double.parseDouble(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }

    static String readString(String message) {

        System.out.print(message);

        return sc.nextLine().trim();
    }
}


// ================================================================
// USER CLASS
// ================================================================

class User {

    private int userId;
    private String username;
    private String password;

    public User(int userId, String username, String password) {

        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}


// ================================================================
// ADMIN CLASS
// ================================================================

class Admin {

    private String username;
    private String password;

    public Admin(String username, String password) {

        this.username = username;
        this.password = password;
    }

    public boolean login(String username, String password) {

        return this.username.equals(username)
                && this.password.equals(password);
    }
}


// ================================================================
// AUCTION CLASS
// ================================================================

class Auction {

    private int auctionId;
    private String category;
    private String itemName;
    private String description;

    private double startingPrice;
    private double currentHighestBid;
    private double bidIncrement;

    private String highestBidder;
    private String createdBy;

    private boolean active;

    public Auction(
            int auctionId,
            String category,
            String itemName,
            double startingPrice,
            double bidIncrement,
            String createdBy) {

        this.auctionId = auctionId;
        this.category = category;
        this.itemName = itemName;
        this.startingPrice = startingPrice;
        this.currentHighestBid = startingPrice;
        this.bidIncrement = bidIncrement;
        this.createdBy = createdBy;
        this.active = true;
    }

    public void displayAuction() {

        System.out.println("Auction ID       : " + auctionId);
        System.out.println("Category         : " + category);
        System.out.println("Item Name        : " + itemName);

        if (description != null) {
            System.out.println("Description      : " + description);
        }

        System.out.println("Starting Price   : ₹" + startingPrice);
        System.out.println("Current Highest  : ₹"
                + currentHighestBid);

        System.out.println("Bid Increment    : ₹"
                + bidIncrement);

        System.out.println("Highest Bidder   : "
                + (highestBidder == null
                ? "No bids yet"
                : highestBidder));

        System.out.println("Status           : "
                + (active ? "ACTIVE" : "CLOSED"));
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCurrentHighestBid(double amount) {
        this.currentHighestBid = amount;
    }

    public void setHighestBidder(String username) {
        this.highestBidder = username;
    }

    public void closeAuction() {
        this.active = false;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public String getCategory() {
        return category;
    }

    public String getItemName() {
        return itemName;
    }

    public double getCurrentHighestBid() {
        return currentHighestBid;
    }

    public double getBidIncrement() {
        return bidIncrement;
    }

    public String getHighestBidder() {
        return highestBidder;
    }

    public boolean isActive() {
        return active;
    }
}


// ================================================================
// BID CLASS
// ================================================================

class Bid {

    private int bidId;
    private int userId;
    private String username;
    private int auctionId;
    private double amount;

    public Bid(
            int bidId,
            int userId,
            String username,
            int auctionId,
            double amount) {

        this.bidId = bidId;
        this.userId = userId;
        this.username = username;
        this.auctionId = auctionId;
        this.amount = amount;
    }

    public int getBidId() {
        return bidId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public double getAmount() {
        return amount;
    }
}