package manager;

import java.util.Arrays;

import comparators.CompareBuyerByName;
import comparators.CompareSellerByProductCount;
import enums.Category;
import products.Product;
import products.ShoppingCart;
import products.SpecialProduct;
import products.StandardProduct;
import users.Address;
import users.Buyer;
import users.Seller;

public class ECommerceManager {
    // Constants for initial size of the arrays and the growth factor for resizing
    private static final int INITIAL_SIZE = 4;
    private static final int GROWTH_FACTOR = 2;

    // Arrays to store sellers and buyers
    private Seller[] sellers;
    private Buyer[] buyers;

    // Counters to keep track of the number of sellers and buyers
    private int sellersCount;
    private int buyersCount;

    // Constructor to initialize arrays and counters
    public ECommerceManager() {
        sellers = new Seller[INITIAL_SIZE];
        buyers = new Buyer[INITIAL_SIZE];
        sellersCount = 0;
        buyersCount = 0;
    }

    // Method to add a new seller
    public boolean addSeller(String username, String password) {
        // Check if the seller already exists
        if (findSeller(username) != null) {
            return false; // Seller already exists
        }

        // Expand the sellers array if it's full
        if (sellersCount == sellers.length) {
            expandArray(sellers);
        }

        // Add the new seller and increment the seller count
        sellers[sellersCount++] = new Seller(username, password);
        return true;
    }

    // Method to add a new buyer
    public boolean addBuyer(String username, String password, Address address) {
        // Check if the buyer already exists
        if (findBuyer(username) != null) {
            return false; // Buyer already exists
        }

        // Expand the buyers array if it's full
        if (buyersCount == buyers.length) {
            expandArray(buyers);
        }

        // Add the new buyer and increment the buyer count
        buyers[buyersCount++] = new Buyer(username, password, address);
        return true;
    }

    // Method to display products for a specific seller
    public void displaySellerProducts(String sellerUsername) {
        // Find the seller by username
        Seller seller = findSeller(sellerUsername);
        if (seller == null) {
            System.out.println("Seller not found!");
            return;
        }

        System.out.println("Products for Seller: " + sellerUsername);
        Product[] products = seller.getProducts();

        // Print each product of the seller
        for (int i = 0; i < products.length; i++) {
            if (products[i] != null) {
                System.out.println((i + 1) + ". " + products[i]);
            }
        }
    }

    // Method to find a product by name for a specific seller
    public Product findSellerProduct(String sellerName, String productName) {
        Seller seller = findSeller(sellerName);
        if (seller == null) {
            System.out.println("Seller not found!");
            return null;
        }

        Product[] sellerProducts = seller.getProducts();
        if (sellerProducts == null || sellerProducts.length == 0) {
            System.out.println("No products available for the specified seller!");
            return null;
        }

        for (Product product : sellerProducts) {
            if (product != null && product.getName().equals(productName)) {
                return product;
            }
        }

        System.out.println("Product '" + productName + "' not found for seller: " + sellerName);
        return null;
    }

    // Method to add a product to a buyer's cart from a specific seller
    public boolean addProductForBuyerCart(String buyerUsername, String productName, String sellerNameForBuyer) {
        // Find the buyer by their username
        Buyer buyer = findBuyer(buyerUsername);
        if (buyer == null) {
            return false; // Buyer not found
        }

        // Find the product from the seller
        Product productToAdd = findSellerProduct(sellerNameForBuyer, productName);

        // If the product is not found, return
        if (productToAdd == null) {
            return false; // Product not found
        }

        // Add the product to the buyer's shopping cart
        buyer.addToShoppingCart(productToAdd);
        return true;
    }

    // Method to add a product for a specific seller
    public boolean addProductForSeller(String sellerUsername, String productName, double price, Category category,
            boolean isSpecial, double packagingCost) {
        // Find the seller by username
        Seller seller = findSeller(sellerUsername);
        if (seller == null) {
            return false; // Seller not found
        }

        // Create the product based on whether it's special or standard
        Product product;
        if (isSpecial) {
            product = new SpecialProduct(productName, price, sellerUsername, category, packagingCost);
        } else {
            product = new StandardProduct(productName, price, sellerUsername, category);
        }

        // Add the new product to the seller's product list
        seller.addProduct(product);
        return true;
    }

    // Method to display the current shopping cart of a buyer
    public boolean displayCurrentShoppingCart(String buyerNameForPayment) {
        // Find the buyer by username
        Buyer currentBuyer = findBuyer(buyerNameForPayment);
        if (currentBuyer == null) {
            return false; // Buyer not found
        }

        ShoppingCart currentCart = currentBuyer.getShoppingCart();
        int cartSize = currentCart.getCartSize();
        Product[] products = currentCart.getCart();
        double totalAmount = currentCart.getTotalAmount();

        // If the cart is empty, print a message
        if (cartSize == 0) {
            System.out.println("\tCart is Empty!");
            return false;
        } else {
            // Print the products in the cart and the total amount
            System.out.println("Products in Cart:");
            for (Product product : products) {
                if (product != null) {
                    System.out.println("\t" + product.getDetails());
                }
            }
            System.out.println("Total Amount: $" + totalAmount);
            return true;
        }
    }

