import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ECommerceManager manager = new ECommerceManager();

        // Demo data for debugging
        setupDemoData(manager);

        int choice;
        boolean continueLoop = true;

        do {
            // Displaying the menu options
            System.out.println("Menu:");
            System.out.println("0. Exit");
            System.out.println("1. Add Seller");
            System.out.println("2. Add Buyer");
            System.out.println("3. Add Product for Seller");
            System.out.println("4. Add Product for Buyer");
            System.out.println("5. Order Payment for Buyer");
            System.out.println("6. Display Details of All Buyers");
            System.out.println("7. Display Details of All Sellers");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt(); // Getting the user's choice

            switch (choice) {
                case 0:
                    continueLoop = false;
                    break;
                case 1:
                    handleAddSeller(manager);
                    break;
                case 2:
                    handleAddBuyer(manager);
                    break;
                case 3:
                    handleAddProductForSeller(manager);
                    break;
                case 4:
                    handleAddProductForBuyer(manager);
                    break;
                case 5:
                    handleOrderPaymentForBuyer(manager);
                    break;
                case 6:
                    handleDisplayDetailsOfAllBuyers(manager);
                    break;
                case 7:
                    handleDisplayDetailsOfAllSellers(manager);
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (continueLoop);

        scanner.close();
    }

    private static void setupDemoData(ECommerceManager manager) {
        // Adding sellers
        manager.addSeller("seller1", "password1");
        manager.addSeller("seller2", "password2");

        // Adding buyers with Address
        manager.addBuyer("buyer1", "password1", new Address("123 New York", "City A", "State A", "10001", "Country A"));
        manager.addBuyer("buyer2", "password2", new Address("456 Tel Aviv", "City B", "State B", "20002", "Country B"));
        manager.addBuyer("buyer3", "password3", new Address("789 Thailand", "City C", "State C", "30003", "Country C"));

        // Adding products for sellers
        manager.addProductForSeller("seller1", "Product A", 10.99);
        manager.addProductForSeller("seller1", "Product B", 15.49);
        manager.addProductForSeller("seller2", "Product C", 20.99);
        manager.addProductForSeller("seller2", "Product D", 5.99);

        // Adding products for buyers (adding to cart)
        manager.addProductForBuyerCart("buyer1", "Product A", "seller1");
        manager.addProductForBuyerCart("buyer1", "Product C", "seller2");
        manager.addProductForBuyerCart("buyer2", "Product B", "seller1");
    }

    private static void handleAddSeller(ECommerceManager manager) {
        boolean usernameExists;

        do {
            System.out.print("Enter seller's username: ");
            String sellerUsername = scanner.next();

            if (manager.findSeller(sellerUsername) != null) {
                System.out
                        .println("Username " + sellerUsername + " already exists! Please choose a different username.");
                usernameExists = true; // Set flag to true to repeat the loop
            } else {
                usernameExists = false; // Set flag to false to exit the loop
                System.out.print("Enter seller's password: ");
                String sellerPassword = scanner.next();
                boolean success = manager.addSeller(sellerUsername, sellerPassword);
                if (success) {
                    System.out.println("Seller added successfully!");
                } else {
                    System.out.println("Failed to add seller. Please try again.");
                }
            }
        } while (usernameExists);
    }

    private static void handleAddBuyer(ECommerceManager manager) {
        boolean usernameExists;

        do {
            System.out.print("Enter buyer's username: ");
            String buyerUsername = scanner.next();

            if (manager.findBuyer(buyerUsername) != null) {
                System.out.println("Username " + buyerUsername + " already exists! Please enter a different username.");
                usernameExists = true;
            } else {
                usernameExists = false;
                System.out.print("Enter buyer's password: ");
                String buyerPassword = scanner.next();

                // Collect address details
                System.out.print("Enter buyer's street: ");
                scanner.nextLine(); // Consume the newline
                String street = scanner.nextLine();
                System.out.print("Enter buyer's city: ");
                String city = scanner.nextLine();
                System.out.print("Enter buyer's state: ");
                String state = scanner.nextLine();
                System.out.print("Enter buyer's postal code: ");
                String postalCode = scanner.nextLine();
                System.out.print("Enter buyer's country: ");
                String country = scanner.nextLine();

                // Create Address object
                Address address = new Address(street, city, state, postalCode, country);

                // Add buyer with the created Address object
                boolean success = manager.addBuyer(buyerUsername, buyerPassword, address);
                if (success) {
                    System.out.println("Buyer added successfully!");
                } else {
                    System.out.println("Failed to add buyer. Please try again.");
                }
            }
        } while (usernameExists);
    }

    private static void handleAddProductForSeller(ECommerceManager manager) {
        String sellerName;
        boolean sellerExists;

        do {
            System.out.print("Enter seller's username (or press 'x' to return to menu): ");
            sellerName = scanner.next();

            if (sellerName.equalsIgnoreCase("x")) {
                return; // Return to menu
            }

            sellerExists = manager.findSeller(sellerName) != null;
            if (!sellerExists) {
                System.out.println("Seller not found! Try again or press 'x' to return to menu.");
            }
        } while (!sellerExists);

        System.out.print("Enter product name: ");
        String productName = scanner.next();
        System.out.print("Enter product price: ");
        double productPrice = scanner.nextDouble();

        boolean success = manager.addProductForSeller(sellerName, productName, productPrice);
        if (success) {
            System.out.println("Product added successfully for seller: " + sellerName);
        } else {
            System.out.println("Failed to add product for seller. Please try again.");
        }
    }

    private static void handleAddProductForBuyer(ECommerceManager manager) {
        String buyerUsername;
        boolean buyerExists;

        do {
            System.out.print("Enter buyer's username (or press 'x' to return to menu): ");
            buyerUsername = scanner.next();

            if (buyerUsername.equalsIgnoreCase("x")) {
                return; // Return to menu
            }

            buyerExists = manager.findBuyer(buyerUsername) != null;
            if (!buyerExists) {
                System.out.println("Buyer not found! Try again or press 'x' to return to menu.");
            }
        } while (!buyerExists);

        System.out.print("Enter seller's username: ");
        String sellerNameForBuyer = scanner.next();

        boolean sellerExists = manager.findSeller(sellerNameForBuyer) != null;
        if (!sellerExists) {
            System.out.println("Seller not found! Returning to menu.");
            return; // Return to menu if seller not found, allowing to watch seller's list again
        }

        manager.displaySellerProducts(sellerNameForBuyer);

        System.out.print("Enter product name: ");
        scanner.nextLine();
        String productNameForBuyer = scanner.nextLine();

        boolean success = manager.addProductForBuyerCart(buyerUsername, productNameForBuyer, sellerNameForBuyer);
        if (success) {
            System.out.println("Product added to cart successfully for buyer: " + buyerUsername);
        } else {
            System.out.println("Failed to add product to cart. Please check product availability or try again.");
        }
    }

    private static void handleOrderPaymentForBuyer(ECommerceManager manager) {
        System.out.print("Enter buyer's username for payment: ");
        String buyerUsernameForPayment = scanner.next();

        boolean hasItems = manager.displayCurrentShoppingCart(buyerUsernameForPayment);
        if (!hasItems) {
            System.out.println("No items in the cart. Returning to the menu.");
            return;
        }

        System.out.print("Do you want to pay for this cart? (y/n): ");
        String paymentDecision = scanner.next();
        boolean success = manager.paymentForShoppingCart(paymentDecision, buyerUsernameForPayment);
        if (success) {
            System.out.println("Payment processed successfully.");
        } else {
            System.out.println("Failed to process payment. Please try again.");
        }
    }

    private static void handleDisplayDetailsOfAllBuyers(ECommerceManager manager) {
        manager.displayBuyers();
    }

    private static void handleDisplayDetailsOfAllSellers(ECommerceManager manager) {
        manager.displaySellers();
    }
}
