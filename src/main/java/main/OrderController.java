package main;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderDao dao;

    @Data
    public static class DateDto {
        @JsonFormat(pattern="yyyy-MM-dd")
        private LocalDate date = LocalDate.now();
    }

    @GetMapping("orders")
    public List<Order> getOrders() {
        return dao.getAllOrders();
    }

    @PostMapping("orders")
    public Order saveOrder(@RequestBody @Valid Order order) {
        return dao.insertOrder(order);
    }

//
//    @GetMapping("orders/{id}")
//    public Order getOrderById(@PathVariable Long id) {
//        return dao.getOrderById(id);
//    }
//
//    @GetMapping("orders/{id}/installments")
//    public List<Installment> getInstallments(
//            @PathVariable Long id,
//            @RequestParam Map<String, String> requestParams) {
//
//        return dao.getInstallmentsForOrderById(id, requestParams.get("start"), requestParams.get("end"));
//    }

//    @DeleteMapping("orders/delete/{id}")
//    public void deleteOrder(@PathVariable Long id) {
//        dao.deleteOrderById(id);
//    }
}
