package servlet;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import servlet.helpers.JsonConverter;
import servlet.helpers.Util;
import servlet.model.Order;
import servlet.model.OrderRow;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Orders", urlPatterns = "/api/orders")
public class OrdersServlet extends HttpServlet {

    private List<Order> ordersList;
    private File file;
    private ObjectMapper mapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ordersList = new ArrayList<>();
        file = new File("./src/main/java/servlet/jsonObjects/orders.json");
        mapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String json = Util.readStream(request.getInputStream());
        Order orderObject = JsonConverter.convertJsonToObject(json);

        String orderNumber = orderObject.getOrderNumber();

        //create new order object with unique id
        Order order = new Order();
        order.setId(ordersList.size()+1L);
        order.setOrderNumber(orderNumber);
        if (orderObject.getOrderRows() != null) {
            List<OrderRow> orderRows = orderObject.getOrderRows();
            for (OrderRow row : orderRows) {
                order.add(row);
            }
        }
        ordersList.add(order);

        //add orders into json file
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(file, ordersList);

        response.setHeader("Content-Type", "application/json");
        response.getWriter().print(order.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Order[] orders = mapper.readValue(file, Order[].class);

        Long id = Long.parseLong(request.getParameter("id"));

        response.setHeader("Content-Type", "application/json");
        for (Order order :
                orders) {
            if (order.getId().equals(id)) {
                response.getWriter().print(order.toString());
            }
        }
    }
}
