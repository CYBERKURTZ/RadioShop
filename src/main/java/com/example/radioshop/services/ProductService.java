package com.example.radioshop.services;

import com.example.radioshop.models.Category;
import com.example.radioshop.models.Product;
import com.example.radioshop.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //МЕТОД ДЛЯ ВЫВОДА СПИСКА ВСЕХ ТОВАРОВ
    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }

    //МЕТОД ДЛЯ ПОЛУЧЕНИЯ ТОВАРА ПО id
    public Product getProductId(int id){
        Optional<Product> optionalProduct = productRepository.findById(id);
        //если товар не найден, возвращается null
        return optionalProduct.orElse(null);
    }

    //МЕТОД ДЛЯ СОХРАНЕНИЯ ТОВАРА
    @Transactional
    public void saveProduct(Product product, Category category){
        product.setCategory(category);
        productRepository.save(product);
    }

    //МЕТОД ДЛЯ ОБНОВЛЕНИЯ ДАННЫХ О ТОВАРЕ
    @Transactional
    public void updateProduct(int id, Product product){
        product.setId(id);
        productRepository.save(product);
    }

    //МЕТОД ДЛЯ УДАЛЕНИЯ ТОВАРА ПО id
    @Transactional
    public void deleteProduct(int id){
        productRepository.deleteById(id);
    }

    //ДОПИСАТЬ МЕТОДЫ ПО РАБОТЕ С МЕТОДАМИ РЕПОЗИТОРИЯ (ПОИСК, СОРТИРОВКА, ФИЛЬТРАЦИЯ)
}
