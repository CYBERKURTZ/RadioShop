package com.example.radioshop.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//ВКЛЮЧАЕМ СКАНИРОВАНИЕ КОМПОНЕНТОВ
@Configuration
@ComponentScan("com.example.radioshop.util")
public class SpringConfig {}
