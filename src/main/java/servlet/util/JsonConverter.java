package servlet.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import servlet.model.Order;
import servlet.model.ValidationErrors;

import java.io.IOException;
import java.util.List;

public class JsonConverter {

    public static void main(String[] args) throws Exception {

    }

    public static String convertOrdersListToJson(List<Order> orderList) throws IOException {
        return new ObjectMapper().writeValueAsString(orderList);
    }

    public static String convertOrderObjectToJson(Order order) throws JsonProcessingException {

        return new ObjectMapper().writeValueAsString(order);
    }

    public static Order convertJsonToOrderObject(String json) throws IOException {

        return new ObjectMapper().readValue(json, Order.class);
    }

    public static String convertValidationObjectToJson(ValidationErrors validationErrors) throws JsonProcessingException {

        return new ObjectMapper().writeValueAsString(validationErrors);
    }

    public static ValidationErrors convertJsonToValidationObject(String json) throws IOException {

        return new ObjectMapper().readValue(json, ValidationErrors.class);
    }
}
