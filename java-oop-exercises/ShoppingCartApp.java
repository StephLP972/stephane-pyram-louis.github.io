import java.util.ArrayList;

// Exercise 6 - Shopping Cart Promotion Engine

abstract class CartItem {
    private String sku;
    private int quantity;
    private double unitPrice;

    public CartItem(String sku, int quantity, double unitPrice) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0 (got " + quantity + ")");
        }
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Unit price must be >= 0 (got " + unitPrice + ")");
        }
        this.sku = sku;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void addQuantity(int extra) {
        quantity = quantity + extra;
    }

    public abstract double subtotal();

    public abstract String getType();
}

class GroceryItem extends CartItem {
    public GroceryItem(String sku, int quantity, double unitPrice) {
        super(sku, quantity, unitPrice);
    }

    @Override
    public double subtotal() {
        return getQuantity() * getUnitPrice() * 1.05;
    }

    @Override
    public String getType() {
        return "Grocery";
    }
}

class DigitalItem extends CartItem {
    public DigitalItem(String sku, int quantity, double unitPrice) {
        super(sku, quantity, unitPrice);
    }

    @Override
    public double subtotal() {
        return getQuantity() * getUnitPrice() * 1.15;
    }

    @Override
    public String getType() {
        return "Digital";
    }
}

class Cart {
    private ArrayList<CartItem> items = new ArrayList<>();

    public boolean addItem(CartItem newItem) {
        for (int i = 0; i < items.size(); i++) {
            CartItem existing = items.get(i);
            if (existing.getSku().equals(newItem.getSku())) {
                boolean sameType = existing.getClass() == newItem.getClass();
                boolean samePrice = existing.getUnitPrice() == newItem.getUnitPrice();
                if (sameType && samePrice) {
                    existing.addQuantity(newItem.getQuantity());
                    System.out.println("Merged " + newItem.getSku() + " -> quantity is now " + existing.getQuantity());
                    return true;
                } else {
                    System.out.println("Rejected " + newItem.getSku() + ": same SKU but different type or price");
                    return false;
                }
            }
        }
        items.add(newItem);
        System.out.println("Added " + newItem.getSku() + " (" + newItem.getType() + ") x" + newItem.getQuantity());
        return true;
    }

    public double getSubtotalSum() {
        double sum = 0;
        for (int i = 0; i < items.size(); i++) {
            sum = sum + items.get(i).subtotal();
        }
        return sum;
    }

    public double getDiscount() {
        double sum = getSubtotalSum();
        // rounded to cents before comparing, so 199.99999999 counts as 200.00
        if (Math.round(sum * 100) >= 20000) {
            return sum * 0.05;
        }
        return 0;
    }

    public double getTotal() {
        return getSubtotalSum() - getDiscount();
    }

    public void printReceipt() {
        System.out.println("-------------------------- RECEIPT --------------------------");
        System.out.printf("%-8s %-8s %5s %10s %12s%n", "SKU", "Type", "Qty", "Unit", "Subtotal");
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            System.out.printf("%-8s %-8s %5d %10.2f %12.2f%n",
                    item.getSku(), item.getType(), item.getQuantity(), item.getUnitPrice(), item.subtotal());
        }
        System.out.printf("Pre-discount sum : %.2f%n", getSubtotalSum());
        System.out.printf("Discount (5%%)    : %.2f%n", getDiscount());
        System.out.printf("Total to pay     : %.2f%n", getTotal());
        System.out.println("-------------------------------------------------------------");
    }
}

public class ShoppingCartApp {

    public static void main(String[] args) {
        demoMerge();
        testMergeRejected();
        testInvalidQuantity();
        testBelow200();
        testExactly200();
        testAbove200();
    }

    static void demoMerge() {
        System.out.println("=== Demo: merge RICE 2 + 3 ===");
        Cart cart = new Cart();
        cart.addItem(new GroceryItem("RICE", 2, 10.00));
        cart.addItem(new GroceryItem("RICE", 3, 10.00));
        cart.printReceipt();
    }

    static void testMergeRejected() {
        System.out.println("\n=== Test: merge rejected (different price / different type) ===");
        Cart cart = new Cart();
        cart.addItem(new GroceryItem("RICE", 2, 10.00));
        cart.addItem(new GroceryItem("RICE", 1, 12.00));  // different price
        cart.addItem(new DigitalItem("RICE", 1, 10.00));  // different type
        cart.printReceipt();
    }

    static void testInvalidQuantity() {
        System.out.println("\n=== Test: zero and negative quantity ===");
        Cart cart = new Cart();
        try {
            cart.addItem(new GroceryItem("MILK", 0, 2.50));
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
        try {
            cart.addItem(new DigitalItem("EBOOK", -2, 9.99));
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
        cart.printReceipt();
    }

    static void testBelow200() {
        System.out.println("\n=== Test: total below 200 (no discount) ===");
        Cart cart = new Cart();
        cart.addItem(new GroceryItem("APPLE", 10, 2.00));
        cart.addItem(new DigitalItem("GAME", 1, 59.99));
        cart.printReceipt();
    }

    static void testExactly200() {
        System.out.println("\n=== Test: total exactly 200.00 (discount applies) ===");
        Cart cart = new Cart();
        cart.addItem(new GroceryItem("BREAD", 1, 1.00));     // 1.05
        cart.addItem(new DigitalItem("SOFT", 1, 173.00));    // 198.95
        cart.printReceipt();
    }

    static void testAbove200() {
        System.out.println("\n=== Test: total above 200 (discount applies) ===");
        Cart cart = new Cart();
        cart.addItem(new DigitalItem("OFFICE", 2, 150.00));
        cart.addItem(new GroceryItem("COFFEE", 4, 8.50));
        cart.printReceipt();
    }
}
