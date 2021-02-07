public class NewItem extends Item {
    NewItem(String title, double price, int quantity) {
        super(title, price, quantity);
    }

    @Override
    public int CalculateDiscount() {
        return 0;
    }
}