    // Method to handle payment for the shopping cart
    public boolean paymentForShoppingCart(String paymentDecision, String buyerNameForPayment) {
        paymentDecision = paymentDecision.toLowerCase();
        Buyer currentBuyer = findBuyer(buyerNameForPayment);

        // If the payment decision is 'n', print a message and return
        if (paymentDecision.equals("n")) {
            System.out.println("Payment has not been made");
            return false;
        } else if (paymentDecision.equals("y")) {
            // If the payment decision is 'y', process the payment
            currentBuyer.paymentForShoppingCart();
            return true;
        }
        return false;
    }

    // Helper method to find a seller by username
    public Seller findSeller(String username) {
        for (int i = 0; i < sellersCount; i++) {
            if (sellers[i].getUsername().equals(username)) {
                return sellers[i];
            }
        }
        return null;
    }

    // Helper method to find a buyer by username
    public Buyer findBuyer(String username) {
        for (int i = 0; i < buyersCount; i++) {
            if (buyers[i].getUsername().equals(username)) {
                return buyers[i];
            }
        }
        return null;
    }

    // Method for displaying buyers --- sorted by buyer names
    public void displayBuyers() {
        if (buyersCount == 0) {
            System.out.println("No existing Buyers!");
            return;
        }

        Buyer[] sortedBuyers = Arrays.copyOf(buyers, buyersCount);
        Arrays.sort(sortedBuyers, new CompareBuyerByName());

        for (int i = 0; i < sortedBuyers.length; i++) {
            System.out.println((i + 1) + ". Buyer name: " + sortedBuyers[i].getUsername());
            System.out.println("Address: " + sortedBuyers[i].getAddress());
            System.out.println("Current Shopping Cart:");

            ShoppingCart cart = sortedBuyers[i].getShoppingCart();
            int cartSize = cart.getCartSize();
            Product[] products = cart.getCart();
            double totalAmount = cart.getTotalAmount();
            ShoppingCart[] ordersHistory = sortedBuyers[i].getOrdersHistory();
            int ordersHistoryCount = sortedBuyers[i].getOrderHistoryCount();

            if (cartSize == 0) {
                System.out.println("\tCart is Empty!");
            } else {
                for (Product product : products) {
                    if (product != null) {
                        System.out.println("\t" + product.getDetails());
                    }
                }
            }
            System.out.println(String.format("\tTotal Amount: $%.2f", totalAmount));

            System.out.println("Orders history: ");
            if (ordersHistoryCount == 0) {
                System.out.println("\tNo orders history..\n");
            } else {
                for (int j = 0; j < ordersHistoryCount; j++) {
                    if (ordersHistory[j] != null) {
                        System.out.println("\tOrder " + (j + 1) + ":");
                        System.out.println(ordersHistory[j]);
                    }
                }
            }
        }
    }

    // Method for displaying buyers --- sorted by sellers products Count
    public void displaySellers() {
        if (sellersCount == 0) {
            System.out.println("No existing Sellers!");
            return;
        }

        Seller[] sortedSellers = Arrays.copyOf(sellers, sellersCount);
        Arrays.sort(sortedSellers, new CompareSellerByProductCount());

        for (int i = 0; i < sortedSellers.length; i++) {
            System.out.println((i + 1) + ". Seller name: " + sortedSellers[i].getUsername());
            System.out.println("Products:");
            for (Product product : sortedSellers[i].getProducts()) {
                if (product != null) {
                    System.out.println("\t" + product.getDetails());
                }
            }
        }
    }

    // Method to display product by category
    public void displayProductsByCategory(Category category) {
        System.out.println("Products category: " + category);
        for (Seller seller : sellers) {
            if (seller != null) {
                for (Product product : seller.getProducts()) {
                    if (product != null) {
                        if (product.getCategory() == category) {
                            System.out.println(product.getDetails());
                        }
                    }
                }
            }
        }
    }

    // Method to create a new cart from history
    public void createNewCartFromHistory(Buyer buyer, int historyIndex) {
        ShoppingCart[] history = buyer.getOrdersHistory();
        if (historyIndex >= 0 && historyIndex < history.length) {
            try {
                ShoppingCart selectedCart = (ShoppingCart) history[historyIndex].clone();
                buyer.setShoppingCart(selectedCart);
                System.out.println("New cart created from history.");
            } catch (CloneNotSupportedException e) {
                System.out.println("Failed to clone the shopping cart: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid history index.");
        }
    }

    // Methd to display buyers orders history
    public void displayBuyerOrderHistory(String buyerUsername) {
        Buyer buyer = findBuyer(buyerUsername);
        if (buyer != null) {
            ShoppingCart[] orderHistory = buyer.getOrdersHistory();
            int orderHistoryCount = buyer.getOrderHistoryCount();
            if (orderHistoryCount == 0) {
                System.out.println("No orders history..");
            } else {
                for (int i = 0; i < orderHistoryCount; i++) {
                    if (orderHistory[i] != null) {
                        System.out.println((i + 1) + ". " + orderHistory[i]);
                    }
                }
            }
        } else {
            System.out.println("Buyer not found.");
        }
    }

    // Method to get all products of seller
    public Product[] getSellerProducts(String sellerName) {
        Seller seller = findSeller(sellerName);
        if (seller != null) {
            return seller.getProducts();
        }
        return null;
    }

    // Helper method to expand the size of an array
    private void expandArray(Object[] array) {
        int newSize = array.length * GROWTH_FACTOR;
        array = Arrays.copyOf(array, newSize);
    }
}
