package com.example.radioshop.controllers;

import com.example.radioshop.enumm.*;
import com.example.radioshop.models.*;
import com.example.radioshop.repositories.*;
import com.example.radioshop.security.*;
import com.example.radioshop.services.*;
import com.example.radioshop.util.*;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//МЕТОДЫ РЕГИСТРАЦИИ/АВТОРИЗАЦИИ ПОЛЬЗОВАТЕЛЯ
@Controller
public class MainController {
    private final ProductRepository productRepository;
    private final PersonValidator personValidator;
    private final PersonService personService;
    private final ProductService productService;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    //ВНЕДРЕНИЕ ПОЛЕЙ
    public MainController(ProductRepository productRepository, PersonValidator personValidator, PersonService personService, ProductService productService, CartRepository cartRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.personValidator = personValidator;
        this.personService = personService;
        this.productService = productService;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }

    //МЕТОД ДЛЯ СОЗДАНИЯ ОБЪЕКТА СЕССИИ, ПОЗВОЛЯЕТ НЕ ВВОДИТЬ ПОВТОРНО ПАРОЛЬ/ЛОГИН ЗАДАННОЕ ВРЕМЯ
    @GetMapping("/person account")
    public String index(Model model){
        //ПОЛУЧАЕМ ОБЪЕКТ АУТЕНТИФИКАЦИИ -> С ПОМОЩЬЮ springcontextholder ОБРАЩАЕМСЯ К КОНТЕКСТУ И НА НЕМ ВЫЗЫВАЕМ МЕТОД АУТЕНТИФИКАЦИИ. ИЗ СЕССИИ ТЕКУЩЕГО ПОЛЬЗОВАТЕЛЯ ПОЛУЧАЕМ ОБЪЕКТ, КОТОРЫЙ БЫЛ ПОЛОЖЕН В ДАННУЮ СЕССИЮ ПОСЛЕ АУТЕНТИФИКАЦИИ ПОЛЬЗОВАТЕЛЯ
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //ПРЕОБРАЗОВЫВАЕМ ЭТОТ ОБЪЕКТ В PersonDetails
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String role = personDetails.getPerson().getRole();
        //ПРОВЕРКА РОЛИ. ЕСЛИ ADMIN, ТО ПЕРЕХОД НА admin.html
        if(role.equals("ROLE_ADMIN")){
            return "redirect:/admin";
        }
        //ОТОБРАЖЕНИЕ СПИСКА ВСЕХ ТОВАРОВ НА ЛИЧНОЙ СТРАНИЦЕ ПОЛЬЗОВАТЕЛЯ
        model.addAttribute("products", productService.getAllProduct());
        //ЕСЛИ USER, ТО ПЕРЕХОД НА index.html
        return "/user/index";
    }

    //МЕТОД ДЛЯ ВОЗВРАТА ФОРМЫ РЕГИСТРАЦИИ
    @GetMapping("/registration")
    public String registration(@ModelAttribute("person") Person person){
        return "registration";
    }

