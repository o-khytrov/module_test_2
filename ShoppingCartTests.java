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
    @ValueSource(strings = {"", "Very long name___________________________"})
    public void AddItem_InvalidName_AddItemFails(String title) {
        ShoppingCart cart = new ShoppingCart();

        Exception thrown = assertThrows(IllegalArgumentException.class, () ->
                cart.addItem(new RegularItem(title,10,1)));
        assertTrue(thrown.getMessage().contains("Illegal title"));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, -1, 0.001, 0.005})
    public void AddItem_InvalidPrice_AddItemFails(double price) {
        ShoppingCart cart = new ShoppingCart();

        Exception thrown = assertThrows(IllegalArgumentException.class, () ->
                cart.addItem(new RegularItem("Test product", price, 1)));
        assertEquals("Illegal price", thrown.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void AddItem_InvalidQuantity_AddItemFails(int quantity) {
        ShoppingCart cart = new ShoppingCart();

        Exception thrown = assertThrows(IllegalArgumentException.class, () ->
                cart.addItem(new RegularItem("Test product", 10, quantity)));

        assertEquals("Illegal quantity", thrown.getMessage());
    }

    @Test
    public void AddItem_AllParametersAreValid_AddItemSucceeds() {

        var title = "Test product";
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new RegularItem(title, 10, 1));
        cart.toString().contains(title);

    }


    @ParameterizedTest
    @MethodSource("getDiscountTestCases")
    public void TestCalculateDiscount(Item item,  int expectedDiscount) {

        var discount = ShoppingCart.calculateDiscount(item);
        assertEquals(expectedDiscount, discount);

    }

    private static Stream<Arguments> getDiscountTestCases() {
        return Stream.of(
                // For NEW item discount is 0%;
                arguments(new NewItem("New item",1,1),0),
                //  For SECOND_FREE item discount is 50% if quantity > 1
                arguments(new SECOND_FREEItem("Second free item",1,2), 50),
                //For SALE item discount is 70%,
                arguments(new SaleItem("Sale item",1,1), 70),
                //For each full 10 not NEW items item gets additional 1% discount,
                arguments(new RegularItem("Regular item",1,10), 1),
                arguments(new RegularItem("Regular item",1,20), 2),
                arguments(new RegularItem("Regular item",1,95), 9),
                //but not more than 80% total
                arguments(new SaleItem("Sale item",1,100), 80),
                arguments(new SaleItem("Sale item",1,850), 80)
        );
    }

    @ParameterizedTest
    @MethodSource("getAppendFormattedTestCases")
    public void appendFormattedTest(String value, int align, int width, String expectedResult) {

        StringBuilder sb = new StringBuilder();
        ShoppingCart.appendFormatted(sb,value,align,width);
        assertEquals(expectedResult, sb.toString());

    }

    private static Stream<Arguments> getAppendFormattedTestCases() {
        return Stream.of(

                //left
                arguments("test", -1, 10,"test       "),

                //right
                arguments("test", 1, 10,"      test "),

                //center
                arguments("test", 0, 10,"   test    "),

                //trim
                arguments("  test   ", 0, 6,"  test ")
        );
    }
}
