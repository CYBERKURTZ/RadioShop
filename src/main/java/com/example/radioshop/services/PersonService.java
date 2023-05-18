package com.example.radioshop.services;

import com.example.radioshop.models.Person;
import com.example.radioshop.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    //МЕТОД ДЛЯ ХЭШИРОВАНИЯ ПАРОЛЕЙ
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //МЕТОД ДЛЯ РЕГИСТРАЦИИ ПОЛЬЗОВАТЕЛЯ. СОХРАНЕНИЕ ПОЛЬЗОВАТЕЛЯ В БД
    @Transactional
    public void register(Person person){
        //НАСТРОЙКА ДЛЯ ХЭШИРОВАНИЯ
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        personRepository.save(person);
    }

    //МЕТОД ДЛЯ ПОИСКА ПОЛЬЗОВАТЕЛЯ ПО ЛОГИНУ. МЕТОД РАБОТАЕТ ПРИ ВАЛИДАЦИИ
    public Person findByLogin(Person person){
        Optional<Person> person_db = personRepository.findByLogin(person.getLogin());
        return person_db.orElse(null);
    }

    //МЕТОД ДЛЯ ПОИСКА ВСЕХ ПОЛЬЗОВАТЕЛЕЙ
    public List<Person> findAllUsers() {
        return personRepository.findAll();
    }

    //МЕТОД ДЛЯ ВЫБОРА КОНКРЕТНОГО ПОЛЬЗОВАТЕЛЯ
    public Person findById(int id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        return optionalPerson.orElse(null);
    }

    //МЕТОД ДЛЯ ОБНОВЛЕНИЯ ДАННЫХ ПОЛЬЗОВАТЕЛЯ
    @Transactional
    public void updatePerson(int id, Person person) {
        person.setId(id);
        personRepository.save(person);
    }
}