    //МЕТОД ДЛЯ ОБРАБОТКИ ДАННЫХ С ФОРМЫ РЕГИСТРАЦИИ
    @PostMapping("/registration")
    public String resultRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()){
            return "registration";
        }
        personService.register(person);
        return "redirect:/person account";
    }

    //МЕТОД ДЛЯ ВОЗВРАТА КАРТОЧКИ С ОПРЕДЕЛЕННЫМ ТОВАРОМ
    @GetMapping("/person account/product/info/{id}")
    public String infoProduct(@PathVariable("id") int id, Model model){
        model.addAttribute("product", productService.getProductId(id));
        return "/user/infoProduct";
    }

    //МЕТОД ПО РАБОТЕ С ФОРМОЙ 'ПОИСК/СОРТИРОВКА/ФИЛЬТРАЦИЯ' ДЛЯ АУТЕНТИФИЦИРОВАННЫХ ПОЛЬЗОВАТЕЛЕЙ
    @PostMapping("/person account/product/search")
    public String productSearch(@RequestParam("search") String search,
                                @RequestParam("ot") String Ot,
                                @RequestParam("do") String Do,
                                @RequestParam(value = "price", required = false, defaultValue = "") String price,
                                @RequestParam(value = "contract", required = false, defaultValue = "") String contract,
                                Model model){
        //МЕТОД ДЛЯ ОТОБРАЖЕНИЯ СПИСКА ВСЕХ ТОВАРОВ
        model.addAttribute("products", productService.getAllProduct());
        //ПРОВЕРКА ПОЛЕЙ 'ОТ' И 'ДО'
        if (!Ot.isEmpty() & !Do.isEmpty()){
            //ПРОВЕРКА ПОЛЕЙ СОРТИРОВКИ ПО ЦЕНЕ
            if (!price.isEmpty()){
                if (price.equals("sorted_by_ascending_price")){ //СОРТИРОВКА ПО ВОЗРАСТАНИЮ
                    //ПРОВЕРКА ПОЛЕЙ ВЫБОРА КАТЕГОРИИ
                    if (!contract.isEmpty()){
                        //ПОЛЯ 'ОТ' И 'ДО' ЗАДАНЫ И КАТЕГОРИЯ ВЫБРАНА
                        //УСТАНОВЛЕНИЕ ВЫБРАННОЙ КАТЕГОРИИ ---> СРАБОТАЕТ СООТВЕТСТВУЮЩИЙ МЕТОД ИЗ РЕПОЗИТОРИЯ
                        if (contract.equals("oscilloscope")){
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do), 1));
                        } else if (contract.equals("signal_generator")){
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do), 2));
                        } else if (contract.equals("spectrum_analyzer")){
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do), 3));
                        } else if (contract.equals("amplifier")){
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do), 4));
                        }
                        //ПОЛЯ 'ОТ' И 'ДО' ЗАДАНЫ --- КАТЕГОРИЯ - НЕ ЗАДАНА
                    } else {
                        model.addAttribute("search_product", productRepository.findByTitleOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do)));
                    }
                } else if (price.equals("sorted_by_descending_price")){ //СОРТИРОВКА ПО УБЫВАНИЮ
                    //ПРОВЕРКА ПОЛЕЙ ВЫБОРА КАТЕГОРИИ
                    if (!contract.isEmpty()){
                        System.out.println(contract);
                        //ПОЛЯ 'ОТ' И 'ДО' ЗАДАНЫ И КАТЕГОРИЯ ВЫБРАНА
                        //УСТАНОВЛЕНИЕ ВЫБРАННОЙ КАТЕГОРИИ ---> СРАБОТАЕТ СООТВЕТСТВУЮЩИЙ МЕТОД ИЗ РЕПОЗИТОРИЯ
                        if (contract.equals("oscilloscope")){
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do), 1));
                        } else if (contract.equals("signal_generator")){
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do), 2));
                        } else if (contract.equals("spectrum_analyzer")){
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do), 3));
                        } else if (contract.equals("amplifier")){
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do), 4));
                        }
                        //ПОЛЯ 'ОТ' И 'ДО' ЗАДАНЫ --- КАТЕГОРИЯ - НЕ ЗАДАНА
                    } else {
                        model.addAttribute("search_product", productRepository.findByTitleOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do)));
                    }
                }
                //ПОЛЯ 'ОТ' И 'ДО' КАТЕГОРИЯ ЗАДАНЫ --- НАИМЕНОВАНИЕ/СОРТИРОВКА ПО ЦЕНЕ - НЕ ЗАДАНЫ
            } else if (!contract.isEmpty()){
                if (contract.equals("oscilloscope")){
                    model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do), 1));
                } else if (contract.equals("signal_generator")){
                    model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do), 2));
                } else if (contract.equals("spectrum_analyzer")){
                    model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do), 3));
                } else if (contract.equals("amplifier")){
                    model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do), 4));
                } else {
                    model.addAttribute("search_product", productRepository.findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do)));
                }
            }
            //НАИМЕНОВАНИЕ ЗАДАНО --- ПОЛЯ 'ОТ' И 'ДО'/СОРТИРОВКА ПО ЦЕНЕ/КАТЕГОРИЯ - НЕ ЗАДАНЫ
        } else {
            model.addAttribute("search_product", productRepository.findByTitleContainingIgnoreCase(search));
        }
        //МЕТОДЫ ДЛЯ СОХРАНЕНИЯ ДАННЫХ, ВВЕДЕНЫХ В ФОРМУ, ПРИ ПЕРЕЗАГРУЗКЕ СТРАНИЦЫ
        model.addAttribute("value_search", search);
        model.addAttribute("value_price_ot", Ot);
        model.addAttribute("value_price_do", Do);
        return "/product/product";
    }

    //МЕТОД ДЛЯ ОБРАБОТКИ НАЖАТИЯ НА КНОПКУ 'ДОБАВИТЬ В КОРЗИНУ'
    @GetMapping("/cart/add/{id}")
    public String addProductInCart(@PathVariable("id") int id, Model model){
        //ПОЛУЧАЕМ ПРОДУКТ ПО id
        Product product = productService.getProductId(id);
        //ИЗВЛЕКАЕМ ОБЪЕКТ АУТЕНТИФИЦИРОВАННОГО ПОЛЬЗОВАТЕЛЯ
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        //ИЗВЛЕКАЕМ id ПОЛЬЗОВАТЕЛЯ ИЗ ОБЪЕКТА
        int id_person = personDetails.getPerson().getId();
        //ФОРМИРУЕМ НОВУЮ КОРЗИНУ
        Cart cart = new Cart(id_person, product.getId());
        //СОХРАНЕНИЕ КОРЗИНЫ В БД
        cartRepository.save(cart);
        return "redirect:/cart";
    }

    //ОБРАБОТКА redirect КОРЗИНЫ ---> ВОЗВРАТ КОРЗИНЫ С ДОБАВЛЕННЫМИ ТОВАРАМИ
    @GetMapping("/cart")
    public String cart(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        //ИЗВЛЕКАЕМ id ПОЛЬЗОВАТЕЛЯ ИЗ ОБЪЕКТА
        int id_person = personDetails.getPerson().getId();
        //МЕТОД ДЛЯ ПОИСКА КОРЗИНЫ В БД ДЛЯ СООТВЕТСТВУЮЩЕГО ПОЛЬЗОВАТЕЛЯ
        List<Cart> cartList = cartRepository.findByPersonId(id_person);
        //МЕТОД ДЛЯ ПОЛУЧЕНИЯ СПИСКА ВСЕХ ТОВАРОВ ДЛЯ ОТОБРАЖЕНИЯ В КОРЗИНЕ
        List<Product> productList = new ArrayList<>();
        //ПОЛУЧАЕМ ТОВАРЫ ИЗ КОРЗИНЫ ПО id ТОВАРА
        for (Cart cart: cartList) {
            productList.add(productService.getProductId(cart.getProductId()));
        }
        //ВЫЧИСЛЯЕМ ИТОГОВУЮ ЦЕНУ
        float price = 0;
        for (Product product: productList) {
            price += product.getPrice();
        }
        //ДОБАВЛЯЕМ ЦЕНУ В МОДЕЛЬ
        model.addAttribute("price", price);
        //ДОБАВЛЯЕМ ТОВАРЫ КОРЗИНЫ В МОДЕЛЬ
        model.addAttribute("cart_product", productList);
        return "/user/cart";
    }

    //МЕТОД ДЛЯ УДАЛЕНИЯ ТОВАРА ИЗ КОРЗИНЫ ПО id
    @GetMapping("/cart/delete/{id}")
    public String deleteProductFromCart(Model model, @PathVariable("id") int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        //ИЗВЛЕКАЕМ id ПОЛЬЗОВАТЕЛЯ ИЗ ОБЪЕКТА
        int id_person = personDetails.getPerson().getId();
        //МЕТОД ДЛЯ ПОИСКА КОРЗИНЫ В БД ДЛЯ СООТВЕТСТВУЮЩЕГО ПОЛЬЗОВАТЕЛЯ
        List<Cart> cartList = cartRepository.findByPersonId(id_person);
        //МЕТОД ДЛЯ ПОЛУЧЕНИЯ СПИСКА ВСЕХ ТОВАРОВ ДЛЯ ОТОБРАЖЕНИЯ В КОРЗИНЕ
        List<Product> productList = new ArrayList<>();
        //ПОЛУЧАЕМ ПРОДУКТЫ ИЗ КОРЗИНЫ по id ТОВАРА
        for (Cart cart: cartList) {
            productList.add(productService.getProductId(cart.getProductId()));
        }
        //УДАЛЕНИЕ ПО id
        cartRepository.deleteCartByProductId(id);
        return "redirect:/cart";
    }

    //МЕТОД ДЛЯ ОФОРМЛЕНИЯ ЗАКАЗА ИЗ ТОВАРОВ В КОРЗИНЕ
    @GetMapping("/order/create")
    public String order(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        //ИЗВЛЕКАЕМ id ПОЛЬЗОВАТЕЛЯ ИЗ ОБЪЕКТА
        int id_person = personDetails.getPerson().getId();
        //МЕТОД ДЛЯ ПОИСКА КОРЗИНЫ В БД ДЛЯ СООТВЕТСТВУЮЩЕГО ПОЛЬЗОВАТЕЛЯ
        List<Cart> cartList = cartRepository.findByPersonId(id_person);
        //МЕТОД ДЛЯ ПОЛУЧЕНИЯ СПИСКА ВСЕХ ТОВАРОВ ДЛЯ ОТОБРАЖЕНИЯ В КОРЗИНЕ
        List<Product> productList = new ArrayList<>();
        //ПОЛУЧАЕМ ТОВАРЫ ИЗ КОРЗИНЫ ПО id ТОВАРА
        for (Cart cart: cartList) {
            productList.add(productService.getProductId(cart.getProductId()));
        }
        //ВЫЧИСЛЯЕМ ИТОГОВУЮ ЦЕНУ
        float price = 0;
        for (Product product: productList) {
            price += product.getPrice();
        }
        //ГЕНЕРИРУЕМ УНИКАЛЬНЫЙ НОМЕР ЗАКАЗА
        String uuid = UUID.randomUUID().toString();
        //ПЕРЕБИРАЕМ ТОВАРЫ, КОТОРЫЕ ДОЛЖНЫ ДОБАВИТЬСЯ В ЗАКАЗ
        for(Product product : productList){
            Order newOrder = new Order(uuid, product, personDetails.getPerson(), 1, product.getPrice(), Status.ОФОРМЛЕН);
            //СОХРАНЯЕМ ОБЪЕКТ ЗАКАЗА В БД
            orderRepository.save(newOrder);
            //ОЧИЩАЕМ КОРЗИНУ
            cartRepository.deleteCartByProductId(product.getId());
        }
        return "redirect:/orders";
    }

    //МЕТОД ДЛЯ ОБРАБОТКИ НАЖАТИЯ НА КНОПКУ 'ЗАКАЗЫ'
    @GetMapping("/orders")
    public String orderUser(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        //ПОЛУЧАЕМ СПИСОК ВСЕХ ЗАКАЗОВ КОНКРЕТНОГО ПОЛЬЗОВАТЕЛЯ
        List<Order> orderList = orderRepository.findByPerson(personDetails.getPerson());
        model.addAttribute("orders", orderList);
        return "/user/orders";
    }
}
