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

            return new Order(rs.getLong("id"),
                    order.getOrderNumber());

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
                return new Order(rs.getLong("id"),
                        rs.getString("ordernumber"));
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Order> findOrders() {

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("select id, ordernumber from \"order\"");

            List<Order> orders = new ArrayList<>();

            while(rs.next()) {
                Order order = new Order(rs.getLong("id"),
                        rs.getString("ordernumber"));

                orders.add(order);
            }

            return orders;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
