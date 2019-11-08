package main;

import model.Order;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Order insertOrder(Order order) {
        em.persist(order);

        return new Order(order.getId(), order.getOrderNumber(), order.getOrderRows());
    }


    public List<Order> getAllOrders() {

        return em.createQuery("select o from Order o").getResultList();
    }
}
