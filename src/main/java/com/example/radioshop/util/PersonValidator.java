package com.example.radioshop.util;

import com.example.radioshop.models.Person;
import com.example.radioshop.services.PersonService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PersonService personService;

    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    //В ДАННОМ МЕТОДЕ УКАЗЫВАЕМ ДЛЯ КАКОЙ МОДЕЛИ ПРЕДНАЗНАЧЕН ДАННЫЙ ВАЛИДАТОР
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    //В ЭТОМ МЕТОДЕ ПРОПИСАНА САМА ВАЛИДАЦИЯ
    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        if(personService.findByLogin(person) != null){
            //ЕСЛИ ОБЪЕКТ НЕ ПУСТОЙ, ТО ВЫВОДИМ ОШИБКУ
            errors.rejectValue("login", "", "Данный логин уже зарегистрирован в системе");
        }
    }
}
