package servlet;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import servlet.main.ServletContextListener;
import servlet.model.Order;
import servlet.main.OrderDao;
import servlet.model.ValidationError;
import servlet.model.ValidationErrors;
import servlet.util.JsonConverter;
import servlet.util.FileUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collections;
import java.util.List;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@WebServlet(name = "Orders", urlPatterns = "/api/orders")
public class OrdersServlet extends HttpServlet {

    private OrderDao orderDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var ctx = ServletContextListener.contextInitialized();

        orderDao = ctx.getBean(OrderDao.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String orderFromStream = FileUtil.readStream(request.getInputStream());
        Order orderObject = JsonConverter.convertJsonToOrderObject(orderFromStream);

        response.setHeader("Content-Type", "application/json");

        if (orderObject.getOrderNumber().length() < 2) {
            response.setStatus(400);

            ValidationErrors validationErrors = new ValidationErrors();
            ValidationError error = new ValidationError();

            error.setCode("too_short_number");
            validationErrors.setErrors(Collections.singletonList(error));

            response.getWriter().print(JsonConverter.convertValidationObjectToJson(validationErrors));

        } else {
            Order order = orderDao.insertOrder(orderObject);
            response.getWriter().print(JsonConverter.convertOrderObjectToJson(order));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setHeader("Content-Type", "application/json");
        if (request.getParameter("id") == null) {
            List<Order> orders = orderDao.findOrders();
            response.getWriter().print(JsonConverter.convertOrdersListToJson(orders));
        } else {
            Long id = Long.parseLong(request.getParameter("id"));
            Order order = orderDao.findOrderById(id);
            response.getWriter().print(JsonConverter.convertOrderObjectToJson(order));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            orderDao.deleteOrderById(id);
        } catch (NumberFormatException e) {
            log("Only integers allowed for id parameters!", e);
        }
    }
}
