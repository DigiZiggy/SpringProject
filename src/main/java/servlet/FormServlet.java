package servlet;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import servlet.helpers.Util;
import servlet.model.Order;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "FormServlet", urlPatterns = "/orders/form")
public class FormServlet extends HttpServlet {

    private List<Order> ordersList;
    private File file;
    private ObjectMapper mapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ordersList = new ArrayList<>();
        file = new File("./src/main/java/servlet/jsonObjects/orders.json");
        mapper = new ObjectMapper();

        try {
            Order[] orders = mapper.readValue(file, Order[].class);
            ordersList.addAll(Arrays.asList(orders));
        } catch (IOException e) {
            log("Cannot read from file!", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String json = Util.readStream(request.getInputStream());
        String orderNumber = "";

        //find order number using regex matcher
        Pattern pattern = Pattern.compile("([A-Z]\\d+)");
        Matcher orderNumberMatcher = pattern.matcher(json);

        if (orderNumberMatcher.find()) {
            orderNumber = orderNumberMatcher.group(1);
        }

        //create new order object with unique id
        Order order = new Order();
        order.setId(ordersList.size()+1L);
        order.setOrderNumber(orderNumber);
        ordersList.add(order);

        //add orders into json file
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(file, ordersList);

        response.setHeader("Content-Type", "text/plain");
        response.getWriter().print(order.getId());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
