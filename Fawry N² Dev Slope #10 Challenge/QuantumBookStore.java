import java.time.LocalDate;
import java.util.*;

class Book{

    protected String isbn;
    protected String title;
    private String author;
    protected LocalDate publicationDate;
    protected double price;   
    protected int quantity;

    public Book(String isbn, String title, String author, LocalDate publicationDate, double price, int quantity) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.price = price;
        this.quantity = quantity;

    }
    
    public String getIsbn(){
        return isbn;
    }
    public String getTitle(){
        return title;
    }
    public LocalDate getPublicationDate(){
        return publicationDate;
    }
    public double getPrice(){
        return price;
    }
    public String getAuthor(){
        return author;
    }
    public int getQuantity(){
        return quantity;
    }
    public void reduceQuantity(int quantity) {
        if (this.quantity >= quantity) {
            this.quantity -= quantity;
        } else {
            throw new IllegalArgumentException("Not enough stock available.");
        }
    }
    public boolean isAvailable() {
        return quantity > 0;
    }

}

class EBook extends Book{

    private String fileType;

    public EBook(String isbn, String title, String author, LocalDate publicationDate, double price, int quantity, String fileType) {
        super(isbn, title, author, publicationDate, price, quantity);
        this.fileType = fileType;
    }
    public void deliverToEmail(String email) {
        System.out.println("Delivering paper book to: " + email);
    }

}
class PaperBook extends Book{

    public PaperBook(String isbn, String title, String author, LocalDate publicationDate, double price, int quantity) {
        super(isbn, title, author, publicationDate, price, quantity);
    }
    public void deliverToAddress(String address) {
        System.out.println("Delivering paper book to: " + address);
    }

}
class DemoBook extends Book{

    public DemoBook(String isbn, String title, String author, LocalDate publicationDate, double price, int quantity) {
        super(isbn, title, author, publicationDate, price, quantity);
    }

}
class Inventory{

    Map<String, Book> books;

    public Inventory() {
        books = new HashMap<>();
    }
    
    public void addBook(Book book) {

        if(book.isAvailable()){
            books.put(book.getIsbn(), book);
        }
        else
            throw new IllegalArgumentException("Book is not available for addition.");

    }
    public Book removeExpiredBooks(int yearsThreshold) {

        for (Map.Entry<String, Book> entry : books.entrySet()){
            Book book = entry.getValue();
            if (book.getPublicationDate().plusYears(yearsThreshold).isBefore(LocalDate.now())) {
                System.out.println("Quantum book store: Book expired - " + book.getTitle());
                books.remove(book.getIsbn());
                return book;
            }
        }
        return null;
    }

    public void removeBook(String isbn){
        books.remove(isbn);
    }
    public double buyBook(String isbn, int quantity, String email, String address) {

        Book book = books.get(isbn);

        if (book != null && book.isAvailable() && book.getQuantity() >= quantity) {

            book.reduceQuantity(quantity);

            if (book instanceof EBook) {
                ((EBook) book).deliverToEmail(email);
            } else if (book instanceof PaperBook) {
                ((PaperBook) book).deliverToAddress(address);
            }
            return book.getPrice() * quantity;

        } else 
            throw new IllegalArgumentException("Book not available or insufficient stock.");
        
    }
   
}

public class QuantumBookStore {

    public static void main(String[] args) {

        Inventory inventory = new Inventory();

        EBook ebook = new EBook("124548", "Effective Java", "Joshua Bloch",
                LocalDate.of(2017, 12, 27), 45.99, 5, "PDF");

        PaperBook paperBook = new PaperBook("666666", "Design Patterns", "Eric Freeman",
                LocalDate.of(2004, 10, 25), 39.99, 3);

        PaperBook oldBook = new PaperBook("7897855", "Old Book", "Old Author",
                LocalDate.of(2000, 1, 1), 19.99, 2);

        System.out.println("\n->Add Books");
        inventory.addBook(ebook);
        inventory.addBook(paperBook);
        inventory.addBook(oldBook);
        System.out.println(" Books added to inventory ");
    
  
        System.out.println("\n->Buying EBook ");
        double cost1 = inventory.buyBook(ebook.getIsbn(), 1, "test@email.com", "");
        System.out.println("Bought EBook, total: $" + cost1);
    

        System.out.println("\n->Buying PaperBook ");
        double cost2 = inventory.buyBook(paperBook.getIsbn(), 2, "", "123 Street");
        System.out.println("Bought PaperBook, total: $" + cost2);
    

        System.out.println("\n->Buying More Than Available ");
        try {
            inventory.buyBook(paperBook.getIsbn(), 10, "", "Some Address");
        } catch (IllegalArgumentException e) {
            System.out.println("Expected failure: " + e.getMessage());
        }
    
        // Remove expired books (>10 years old)
        System.out.println("\n->Removing Expired Books ");
        Book removed = inventory.removeExpiredBooks(10);
        if (removed != null)
            System.out.println("Removed expired book: " + removed.getTitle());
    
        // Try to buy removed book
        System.out.println("\n->Buying Removed Book ");
        try {
            inventory.buyBook(oldBook.getIsbn(), 1, "", "Nowhere");
        } catch (IllegalArgumentException e) {
            System.out.println("Expected failure: " + e.getMessage());
        }
    
        System.out.println("\n Test Completed ");
    }
    
    
}