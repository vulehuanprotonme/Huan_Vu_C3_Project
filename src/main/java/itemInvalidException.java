public class itemInvalidException extends Throwable {
    public itemInvalidException(String itemName, int price) {
        super(itemName + " - price: " + price);
    }
}
