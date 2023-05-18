package com.example.radioshop.repositories;

import com.example.radioshop.models.Order;
import com.example.radioshop.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

//РЕПОЗИТОРИЙ ДЛЯ ЗАКАЗА
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByPerson(Person person);

    //МЕТОД ДЛЯ ПОИСКА ЗАКАЗА ПО 4 ПОСЛЕДНИМ СИМВОЛАМ
    @Query(value = "SELECT * FROM orders WHERE tnumber LIKE %?1", nativeQuery = true)
    List<Order> findOrderByLastFourChar(String tnumber);
}