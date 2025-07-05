import java.util.*;
import java.time.LocalDate;

class Product{

    private String name;
    private int quantity;
    private double price;
    private LocalDate expiryDate;
    private Double weight;

    public Product(String name, int quantity, double price, LocalDate expiryDate, Double weight){
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.expiryDate = expiryDate;
        this.weight = weight;
    }

    public String getName(){
        return name;
    }

    public int getQuantity(){
        return quantity;
    }

    public double getPrice(){
        return price;
    }

    public LocalDate getExpiryDate(){
        return expiryDate;
    }

    public double getWeight(){
        return weight;
    }

    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDate.now());
    }

    public boolean isAvailable() {
        return quantity > 0 && !isExpired();
    }

    public boolean isShippable(){
        // System.out.println("product is shippable?" + weight != null);
        return (weight != null);
    }

    public void reduceQuantity(int amount) {
        if (amount <= quantity) {
            quantity -= amount;
        }
    }

}

class Customer{

    private String name;
    private double balance;

    public Customer(String name, double balance){
        this.name = name;
        this.balance = balance;
    }
 
    public String getName(){
        return name;
    }

    public double getBalance(){
        return balance;
    }   

    public void reduceBalance(double amount) {
        if (amount <= balance) {
            balance -= amount;
        } else {
            throw new IllegalArgumentException("Insufficient balance");
        }
    }

}

class CartItem{

    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
    public Product getProduct() {
        return product;
    }
    public int getQuantity() {
        return quantity;
    }
    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }

}

class Cart{

    public  List<CartItem> items;

    public Cart(){
        items = new ArrayList<>();
    }
  
    public void addProduct(Product product, int quantity){

        if (product.isAvailable()){
            items.add(new CartItem(product, quantity));    
            product.reduceQuantity(quantity);  
        } else {
            throw new IllegalArgumentException("Product is not available");
        }

    }

    public void remove(Product product){
        items.removeIf(item -> item.getProduct().equals(product));
    }

    public boolean isEmpty(){
        return items.isEmpty();
    }

    public double getSubtotal(){
        return items.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    // Calculate shipping fees (assuming 10 per kg)
    public double getShippingFees(){
        double totalWeight = 0;
        for (CartItem item : items){
            if (item.getProduct().isShippable()){
                totalWeight += item.getProduct().getWeight() * item.getQuantity();
            }
        }
        return totalWeight * 10; // 10 currency units per kg
    }

    public void clear() {
        items.clear();
    }

}

interface Shippable{
    String getName();
    double getWeight();
}

class ShippableItem implements Shippable {

    private Product product;
    private int quantity;

    public ShippableItem(Product product, int quantity){
        this.product = product;
        this.quantity = quantity;
    }

    @Override
    public String getName(){
        return product.getName();
    }

    @Override
    public double getWeight(){
        return product.getWeight() * quantity;
    }

    public int getQuantity() {
        return quantity;
    }

}

class ShippingService{

    public void process(List<ShippableItem> items){

        if (items.isEmpty()){
            throw new IllegalArgumentException("No items to ship");
        }
        System.out.println("Shipping Service Noticed");
        double totalWeight = 0;
        for(ShippableItem item: items){
            System.out.printf("%dx %s %.0fg%n", 
            item.getQuantity(), 
            item.getName(), 
            item.getWeight() * 1000); 
            totalWeight += item.getWeight();
        }
        System.out.printf("Total package weight %.1fkg%n", totalWeight);
        System.out.printf("Shipment package fees %.1fLE%n", totalWeight * 10);
        System.out.println();

    }

}

class CheckoutService{

    public static void checkout(Customer customer, Cart cart){

        if(cart.isEmpty()){
            throw new IllegalArgumentException("Cart is empty");
        }
        List<ShippableItem> shippableItems = new ArrayList<>();
        for(CartItem item : cart.items){
            if(item.getProduct().isShippable()){
                // System.out.printf("%sjannana",item.getProduct().getName());
                shippableItems.add(new ShippableItem(item.getProduct(), item.getQuantity()));
            }
        }
        double total = cart.getSubtotal() + cart.getShippingFees();
        if(customer.getBalance() < total){
            throw new IllegalArgumentException("Customer hasn't enough balance");
        }
        if(!shippableItems.isEmpty()){
            ShippingService shippingService = new ShippingService();
            shippingService.process(shippableItems);
        }
        System.out.println("** Checkout receipt **");
        for(CartItem item : cart.items){
            System.out.printf("%dx %s %.0fLE%n", 
            item.getQuantity(), 
            item.getProduct().getName(), 
            item.getTotalPrice());
        }
   
        System.out.println("----------------------");
        System.out.printf("Subtotal %.0f\n", cart.getSubtotal());
        System.out.printf("Shipping %.0f\n", cart.getShippingFees());
        System.out.printf("Amount %.0f\n",  total);
        System.out.println("----------------------");
        customer.reduceBalance(total);
        System.out.printf("Balance left %.0f\n", customer.getBalance());
        System.out.println("----------------------");
        cart.clear();


    }
}


public class eCommerce {

    public static void main(String[] args){

        Product cheese = new Product("Cheese", 100, 10, LocalDate.of(2025, 7, 10), 0.2);
        Product biscuits = new Product("Biscuits", 150, 5, LocalDate.of(2025, 7, 6), 0.7);
        Product tv = new Product("TV", 5000, 3000, null, 8.0);
        Product scratchCard = new Product("Scratch Card", 50, 10, null, null);

        Customer customer = new Customer("Jane", 10000);

        Cart cart = new Cart();

        cart.addProduct(cheese, 2);      
        cart.addProduct(biscuits, 1);    
        cart.addProduct(scratchCard, 1); 
        cart.addProduct(tv, 1);          

        try {
            CheckoutService.checkout(customer, cart);
        } catch (Exception e) {
            System.err.println("Checkout failed: " + e.getMessage());
        }

    }
}

//3035
//91