package users;

import java.util.Arrays;

import products.Product;

public class Seller extends User {
    private Product[] products; // Array to store the seller's products
    private int productCount; // Counter to keep track of the number of products

    private static final int INITIAL_SIZE = 4; // Initial size of the products array
    private static final int GROWTH_FACTOR = 2; // Growth factor for resizing the array

    public Seller(String username, String password) {
        super(username, password);
        this.products = new Product[INITIAL_SIZE]; // Initialize products array with initial size
        this.productCount = 0; // Initialize product count
    }

    // Method to add a product
    public void addProduct(Product product) {
        if (productCount == products.length) {
            expandArray(products); // Expand the array if it's full
        }
        products[productCount++] = product; // Add the product
    }

    // Getter method to retrieve the products array
    public Product[] getProducts() {
        return products;
    }

    // Getter method to retrieve the products count
    public int getProductCount() {
        return productCount;
    }

    // Helper method to expand the size of the array
    private void expandArray(Product[] array) {
        int newSize = array.length * GROWTH_FACTOR;
        products = Arrays.copyOf(array, newSize);
    }
}
