package com.example.radioshop.security;

import com.example.radioshop.models.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class PersonDetails implements UserDetails { //контекстное меню -> import methods
    private final Person person;

    public PersonDetails(Person person) {
        this.person = person;
    }

    //МЕТОД ДЛЯ ПОЛУЧЕНИЯ ОБЪЕКТА ПОЛЬЗОВАТЕЛЯ
    public Person getPerson(){
        return this.person;
    }

    //ДАННЫЙ МЕТОД ВЕРНЕТ РОЛЬ ПОЛЬЗОВАТЕЛЯ
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(person.getRole()));
    }

    //ДАННЫЙ МЕТОД СРАБАТЫВАЕТ ПРИ ПОЛУЧЕНИИ ПАРОЛЯ ПОЛЬЗОВАТЕЛЯ
    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

    //ДАННЫЙ МЕТОД СРАБАТЫВАЕТ ПРИ ПОЛУЧЕНИИ ЛОГИНА ПОЛЬЗОВАТЕЛЯ
    @Override
    public String getUsername() {
        return this.person.getLogin();
    }

    //АККАУНТ ДЕЙСТВИТЕЛЕН, ПРОВЕРКА ДЕЙСТВИТЕЛЬНОСТИ
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //АККАУНТ НЕ ЗАБЛОКИРОВАН, ПРОВЕРКА БЛОКИРОВКИ
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //ПАРОЛЬ ДЕЙСТВИТЕЛЕН, ПРОВЕРКА ДЕЙСТВИТЕЛЬНОСТИ
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //АККАУНТ АКТИВЕН, ПРОВЕРКА АКТИВНОСТИ АККАУНТА
    @Override
    public boolean isEnabled() {
        return true;
    }
}
