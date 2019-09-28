package servlet.model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private Long id;
    private String orderNumber;
    private List<OrderRow> orderRows;

    public Order() {}

    public Order(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<OrderRow> getOrderRows() {
        return orderRows;
    }

    public void add(OrderRow orderRow) {
        if (orderRows == null) {
            orderRows = new ArrayList<>();
        }

        orderRows.add(orderRow);
    }

    @Override
    public String toString() {
        if (orderRows != null) {
            String orderRowsToString = "";
            for (int i = 0; i < orderRows.size(); i++) {
                if (i == orderRows.size() - 1) {
                    orderRowsToString = orderRowsToString.concat(orderRows.get(i).toString()+"\n");
                } else {
                    orderRowsToString = orderRowsToString.concat(orderRows.get(i).toString() + ",\n");
                }
            }
            return "{" + " \"id\": " + "\"" + id + "\"" + ",\"orderNumber\": \"" + orderNumber + "\"," +
                    "\"orderRows\":[" + orderRowsToString + "]" + "}";
        }
        return "{" + " \"id\": " + "\"" + id + "\"" + ", \"orderNumber\": \"" + orderNumber + "\"}";
    }
}
