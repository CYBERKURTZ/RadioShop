package com.example.radioshop.models;

import jakarta.persistence.*;

import java.util.List;

//МОДЕЛЬ 'КАТЕГОРИИ ТОВАРА'
@Entity
public class Category {

    //ПЕРВИЧНЫЙ КЛЮЧ
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //НАИМЕНОВАНИЕ КАТЕГОРИИ
    private String title;

    //СВЯЗЬ ТОВАРА С КАТЕГОРИЯМИ ---> один ко многим
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<Product> product;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }
}
