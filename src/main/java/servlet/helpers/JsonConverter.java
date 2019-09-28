package servlet.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import servlet.model.Order;

import java.io.IOException;

public class JsonConverter {

    public static void main(String[] args) throws Exception {

    }

    public static String convertObjectToJson(Order order) throws JsonProcessingException {

        return new ObjectMapper().writeValueAsString(order);
    }

    public static Order convertJsonToObject(String json) throws IOException {

        return new ObjectMapper().readValue(json, Order.class);
    }
}
