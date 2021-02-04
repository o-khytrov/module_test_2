import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ShoppingCartTests {

    @ParameterizedTest
    @ValueSource(strings = {"","Very long name___________________________"})
    public void AddItem_InvalidName_AddItemFails(String title)  {
        ShoppingCart cart = new ShoppingCart();

        Exception thrown = assertThrows(IllegalArgumentException.class, () ->
                cart.addItem(title, 10,1, ShoppingCart.ItemType.NEW));
        assertTrue(thrown.getMessage().contains("Illegal title"));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0,-1,0.001,0.005})
    public void AddItem_InvalidPrice_AddItemFails(double price)  {
        ShoppingCart cart = new ShoppingCart();

        Exception thrown = assertThrows(IllegalArgumentException.class, () ->
                cart.addItem("Test product", price,1, ShoppingCart.ItemType.NEW));
        assertEquals("Illegal price",thrown.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {0,-1})
    public void AddItem_InvalidQuantity_AddItemFails(int quantity)  {
        ShoppingCart cart = new ShoppingCart();

        Exception thrown = assertThrows(IllegalArgumentException.class, () ->
                cart.addItem("Test product", 10,quantity, ShoppingCart.ItemType.NEW));

        assertEquals("Illegal quantity",thrown.getMessage());
    }

   @Test
    public void AddItem_AllParametersAreValid_AddItemSucceeds()  {

        var title  ="Test product";
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(title, 10,1, ShoppingCart.ItemType.NEW);
        cart.toString().contains(title);

    }



    @ParameterizedTest
    @MethodSource("getDiscountTestCases")
    public void TestCalculateDiscount(ShoppingCart.ItemType type, int quantity, int expectedDiscount) {

        var discount  = ShoppingCart.calculateDiscount(type, quantity);
        assertEquals(expectedDiscount,discount);

    }

    private static Stream<Arguments> getDiscountTestCases() {
        return Stream.of(
                // For NEW item discount is 0%;
                arguments(ShoppingCart.ItemType.NEW,1,0) ,
                //  For SECOND_FREE item discount is 50% if quantity > 1
                arguments(ShoppingCart.ItemType.SECOND_FREE,2,50),
                //For SALE item discount is 70%,
                arguments(ShoppingCart.ItemType.SALE,1,70) ,
                //For each full 10 not NEW items item gets additional 1% discount,
                arguments(ShoppingCart.ItemType.REGULAR,10,1),
                arguments(ShoppingCart.ItemType.REGULAR,20,2),
                arguments(ShoppingCart.ItemType.REGULAR,95,9),
                //but not more than 80% total
                arguments(ShoppingCart.ItemType.SALE,100,80),
                arguments(ShoppingCart.ItemType.REGULAR,850,80)
        );
    }
}

