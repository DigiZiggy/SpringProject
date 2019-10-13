package servlet.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import servlet.jdbc.Order;

import java.io.IOException;
import java.util.List;

public class JsonConverter {

    public static void main(String[] args) throws Exception {

    }

    public static String convertOrdersListToJson(List<Order> orderList) throws IOException {
        return new ObjectMapper().writeValueAsString(orderList);
    }

    public static String convertObjectToJson(Order order) throws JsonProcessingException {

        return new ObjectMapper().writeValueAsString(order);
    }

    public static Order convertJsonToObject(String json) throws IOException {

        return new ObjectMapper().readValue(json, Order.class);
    }
}
