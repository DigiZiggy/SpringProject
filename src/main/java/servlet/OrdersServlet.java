package servlet;

import servlet.jdbc.Order;
import servlet.jdbc.OrderDao;
import servlet.util.DataSourceProvider;
import servlet.util.DbUtil;
import servlet.util.JsonConverter;
import servlet.util.FileUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@WebServlet(name = "Orders", urlPatterns = "/api/orders")
public class OrdersServlet extends HttpServlet {

    private OrderDao orderDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        DataSourceProvider.setConnectionInfo(DbUtil.loadConnectionInfo());
        orderDao = new OrderDao(DataSourceProvider.getDataSource());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String json = FileUtil.readStream(request.getInputStream());
        Order orderObject = JsonConverter.convertJsonToObject(json);

        //create new order object with unique id
        Order order = orderDao.insertOrder(orderObject);

        response.setHeader("Content-Type", "application/json");
        response.getWriter().print(order.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setHeader("Content-Type", "application/json");
        if (request.getParameter("id") == null) {
            List<Order> orders = orderDao.findOrders();
            response.getWriter().print(orders.toString());
        } else {
            Long id = Long.parseLong(request.getParameter("id"));
            Order order = orderDao.findOrderById(id);
            response.getWriter().print(order.toString());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setHeader("Content-Type", "application/json");
        Long id = Long.parseLong(request.getParameter("id"));
        orderDao.deleteOrderById(id);
    }
}
