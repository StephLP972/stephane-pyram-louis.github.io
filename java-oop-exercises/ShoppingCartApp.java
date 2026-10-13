import java.util.ArrayList;

abstract class CartItem {
    private String sku;
    private int quantity;
    private double unitPrice;

    public CartItem(String sku, int quantity, double unitPrice) {
        if (quantity <= 0 || unitPrice < 0) {
            throw new IllegalArgumentException("Invalid quantity or price");
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

    public void addQuantity(int q) {
        quantity = quantity + q;
    }

    public abstract double subtotal();

    public abstract String getType();
}

class GroceryItem extends CartItem {
    public GroceryItem(String sku, int quantity, double unitPrice) {
        super(sku, quantity, unitPrice);
    }

    public double subtotal() {
        return getQuantity() * getUnitPrice() * 1.05;
    }

    public String getType() {
        return "Grocery";
    }
}

class DigitalItem extends CartItem {
    public DigitalItem(String sku, int quantity, double unitPrice) {
        super(sku, quantity, unitPrice);
    }

    public double subtotal() {
        return getQuantity() * getUnitPrice() * 1.15;
    }

    public String getType() {
        return "Digital";
    }
}

class Cart {
    private ArrayList<CartItem> items = new ArrayList<>();

    public void addItem(CartItem item) {
        for (int i = 0; i < items.size(); i++) {
            CartItem old = items.get(i);
            if (old.getSku().equals(item.getSku())) {
                if (old.getType().equals(item.getType()) && old.getUnitPrice() == item.getUnitPrice()) {
                    old.addQuantity(item.getQuantity());
                    System.out.println("Merged " + item.getSku());
                } else {
                    System.out.println("Rejected " + item.getSku());
                }
                return;
            }
        }
        items.add(item);
        System.out.println("Added " + item.getSku());
    }

    public double getSum() {
        double sum = 0;
        for (int i = 0; i < items.size(); i++) {
            sum = sum + items.get(i).subtotal();
        }
        return sum;
    }

    public double getDiscount() {
        double sum = Math.round(getSum() * 100) / 100.0;
        if (sum >= 200) {
            return getSum() * 0.05;
        }
        return 0;
    }

    public void printReceipt() {
        System.out.println("Receipt:");
        for (int i = 0; i < items.size(); i++) {
            CartItem it = items.get(i);
            System.out.println(it.getSku() + " " + it.getType() + " qty: " + it.getQuantity()
                    + " price: " + String.format("%.2f", it.getUnitPrice())
                    + " subtotal: " + String.format("%.2f", it.subtotal()));
        }
        System.out.println("Sum: " + String.format("%.2f", getSum()));
        System.out.println("Discount: " + String.format("%.2f", getDiscount()));
        System.out.println("Total: " + String.format("%.2f", getSum() - getDiscount()));
    }
}

public class ShoppingCartApp {

    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
        test5();
    }

    static void test1() {
        System.out.println("Test 1: merge");
        Cart cart = new Cart();
        cart.addItem(new GroceryItem("RICE", 2, 10));
        cart.addItem(new GroceryItem("RICE", 3, 10));
        cart.printReceipt();
        System.out.println();
    }

    static void test2() {
        System.out.println("Test 2: different price and different type");
        Cart cart = new Cart();
        cart.addItem(new GroceryItem("RICE", 2, 10));
        cart.addItem(new GroceryItem("RICE", 1, 12));
        cart.addItem(new DigitalItem("RICE", 1, 10));
        cart.printReceipt();
        System.out.println();
    }

    static void test3() {
        System.out.println("Test 3: quantity 0 and -2");
        try {
            new GroceryItem("MILK", 0, 2);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        try {
            new DigitalItem("EBOOK", -2, 10);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println();
    }

    static void test4() {
        System.out.println("Test 4: total below 200");
        Cart cart = new Cart();
        cart.addItem(new GroceryItem("APPLE", 10, 2));
        cart.addItem(new DigitalItem("GAME", 1, 50));
        cart.printReceipt();
        System.out.println();
    }

    static void test5() {
        System.out.println("Test 5: total exactly 200 and above 200");
        Cart cart = new Cart();
        cart.addItem(new GroceryItem("BREAD", 1, 1));
        cart.addItem(new DigitalItem("SOFT", 1, 173));
        cart.printReceipt();
        cart.addItem(new GroceryItem("BREAD", 9, 1));
        cart.printReceipt();
    }
}
