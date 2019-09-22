package servlet.model;

public class Order {

    private static int count = 0;
    private Long id;
    private String orderNumber;

    public Order() {}

    public Order(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public void setId(Long id) {
        this.id = id;
        count++;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return "{" + " \"id\": " + "\"" + id + "\"" + ", \"orderNumber\": \"" + orderNumber + "\"}";
    }
}
