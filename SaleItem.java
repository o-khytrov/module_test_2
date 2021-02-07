public class SaleItem extends Item {
    SaleItem(String title, double price, int quantity) {
        super(title, price, quantity);
    }

    @Override
    public int CalculateDiscount() {
        return 70;
    }
}
