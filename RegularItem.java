public class RegularItem extends Item {
    RegularItem(String title, double price, int quantity) {
        super(title, price, quantity);
    }

    @Override
    public int CalculateDiscount() {
        return 0;
    }
}
