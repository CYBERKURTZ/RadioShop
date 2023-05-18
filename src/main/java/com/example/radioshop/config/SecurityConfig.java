package com.example.radioshop.config;

import com.example.radioshop.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig{
    private final PersonDetailsService personDetailsService;

    @Bean
    public PasswordEncoder getPasswordEncode(){
        return new BCryptPasswordEncoder();
    }

    //МЕТОД, В КОТОРОМ ПРОПИСАНЫ ВСЕ НАСТРОЙКИ АУТЕНТИФИКАЦИИ
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //ОТКЛЮЧАЕМ ЗАЩИТУ ОТ МЕЖСАЙТОВОЙ ПОДДЕЛКИ ЗАПРОСОВ
        //.csrf().disable()

        //КОНФИГУРИРУЕМ РАБОТУ Spring Security
        http
                //УКАЗЫВАЕМ ЧТО ВСЕ СТРАНИЦЫ ДОЛЖНЫ БЫТЬ ЗАЩИЩЕНЫ АУТЕНТИФИКАЦИЕЙ
                .authorizeHttpRequests()

                //УКАЗЫВАЕМ, ЧТО СТРАНИЦА /ADMIN ДОСТУПНА ПОЛЬЗОВАТЕЛЮ С РОЛЬЮ ADMIN
                .requestMatchers("/admin").hasRole("ADMIN")

                //УКАЗЫВАЕМ ЧТО НЕАУТЕНТИФИЦИРОВАННЫЕ ПОЛЬЗОВАТЕЛЯ МОГУТ ЗАЙТИ НА СТРАНИЦУ АУТЕНТИФИКАЦИИ И НА ОБЪЕКТ ОШИБКИ
                //C ПОМОЩЬЮ permitAll УКАЗЫВАЕМ ЧТО НЕАУТЕНТИФИЦИРОВАННЫЕ ПОЛЬЗОВАТЕЛИ МОГУТ ЗАХОДИТЬ НА ПЕРЕЧИСЛЕННЫЕ СТРАНИЦЫ
                .requestMatchers("/authentication", "/registration", "/error", "/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/product", "/product/info/{id}", "/product/search").permitAll()

                .anyRequest().hasAnyRole("USER", "ADMIN")

                //УКАЗЫВАЕМ, ЧТО ДАЛЬШЕ НАСТРАИВАЕТСЯ АУТЕНТИФИКАЦИЯ И СОЕДИНЯЕМ ЕЕ С НАСТРОЙКОЙ ДОСТУПА
                .and()

                //УКАЗЫВАЕМ КАКОЙ URL ЗАПРОС БУДЕТ ОТПРАВЛЯТСЯ ПРИ ЗАХОДЕ НА ЗАЩИЩЕННЫЕ СТРАНИЦЫ
                .formLogin().loginPage("/authentication")

                //УКАЗЫВАЕМ НА КАКОЙ АДРЕС БУДУТ ОТПРАВЛЯТЬСЯ ДАННЫЕ С ФОРМЫ. НАМ УЖЕ НЕ НУЖНО БУДЕТ СОЗДАВАТЬ МЕТОД В КОНТРОЛЛЕРЕ И ОБРАБАТЫВАТЬ ДАННЫЕ С ФОРМЫ. МЫ ЗАДАЛИ URL, КОТОРЫЙ ИСПОЛЬЗУЕТСЯ ПО УМОЛЧАНИЮ ДЛЯ ОБРАБОТКИ ФОРМЫ АУТЕНТИФИКАЦИИ ПОСРЕДСТВОМ Spring Security. Spring Security БУДЕТ ЖДАТЬ ОБЪЕКТ С ФОРМЫ АУТЕНТИФИКАЦИИ И ЗАТЕМ СВЕРЯТЬ ЛОГИН И ПАРОЛЬ С ДАННЫМИ В БД
                .loginProcessingUrl("/process_login")

                //УКАЗЫВАЕМ НА КАКОЙ URL НЕОБХОДИМО НАПРАВИТЬ ПОЛЬЗОВАТЕЛЯ ПОСЛЕ УСПЕШНОЙ АУТЕНТИФИКАЦИИ. ВТОРЫМ АРГУМЕНТОМ УКАЗЫВАЕТСЯ TRUE ЧТОБЫ ПЕРЕНАПРАВЛЕНИЕ ШЛО В ЛЮБОМ СЛУЧАЕ ПОСЛУ УСПЕШНОЙ АУТЕНТИФИКАЦИИ
                .defaultSuccessUrl("/person account", true)

                //УКАЗЫВАЕМ КУДА НЕОБХОДИМО ПЕРЕНАПРАВИТЬ ПОЛЬЗОВАТЕЛЯ ПРИ ПРОВАЛЕННОЙ АУТЕНТИФИКАЦИИ. В ЗАПРОС БУДЕТ ПЕРЕДАН ОБЪЕКТ error, КОТОРЫЙ БУДЕТ ПРОВЕРЯТЬСЯ НА ФОРМЕ И ПРИ НАЛИЧИИ ДАННОГО ОБЪЕКТА В ЗАПРОСЕ ВЫВОДИТСЯ СООБЩЕНИЕ "НЕПРАВИЛЬНЫЙ ЛОГИН ИЛИ ПАРОЛЬ"
                .failureUrl("/authentication?error")

                .and()

                .logout().logoutUrl("/logout").logoutSuccessUrl("/authentication");
        return http.build();
    }

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        //УКАЗЫВАЕМ, ЧТО ПАРОЛЬ НУЖНО ХЭШИРОВАТЬ МЕТОДОМ getPasswordEncode, КОТОРЫЙ РАБОТАЕТ НА ФУНКЦИИ BCryptPasswordEncoder
        authenticationManagerBuilder.userDetailsService(personDetailsService)
                .passwordEncoder(getPasswordEncode());
    }
}
