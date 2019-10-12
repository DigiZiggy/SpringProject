package servlet.jdbc;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {
    private DataSource dataSource;

    public OrderDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Order insertOrder(Order order) {

        String query = "insert into \"order\" (ordernumber) values (?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, new String[] {"id"})) {

            ps.setString(1, order.getOrderNumber());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (!rs.next()) {
                throw new RuntimeException("Id was not assigned");
            }

            Long id = rs.getLong("id");

            if (order.getOrderRows() != null) {
                for (OrderRow row : order.getOrderRows()) {
                    insertOrderRow(row, id);
                }
            }

            return new Order(id, order.getOrderNumber(), order.getOrderRows());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertOrderRow(OrderRow orderRow, Long orderId) {

        String query = "insert into orderrow (order_id, itemname, quantity, price) values (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, new String[] {"id"})) {

            ps.setLong(1, orderId);
            ps.setString(2, orderRow.getItemName());
            ps.setInt(3, orderRow.getQuantity());
            ps.setInt(4, orderRow.getPrice());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (!rs.next()) {
                throw new RuntimeException("Id was not assigned");
            }

            new OrderRow(orderRow.getItemName(),
                    orderRow.getQuantity(),
                    orderRow.getPrice());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Order findOrderById(Long id) {

        String query = "select id, ordernumber from \"order\" where id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                long thisOrderId = rs.getLong("id");
                String thisOrderNumber = rs.getString("ordernumber");

                List<OrderRow> orderRows;
                orderRows = findOrderRowsById(thisOrderId);

                Order order;
                if (orderRows != null && orderRows.isEmpty()) {
                    order = new Order(thisOrderId, thisOrderNumber, null);
                } else {
                    order = new Order(thisOrderId, thisOrderNumber, orderRows);
                }

                return order;
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<OrderRow> findOrderRowsById(Long id) {

        String query = "select id, order_id, itemName, quantity, price from orderRow where order_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            List<OrderRow> orderRows = new ArrayList<>();

            while (rs.next()) {
                OrderRow orderRow = new OrderRow(rs.getString("itemName"),
                        rs.getInt("quantity"),
                        rs.getInt("price"));
                orderRows.add(orderRow);
            }

            if (orderRows.isEmpty()) {
                return null;
            }
            return orderRows;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Order> findOrders() {

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet orderRs = stmt.executeQuery("select id, ordernumber from \"order\"");

            List<Order> orders = new ArrayList<>();

            while(orderRs.next()) {

                long thisOrderId = orderRs.getLong("id");
                List<OrderRow> orderRows = new ArrayList<>();

                orderRows = findOrderRowsById(thisOrderId);

                Order order;
                if (orderRows != null && orderRows.isEmpty()) {
                    order = new Order(orderRs.getLong("id"),
                            orderRs.getString("ordernumber"), null);
                } else {
                    order = new Order(orderRs.getLong("id"),
                            orderRs.getString("ordernumber"), orderRows);
                }

                orders.add(order);
            }
            return orders;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteOrderById(Long id) {

        String query = "delete from \"order\" where id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, id);
            ps.executeQuery();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
