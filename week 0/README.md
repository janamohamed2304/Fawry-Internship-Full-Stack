# 🛒 Java E-Commerce System

A simple Java-based console application that simulates an e-commerce checkout system

---

## 📦 Features

**Product Management**
- Each product has a name, price, quantity, expiry date, and weight.
- Supports both expiring and non-expiring products.
- Supports both shippable (cheese, TV) and non-shippable products (e.g., scratch card).

**Customer Handling**
- Each customer has a name and balance.
- Balance is reduced after successful checkout.

**Shopping Cart**
- Add and remove products.
- Automatically reduces product quantity.
- Assumption: Calculates subtotal and shipping fees (10 LE per kg).

**Shipping Service**
- Processes and prints shipping details for shippable products.

**Checkout Service**
- Validates cart and customer balance.
- Displays receipt with items, costs, and remaining balance.

**Design Pattern in the Code**
- Delegation in CartItem and ShippableItem 
---

## 🧾 Example Output

```
Shipping Service Noticed
2x Cheese 400g
1x Biscuits 700g
1x TV 8000g
Total package weight 9.1kg
Shipment package fees 91.0LE

** Checkout receipt **
2x Cheese 20LE
1x Biscuits 5LE
1x Scratch Card 10LE
1x TV 3000LE
----------------------
Subtotal 3035
Shipping 91
Amount 3126
----------------------
Balance left 6874
----------------------
```