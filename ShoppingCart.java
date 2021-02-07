import java.util.*;
import java.text.*;
/**
* Containing items and calculating price.
*/
public class ShoppingCart {
    public static enum ItemType { NEW, REGULAR, SECOND_FREE, SALE };


    public void addItem(Item item){
        if (item==null)
            throw new IllegalArgumentException("Parameter can not be null title");

        if (item.title == null || item.title.length() == 0 || item.title.length() > 32)
            throw new IllegalArgumentException("Illegal title");
        if (item.price < 0.01)
            throw new IllegalArgumentException("Illegal price");
        if (item.quantity <= 0)
            throw new IllegalArgumentException("Illegal quantity");

        items.add(item);
    }

    /**
     * Formats shopping price.
     *
     * @return string as lines, separated with \n,
     * first line: # Item Price Quan. Discount Total
     * second line: ---------------------------------------------------------
     * next lines: NN Title $PP.PP Q DD% $TT.TT
     * 1 Some title $.30 2 - $.60
     * 2 Some very long $100.00 1 50% $50.00
     * ...
     * 31 Item 42 $999.00 1000 - $999000.00
     * end line: ---------------------------------------------------------
     * last line: 31 $999050.60
     *
     * if no items in cart returns "No items." string.
     */
    public String formatTicket(){
        if (items.size() == 0)
            return "No items.";
        List<String[]> lines = new ArrayList<String[]>();
        String[] header = {"#","Item","Price","Quan.","Discount","Total"};
        int[] align = new int[] { 1, -1, 1, 1, 1, 1 };
        // formatting each line
        double total = 0.00;
        int index = 0;
        for (Item item : items) {
            int discount = item.CalculateDiscount();
            double itemTotal = item.price * item.quantity * (100.00 - discount) / 100.00;
            lines.add(new String[]{
                String.valueOf(++index),
                item.title,
                MONEY.format(item.price),
                String.valueOf(item.quantity),
                (discount == 0) ? "-" : (String.valueOf(discount) + "%"),
                MONEY.format(itemTotal)
            });
            total += itemTotal;
        }
        String[] footer = { String.valueOf(index),"","","","", MONEY.format(total) };
        // formatting table
        // column max length
        int[] width = new int[]{0,0,0,0,0,0};
        for (String[] line : lines)
            for (int i = 0; i < line.length; i++)
                width[i] = (int) Math.max(width[i], line[i].length());
        for (int i = 0; i < header.length; i++)
            width[i] = (int) Math.max(width[i], header[i].length());
        for (int i = 0; i < footer.length; i++)
            width[i] = (int) Math.max(width[i], footer[i].length());
        // line length
        int lineLength = width.length - 1;
        for (int w : width)
            lineLength += w;
        StringBuilder sb = new StringBuilder();
        // header
        for (int i = 0; i < header.length; i++)
            appendFormatted(sb, header[i], align[i], width[i]);
            sb.append("\n");
        // separator
        for (int i = 0; i < lineLength; i++)
            sb.append("-");
            sb.append("\n");
        // lines
        for (String[] line : lines) {
            for (int i = 0; i < line.length; i++)
                appendFormatted(sb, line[i], align[i], width[i]);
                sb.append("\n");
        }
        if (lines.size() > 0) {
            // separator
            for (int i = 0; i < lineLength; i++)
                sb.append("-");
            sb.append("\n");
        }
        // footer
        for (int i = 0; i < footer.length; i++)
            appendFormatted(sb, footer[i], align[i], width[i]);
        return sb.toString();
    }
    // --- private section -----------------------------------------------------
    private static final NumberFormat MONEY;
        static {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator('.');
            MONEY = new DecimalFormat("$#.00", symbols);
        }
    /**
     * Appends to sb formatted value.
     * Trims string if its length > width.
     * @param align -1 for align left, 0 for center and +1 for align right.
     */
    public static void appendFormatted(StringBuilder sb, String value, int align, int width){
        if (value.length() > width)
            value = value.substring(0,width);
        int before = (align == 0)
            ? (width - value.length()) / 2
            : (align == -1) ? 0 : width - value.length();
        int after = width - value.length() - before;
        while (before-- > 0)
            sb.append(" ");
        sb.append(value);
        while (after-- > 0)
            sb.append(" ");
        sb.append(" ");
    }

    public static int calculateDiscount(Item item){
        int discount = item.CalculateDiscount();
        if (discount < 80) {
            discount += item.quantity / 10;
        if (discount > 80)
            discount = 80;
        }
        return discount;
    }


    /** Container for added items */
    private List<Item> items = new ArrayList<Item>();
}

