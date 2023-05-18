package com.example.radioshop.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//МОДЕЛЬ ДЛЯ ВЗАИМОДЕЙСТВИЯ С ТОВАРАМИ
@Entity
public class Product {

    //ПЕРВИЧНЫЙ КЛЮЧ
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //НАИМЕНОВАНИЕ ТОВАРА
    @Column(name = "title", nullable = false, columnDefinition = "text", unique = true)
    @NotEmpty(message = "Наименование товара не может быть пустым")
    private String title;

    //ОПИСАНИЕ ТОВАРА
    @Column(name = "description", nullable = false, columnDefinition = "text")
    @NotEmpty(message = "Описание товара не может быть пустым")
    private String description;

    //ЦЕНА ТОВАРА
    @Column(name = "price", nullable = false)
    @Min(value = 1, message = "Цена товара не может быть отрицательной или нулевой")
    private float price;

    //СКЛАД ---> МЕСТОПОЛОЖЕНИЕ ТОВАРА
    @Column(name = "warehouse", nullable = false)
    @NotEmpty(message = "Cклад по нахождению товара не может быть пустым")
    private String warehouse;

    //ПОСТАВЩИК
    @Column(name = "seller", nullable = false)
    @NotEmpty(message = "Информация о продавце не может быть пустой")
    private String seller;

    //КАТЕГОРИЯ ТОВАРА
    //СВЯЗЬ ТОВАРА С КАТЕГОРИЯМИ ---> МНОГИЕ К ОДНОМУ
    @ManyToOne(optional = false)
    private Category category;

    //ДАТА И ВРЕМЯ СОЗДАНИЯ НОВОГО ТОВАРА (ПОЛЬЗОВАТЕЛЕМ И АДМИНИСТРАТОРОМ)
    private LocalDateTime dateTime;

    //СВЯЗЬ ТОВАРА С ФОТОГРАФИЯМИ ---> ОДИН КО МНОГИМ
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    private List<Image> imageList = new ArrayList<>();

    //МЕТОД ДЛЯ ДОБАВЛЕНИЯ ФОТОГРАФИИ В ЛИСТ ТЕКУЩЕГО ПРОДУКТА
    public void addImageToProduct(Image image){
        image.setProduct(this);
        imageList.add(image);
    }

    //МЕТОД ДЛЯ ЗАПОЛЕНИЕЯ ПОЛЯ ДАТЫ И ВРЕМЕНИ ПРИ СОЗДАНИИ ТОВАРА (PRODUCT)
    @PrePersist
    private void init(){
        dateTime = LocalDateTime.now();
    }

    //МЕТОД ДЛЯ ОБНОВЛЕНИЯ ПОЛЯ ДАТЫ И ВРЕМЕНИ ПРИ РЕДАКТИРОВАНИИ ТОВАРА (PRODUCT)
    @PreUpdate
    private void initUpdate(){
        dateTime = LocalDateTime.now();
    }

    //СВЯЗЬ ТОВАРОВ С ПОКУПАТЕЛЯМИ В КОРЗИНЕ
    @ManyToMany()
    @JoinTable(name = "product_cart", joinColumns = @JoinColumn(name = "product_id"),inverseJoinColumns = @JoinColumn(name = "person_id"))
    private List<Person> personList;

    //СВЯЗЬ ПОКУПАТЕЛЯ С ЗАКАЗОМ
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<Order> orderList;

    //КОНСТРУКТОР (за исключением id)
    public Product(String title, String description, float price, String warehouse, String seller, Category category, LocalDateTime dateTime, List<Image> imageList) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.warehouse = warehouse;
        this.seller = seller;
        this.category = category;
        this.dateTime = dateTime;
        this.imageList = imageList;
    }

    //ПУСТОЙ КОНСТРУКТОР
    public Product() {}

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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    public String getWarehouse() {
        return warehouse;
    }
    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }
    public String getSeller() {
        return seller;
    }
    public void setSeller(String seller) {
        this.seller = seller;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    public List<Image> getImageList() {
        return imageList;
    }
    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }
}
