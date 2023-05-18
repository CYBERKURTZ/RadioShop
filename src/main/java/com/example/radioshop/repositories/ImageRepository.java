package com.example.radioshop.repositories;

import com.example.radioshop.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer>{

    //МЕТОД ДЛЯ УДАЛЕНИЯ ФОТОГРАФИЙ ТОВАРА ПО ID
    @Modifying
    @Query(value = "DELETE FROM image WHERE product_id=?1", nativeQuery = true)
    void deleteImageByProductId(int id);

    @Query(value = "SELECT * FROM image WHERE product_id=?1", nativeQuery = true)
    List<Image> findAllImageForProductId (int id);
}

