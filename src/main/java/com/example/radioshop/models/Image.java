package com.example.radioshop.models;

import jakarta.persistence.*;

//МОДЕЛЬ 'ФОТОГРАФИИ ТОВАРОВ'
@Entity
public class Image {

    //ПЕРВИЧНЫЙ КЛЮЧ
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //НАИМЕНОВАНИЕ ФАЙЛА ФОТОГРАФИИ
    private String fileName;

    //СВЯЗЬ ФОТОГРАФИИ С ТОВАРОМ ---> многие к одному
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Product product;

    //КОНСТРУКТОР
    public Image(int id, String fileName, Product product) {
        this.id = id;
        this.fileName = fileName;
        this.product = product;
    }

    //ПУСТОЙ КОНСТРУКТОР
    public Image() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
