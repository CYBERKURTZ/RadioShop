package com.example.radioshop.services;

import com.example.radioshop.models.Person;
import com.example.radioshop.repositories.PersonRepository;
import com.example.radioshop.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

//UserDetailsService предназначен для работы с аутентификацией
@Service
public class PersonDetailsService implements UserDetailsService {

    //поле, которое будет хранить в себе PersonRepository
    private final PersonRepository personRepository;

    //внедрение через конструктор
    @Autowired
    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    //метод, позволяющий найти пользователя по username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //получаем пользователя из таблицы по логину с формы аутентификации
        Optional<Person> person = personRepository.findByLogin(username);
        //если пользователь не был найден. выбрасываем исключение, что данный пользователь не найден. данное исключение будет поймано Spring Security и сообщение будет выведено на страницу
        if(person.isEmpty()){
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return new PersonDetails(person.get());
    }
}
