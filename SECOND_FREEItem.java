public class SECOND_FREEItem extends Item {
    SECOND_FREEItem(String title, double price, int quantity) {
        super(title, price, quantity);
    }

    @Override
    public int CalculateDiscount() {
        if (quantity > 1)
            return 50;
        return 0;
    }
}
