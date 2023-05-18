package com.example.radioshop.repositories;

import com.example.radioshop.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    //МЕТОД, КОТОРЫЙ ПОЗВОЛИТ НАЙТИ ЗАПИСЬ В СУЩНОСТИ Person ПО ЛОГИНУ
    Optional<Person> findByLogin(String login);

    //МЕТОД ДЛЯ РАБОТЫ СО СПИСКОМ ПОЛЬЗОВАТЕЛЕЙ
    @Override
    List<Person> findAll();
}

