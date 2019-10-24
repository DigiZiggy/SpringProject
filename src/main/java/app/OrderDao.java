package app;

import model.Installment;
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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

    public Order getOrderById(Long id) {

        String query = "select id, ordernumber from \"order\" where id = ?";

        return template.queryForObject(query, new Object[]{id}, new OrderMapper());
    }

    private void insertOrderRow(OrderRow orderRow, Long orderId) {

        orderRow.setOrderId(orderId);
        var data = new BeanPropertySqlParameterSource(orderRow);

        Number id = new SimpleJdbcInsert(template)
                .withTableName("orderRow")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(data);

        new OrderRow(
                id.longValue(),
                orderId,
                orderRow.getItemName(),
                orderRow.getQuantity(),
                orderRow.getPrice());
    }

    public List<Installment> getInstallmentsForOrderById(Long id, String startDate, String endDate) {

        Integer totalAmountForOrder = getOrderTotalAmount(id);

        Long amountOfInstallments = getTotalAmountOfInstallmentMonths(startDate, endDate);

        List<Installment> installments = new ArrayList<>();

        List<Integer> installmentAmounts = getAmountForEachInstallment(
                totalAmountForOrder,
                amountOfInstallments.intValue());

        List<String> installmentDates = getDatesForEachInstallment(startDate, endDate);

        for (int i = 0; i < installmentAmounts.size(); i++) {
            Installment installment = new Installment(
                    installmentAmounts.get(i),
                    installmentDates.get(i));

            installments.add(installment);
        }

        return installments;
    }

    private List<Integer> getAmountForEachInstallment(Integer orderTotal, Integer amountOfInstallments) {

        List<Integer> installmentAmounts = new ArrayList<>();

        int eachAmount = orderTotal / amountOfInstallments;

        int remainder = orderTotal%amountOfInstallments;

        if (eachAmount < 3) {
            int installmentTotal = orderTotal / eachAmount;
            int totalRemainder = orderTotal%eachAmount;

            addInstallmentAmountsToList(installmentAmounts, eachAmount, installmentTotal, totalRemainder);

        } else {
            addInstallmentAmountsToList(installmentAmounts, amountOfInstallments, eachAmount, remainder);
        }

        return installmentAmounts;
    }

    private void addInstallmentAmountsToList(List<Integer> installmentAmounts, int eachAmount, int installmentTotal, int totalRemainder) {
        for (int i = 0; i < eachAmount-totalRemainder; i++) {
            installmentAmounts.add(installmentTotal);
        }
        if (totalRemainder == 2) {
            installmentAmounts.add(installmentTotal+1);
            installmentAmounts.add(installmentTotal+1);
        } else if (totalRemainder == 1){
            installmentAmounts.add(installmentTotal+1);
        }
    }

    private List<String> getDatesForEachInstallment(String startDate, String endDate) {

        List<String> installmentDates = new ArrayList<>();

        int count = getTotalAmountOfInstallmentMonths(startDate, endDate).intValue();

        installmentDates.add(startDate);
        LocalDate date = LocalDate.parse(startDate);
        for (int i = 1; i < count; i++) {
            date = date.plusDays(31);
            String nextInstallmentDate = getFirstDayOfMonthDate(date.toString());
            installmentDates.add(nextInstallmentDate);
        }
        return installmentDates;
    }

    private Integer getOrderTotalAmount(Long id) {

        String query = "select id, ordernumber from \"order\" where id = ?";

        Order order = template.queryForObject(query, new Object[]{id}, new OrderMapper());

        int total = 0;

        assert order != null;
        for (OrderRow row : order.getOrderRows()) {
            total += row.getPrice() * row.getQuantity();
        }

        return total;
    }


    private Long getTotalAmountOfInstallmentMonths(String startDate, String endDate) {

        String start = getFirstDayOfMonthDate(startDate);
        String end = getFirstDayOfMonthDate(endDate);

        return ChronoUnit.MONTHS.between(
                LocalDate.parse(start),
                LocalDate.parse(end).plusDays(31));
    }

    private String getFirstDayOfMonthDate(String date) {

        return date.substring(0, 8) + "01";
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


    private static class OrderRowMapper implements RowMapper<OrderRow> {
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
