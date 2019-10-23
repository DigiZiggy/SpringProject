package app;

import model.Order;
import model.OrderRow;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Repository
public class OrderDao {

    private JdbcTemplate template;

    @Autowired
    public OrderDao(JdbcTemplate template) {
        this.template = template;
    }

    public Order insertOrder(Order order) {

        String query = "insert into \"order\" (ordernumber) values (?) returning id";

        Long id = template.queryForObject(query, new Object[] {
                order.getOrderNumber()
        }, long.class);

        if (order.getOrderRows() != null) {
            for (OrderRow row : order.getOrderRows()) {
                insertOrderRow(row, id);
            }
        }
        return new Order(id, order.getOrderNumber(), order.getOrderRows());
    }

    public OrderRow insertOrderRow(OrderRow orderRow, Long orderId) {

        orderRow.setOrderId(orderId);
        var data = new BeanPropertySqlParameterSource(orderRow);

        Number id = new SimpleJdbcInsert(template)
                .withTableName("orderRow")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(data);

        return new OrderRow(
                id.longValue(),
                orderId,
                orderRow.getItemName(),
                orderRow.getQuantity(),
                orderRow.getPrice());
    }

    public Order getOrderById(Long id) {

        String query = "select id, ordernumber from \"order\" where id = ?";

        return template.queryForObject(query, new Object[]{id}, new OrderMapper());
    }

    public List<OrderRow> findOrderRowsById(Long id) {

        String query = "select id, order_id, itemName, quantity, price from orderRow where order_id = ?";

        List<OrderRow> orderRows = template.query(query, new Object[]{id}, new OrderRowMapper());

        if ( orderRows.isEmpty() ){
            return null;
        }else if ( orderRows.size() == 1 ) { // list contains exactly 1 element
            return Collections.singletonList(orderRows.get(0));
        }else{  // list contains more than 1 elements
            return orderRows;
        }
    }

    public List<Order> getAllOrders() {

        String query = "select id, ordernumber from \"order\"";

        return template.query(query, new OrderMapper());
    }

    public void deleteOrderById(Long id) {

        String query = "delete from \"order\" where id = ?";

        template.queryForObject(query, new Object[]{id}, new OrderMapper());
    }

    private class OrderMapper implements RowMapper<Order> {
        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {

            long thisOrderId = rs.getLong("id");
            String thisOrderNumber = rs.getString("ordernumber");

            Order order = new Order();
            order.setId(thisOrderId);
            order.setOrderNumber(thisOrderNumber);

            List<OrderRow> orderRows;

            orderRows = findOrderRowsById(thisOrderId);
            if (orderRows != null && orderRows.isEmpty()) {
                order = new Order(thisOrderId, thisOrderNumber, null);
            } else {
                order = new Order(thisOrderId, thisOrderNumber, orderRows);
            }
            order.setOrderRows(orderRows);

            return order;
        }
    }


    private class OrderRowMapper implements RowMapper<OrderRow> {
        @Override
        public OrderRow mapRow(ResultSet rs, int rowNum) throws SQLException {

            OrderRow orderRow = new OrderRow();
            orderRow.setId(rs.getLong("id"));
            orderRow.setOrderId(rs.getLong("order_id"));
            orderRow.setItemName(rs.getString("itemName"));
            orderRow.setQuantity(rs.getInt("quantity"));
            orderRow.setPrice(rs.getInt("price"));

            return orderRow;
        }
    }
}
