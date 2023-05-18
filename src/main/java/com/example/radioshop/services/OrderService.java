package com.example.radioshop.services;

import com.example.radioshop.models.Order;
import com.example.radioshop.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    public final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    //ДЛЯ ПОИСКА ЗАКАЗОВ ПО СИМВОЛАМ
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public Order getOrderById(int id){
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.orElse(null);
    }

    //МЕТОД ДЛЯ ОБНОВЛЕНИЯ СТАТУСА ЗАКАЗА
    @Transactional
    public void updateOrder (int id, Order order){
        order.setId(id);
        orderRepository.save(order);
    }
}
