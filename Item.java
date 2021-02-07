/**
 * item info
 */
public abstract class Item {
    String title;
    double price;
    int quantity;

    Item(String title, double price, int quantity) {

        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }


    public abstract int CalculateDiscount();
}
