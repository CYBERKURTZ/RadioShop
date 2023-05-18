package com.example.radioshop.repositories;

import com.example.radioshop.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    //МЕТОД ДЛЯ НАХОЖДЕНИЯ ПОЛЬЗОВАТЕЛЯ ПО id
    List<Cart> findByPersonId(int id);

    //МЕТОД ДЛЯ УДАЛЕНИЯ ТОВАРА ИЗ КОРЗИНЫ ПО id
    @Modifying
    @Query(value = "DELETE FROM product_cart WHERE product_id=?1", nativeQuery = true)
    void deleteCartByProductId(int id);
}