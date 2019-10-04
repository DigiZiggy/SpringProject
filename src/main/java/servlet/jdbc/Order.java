package servlet.jdbc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private Long id;
    private String orderNumber;

    @Override
    public String toString() {
        return "{" + " \"id\": " + "\"" + id + "\"" + ", \"orderNumber\": \"" + orderNumber + "\"}";
    }
}
