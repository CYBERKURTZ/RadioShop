package com.example.radioshop.repositories;

import com.example.radioshop.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

//РЕПОЗИТОРИЙ ДЛЯ ТОВАРОВ
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    //МЕТОДЫ ДЛЯ ПОИСКА, СОРТИРОВКИ, ФИЛЬТРАЦИИ:

    //ПОИСК ВСЕХ ТОВАРОВ ПО ФРАГМЕНТУ НАИМЕНОВАНИЯ ТОВАРА ВНЕ ЗАВИСИМОСТИ ОТ РЕГИСТРА
    List<Product> findByTitleContainingIgnoreCase(String title);

    //ПОИСК ПО НАИМЕНОВАНИЮ И ФИЛЬТРАЦИЯ ПО ДИАПАЗОНУ ЦЕНЫ
    @Query(value = "SELECT * FROM product WHERE ((lower(title) LIKE %?1%) OR (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1')) AND (price >= ?2 AND price <= ?3)", nativeQuery = true)
    List<Product> findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(String title, float Ot, float Do);

    //ПОИСК ПО НАИМЕНОВАНИЮ И ФИЛЬТРАЦИЯ ПО ДИАПАЗОНУ ЦЕНЫ, А ТАКЖЕ СОРТИРОВКА ПО ВОЗРАСТАНИЮ ЦЕНЫ
    @Query(value = "SELECT * FROM product WHERE ((lower(title) LIKE %?1%) OR (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1')) AND (price >= ?2 AND price <= ?3) order by price", nativeQuery = true)
    List<Product> findByTitleOrderByPriceAsc(String title, float Ot, float Do);

    //ПОИСК ПО НАИМЕНОВАНИЮ И ФИЛЬТРАЦИЯ ПО ДИАПАЗОНУ ЦЕНЫ, А ТАКЖЕ СОРТИРОВКА ПО УБЫВАНИЮ ЦЕНЫ
    @Query(value = "SELECT * FROM product WHERE ((lower(title) LIKE %?1%) OR (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1')) AND (price >= ?2 AND price <= ?3) order by price desc", nativeQuery = true)
    List<Product> findByTitleOrderByPriceDesc(String title, float Ot, float Do);

    //ПОИСК ПО НАИМЕНОВАНИЮ И ФИЛЬТРАЦИЯ ПО ДИАПАЗОНУ ЦЕНЫ, СОРТИРОВКА ПО ВОЗРАСТАНИЮ ЦЕНЫ, А ТАКЖЕ ФИЛЬТРАЦИЯ ПО КАТЕГОРИИ
    @Query(value = "SELECT * FROM product WHERE category_id = ?4 AND ((lower(title) LIKE %?1%) OR (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1')) AND (price >= ?2 AND price <= ?3) order by price", nativeQuery = true)
    List<Product> findByTitleAndCategoryOrderByPriceAsc(String title, float Ot, float Do, int category);

    //ПОИСК ПО НАИМЕНОВАНИЮ И ФИЛЬТРАЦИЯ ПО ДИАПАЗОНУ ЦЕНЫ, СОРТИРОВКА ПО УБЫВАНИЮ ЦЕНЫ, А ТАКЖЕ ФИЛЬТРАЦИЯ ПО КАТЕГОРИИ
    @Query(value = "SELECT * FROM product WHERE category_id = ?4 AND ((lower(title) LIKE %?1%) OR (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1')) AND (price >= ?2 AND price <= ?3) order by price desc", nativeQuery = true)
    List<Product> findByTitleAndCategoryOrderByPriceDesc(String title, float Ot, float Do, int category);
}
