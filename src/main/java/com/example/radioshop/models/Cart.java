package com.example.radioshop.models;

import jakarta.persistence.*;

//ТАБЛИЦА ДЛЯ СВЯЗИ ПОКУПАТЕЛЕЙ С ТОВАРАМИ ---> КОРЗИНА
@Entity
@Table(name = "product_cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //ПОЛЕ С id ПОЛЬЗОВАТЕЛЯ, К КОТОРОМУ БУДЕТ ПРИВЯЗАНА КОРЗИНА
    @Column(name = "person_id")
    private int personId;

    //ПОЛЕ, ОТВЕЧАЮЩЕЕ ЗА ПРИВЯЗКУ К ПРОДУКТУ
    @Column(name = "product_id")
    private int productId;

    public Cart(int personId, int productId) {
        this.personId = personId;
        this.productId = productId;
    }

    public Cart() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getPersonId() {
        return personId;
    }
    public void setPersonId(int personId) {
        this.personId = personId;
    }
    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }
}
