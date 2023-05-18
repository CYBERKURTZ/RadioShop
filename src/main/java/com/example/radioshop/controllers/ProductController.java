package com.example.radioshop.controllers;

import com.example.radioshop.repositories.ProductRepository;
import com.example.radioshop.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//КОНТРОЛЛЕР ДЛЯ НЕАУТЕНТИФИЦИРОВАННЫХ ПОЛЬЗОВАТЕЛЕЙ
@Controller
@RequestMapping("/product")
public class ProductController {
    private final ProductRepository productRepository;
    private  final ProductService productService;

    public ProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    //МЕТОД ДЛЯ ПОЛУЧЕНИЯ СПИСКА ВСЕХ ТОВАРОВ
    @GetMapping("")
    public String getAllProduct(Model model){
        //метод getAllProduct - лист всех товаров
        model.addAttribute("products", productService.getAllProduct());
        return "/product/product";
    }

    //МЕТОД ДЛЯ ОБРАБОТКИ НАЖАТИЯ НА ССЫЛКУ 'ПОДРОБНО О ТОВАРЕ'
    @GetMapping("/info/{id}")
    public String infoProduct(@PathVariable("id") int id, Model model){
        //метод getProductId - инфа о конкретном товаре
        model.addAttribute("product", productService.getProductId(id));
        return "/product/infoProduct";
    }

    //МЕТОД ПО РАБОТЕ С ФОРМОЙ 'ПОИСК/СОРТИРОВКА/ФИЛЬТРАЦИЯ'
    @PostMapping("/search")
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
                //ПОЛЯ 'ОТ' И 'ДО' ЗАДАНЫ --- НАИМЕНОВАНИЕ/СОРТИРОВКИ ПО ЦЕНЕ/КАТЕГОРИЯ - НЕ ЗАДАНЫ
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
}
