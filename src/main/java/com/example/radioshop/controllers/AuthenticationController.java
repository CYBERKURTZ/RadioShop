package com.example.radioshop.controllers;

import com.example.radioshop.models.Product;
import com.example.radioshop.repositories.CategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//КОНТРОЛЛЕР АУТЕНТИФИКАЦИИ
@Controller
public class AuthenticationController {

    //МЕТОД ДЛЯ ВОЗВРАТА ФОРМЫ АУТЕНТИФИКАЦИИ
    @GetMapping("/authentication")
    public String login(){
        return "authentication";
    }
}
