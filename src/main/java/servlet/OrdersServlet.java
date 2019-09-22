package servlet;

import servlet.model.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "Orders", urlPatterns = "/api/orders")
public class OrdersServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String string = Util.readStream(request.getInputStream());
        String orderNumber = "";

        //find order number using regex matcher
        Pattern pattern = Pattern.compile("([A-Z]\\d+)");
        Matcher matcher = pattern.matcher(string);
        if (matcher.find())
        {
            orderNumber = matcher.group(1);
        }

        //create new order object with unique id
        Order order = new Order();
        order.setId(order.getCount()+1L);
        order.setOrderNumber(orderNumber);

        response.setHeader("Content-Type", "application/json");
        response.getWriter().print(order.toString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.getWriter().println("Orders!");
    }
}
