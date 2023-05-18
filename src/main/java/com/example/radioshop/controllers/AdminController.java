package com.example.radioshop.controllers;

import com.example.radioshop.enumm.*;
import com.example.radioshop.models.*;
import com.example.radioshop.repositories.*;
import com.example.radioshop.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

//КОНТРОЛЛЕР ДЛЯ АДМИНА
@Controller
public class AdminController {

    //ПУТЬ, В КОТОРОМ ХРАНЯТСЯ ФОТОГРАФИИ
    @Value("${upload.path}")
    private String uploadPath;

    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final ImageService imageService;
    private final PersonService personService;


    //КОНСТРУКТОР ДЛЯ ВНЕДРЕНИЯ
    public AdminController(ProductService productService, CategoryRepository categoryRepository, OrderService orderService, OrderRepository orderRepository, ImageService imageService, PersonService personService) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.imageService = imageService;
        this.personService = personService;
    }

    //МЕТОД ДЛЯ ОБРАБОТКИ НАЖАТИЯ НА КНОПКУ 'ДОБАВИТЬ ТОВАР' В ЛК АДМИНА
    @GetMapping("admin/product/add")
    public String addProduct(Model model){
        //ПРИВЯЗЫВАЕМ ОБЪЕКТ ПРОДУКТА К МОДЕЛИ
        model.addAttribute("product", new Product());
        //ПРИВЯЗЫВАЕМ СПИСОК КАТЕГОРИЙ К МОДЕЛИ
        model.addAttribute("category", categoryRepository.findAll());
        return "product/addProduct";
    }

    //МЕТОД ДЛЯ ПРИНЯТИЯ ЗНАЧЕНИЙ С ФОРМЫ ДОБАВЛЕНИЯ (ФОТО И КАТЕГОРИЯ)
    @PostMapping("/admin/product/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult,
                             @RequestParam("file_one") MultipartFile file_one,
                             @RequestParam("file_two") MultipartFile file_two,
                             @RequestParam("file_three") MultipartFile file_three,
                             @RequestParam("file_four") MultipartFile file_four,
                             @RequestParam("file_five") MultipartFile file_five,
                             @RequestParam("category") int category,
                             Model model) throws IOException {
        Category category_db = categoryRepository.findById(category).orElseThrow();
        //ПРОВЕРКА НА ОШИБКИ
        if(bindingResult.hasErrors()){
            model.addAttribute("category", categoryRepository.findAll());
            return "product/addProduct";
        }
        //ПРОВЕРКА НА ОТСУТСТВИЕ ФАЙЛА И СОХРАНЕНИЕ
        if(file_one != null){
            File uploadDir = new File(uploadPath);
            //ПРОВЕРКА НА СУЩЕСТВОВАНИЕ ДИРЕКТОРИИ
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            //ГЕНЕРАЦИЯ УНИКАЛЬНОГО id
            String uuidFile = UUID.randomUUID().toString();
            //ГЕНЕРАЦИЯ НАИМЕНОВАНИЯ ФАЙЛА
            String resultFileName = uuidFile + "." + file_one.getOriginalFilename();
            //ОТПРАВКА ФАЙЛА ПО УКАЗАННОЙ ДИРЕКТОРИИ
            file_one.transferTo(new File(uploadPath + "/" + resultFileName));
            //ПРИВЯЗКА ФОТОГРАФИИ К ФАЙЛУ
            Image image = new Image();
            //ПРИВЯЗКА ФАЙЛА ИЗОБРАЖЕНИЯ К ПРОДУКТУ
            image.setProduct(product);
            //УКАЗАНИЕ НАИМЕНОВАНИЯ ФАЙЛА ФОТОГРАФИИ
            image.setFileName(resultFileName);
            //ДОБАВЛЕНИЕ В ЛИСТ ФОТОГРАФИЙ
            product.addImageToProduct(image);
        }
        if(file_two != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_two.getOriginalFilename();
            file_two.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        if(file_three != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_three.getOriginalFilename();
            file_three.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        if(file_four != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_four.getOriginalFilename();
            file_four.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        if(file_five != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_five .getOriginalFilename();
            file_five .transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        //СОХРАНЕНИЕ ЗАПИСИ О ДОБАВЛЕННОМ ПРОДУКТЕ
        productService.saveProduct(product, category_db);
        return "redirect:/admin/product";
    }

    //МЕТОД ДЛЯ ВОЗВРАТА ШАБЛОНА АДМИНА
    @GetMapping("/admin")
    public String admin() {
        return "admin/admin";
    }

    //МЕТОД ДЛЯ ВОЗВРАТА ШАБЛОНА СО ВСЕМИ ТОВАРАМИ
    @GetMapping("/admin/product")
    public String admin(Model model) {
        //по переходу по ссылке /admin/product передаем также все продукты, которые есть в репозитории
        model.addAttribute("products", productService.getAllProduct());
        return "admin/infoProduct";
    }

    //МЕТОД ДЛЯ УДАЛЕНИЯ ТОВАРА ИЗ ЛИСТА В ЛК АДМИНА ПО ССЫЛКЕ
    @GetMapping("admin/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id){
        productService.deleteProduct(id);
        return "redirect:/admin/product";
    }

    //МЕТОД ДЛЯ ПЕРЕХОДА НА ШАБЛОН РЕДАКТИРОВАНИЯ ТОВАРА ИЗ ЛИСТА В ЛК АДМИНА ПО ССЫЛКЕ
    @GetMapping("admin/product/edit/{id}")
    public String editProduct(Model model, @PathVariable("id") int id){
        model.addAttribute("product", productService.getProductId(id));
        model.addAttribute("category", categoryRepository.findAll());
        return "product/editProduct";
    }

    //МЕТОД ДЛЯ ОБРАБОТКИ ФОРМЫ РЕДАКТИРОВАНИЯ
    @PostMapping("admin/product/edit/{id}")
    public String editProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult,
                              @PathVariable("id") int id,
                              @RequestParam("file_one") MultipartFile file_one,
                              @RequestParam("file_two") MultipartFile file_two,
                              @RequestParam("file_three") MultipartFile file_three,
                              @RequestParam("file_four") MultipartFile file_four,
                              @RequestParam("file_five") MultipartFile file_five,
                              Model model) throws IOException {
        //ПРОВЕРКА НА ОШИБКИ
        if(bindingResult.hasErrors()){
            model.addAttribute("category", categoryRepository.findAll());
            return "product/editProduct";
        }
        //МЕТОД ДЛЯ УДАЛЕНИЯ ТЕКУЩИХ ФОТО ТОВАРА
        imageService.deleteImageByProductId(product.getId());
        //ПРОВЕРКА НА ОТСУТСТВИЕ ФАЙЛА И СОХРАНЕНИЕ
        if(!file_one.isEmpty()){
            File uploadDir = new File(uploadPath);
            //ПРОВЕРКА НА СУЩЕСТВОВАНИЕ ДИРЕКТОРИИ
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            //ГЕНЕРАЦИЯ УНИКАЛЬНОГО id
            String uuidFile = UUID.randomUUID().toString();
            //ГЕНЕРАЦИЯ НАИМЕНОВАНИЯ ФАЙЛА
            String resultFileName = uuidFile + "." + file_one.getOriginalFilename();
            //ОТПРАВКА ФАЙЛА ПО УКАЗАННОЙ ДИРЕКТОРИИ
            file_one.transferTo(new File(uploadPath + "/" + resultFileName));
            //ПРИВЯЗКА ФОТОГРАФИИ К ФАЙЛУ
            Image image = new Image();
            //ПРИВЯЗКА ФАЙЛА ИЗОБРАЖЕНИЯ К ПРОДУКТУ
            image.setProduct(product);
            //УКАЗАНИЕ НАИМЕНОВАНИЯ ФАЙЛА ФОТОГРАФИИ
            image.setFileName(resultFileName);
            //ДОБАВЛЕНИЕ В ЛИСТ ФОТОГРАФИЙ
            product.addImageToProduct(image);
        }
        if(!file_two.isEmpty()){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_two.getOriginalFilename();
            file_two.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        if(!file_three.isEmpty()){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_three.getOriginalFilename();
            file_three.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        if(!file_four.isEmpty()){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_four.getOriginalFilename();
            file_four.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        if(!file_five.isEmpty()){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_five .getOriginalFilename();
            file_five .transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        //СОХРАНЕНИЕ ЗАПИСИ О ДОБАВЛЕННОМ ПРОДУКТЕ
        productService.updateProduct(id, product);
        return "redirect:/admin/product";
    }

    //МЕТОД ДЛЯ ВОЗВРАТА ШАБЛОНА СО ВСЕМИ ЗАКАЗАМИ
    @GetMapping("/admin/orders")
    public String showAllOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("status", Status.values());
        return "/admin/orders";
    }

    //МЕТОД ДЛЯ ПОИСКА ЗАКАЗОВ ПО ПОСЛЕДНИМ 4 СИМВОЛАМ
    @PostMapping("/admin/order/search")
    public String orderSearch(@RequestParam("search") String search, Model model){
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("search_order", orderRepository.findOrderByLastFourChar(search));
        model.addAttribute("value_search", search);
        return "/admin/orders";
    }

    //МЕТОД ДЛЯ ВОЗВРАТА ПОДРОБНОЙ ИНФОРМАЦИИ О ЗАКАЗЕ С ВОЗМОЖНОСТЬЮ СМЕНЫ СТАТУСА
    @GetMapping("/admin/order/{id}")
    public String orderInfo(@PathVariable("id") int id, Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        model.addAttribute("status", Status.values());
        return "/admin/orderInfo";
    }

    //МЕТОД ДЛЯ ОБНОВЛЕНИЯ СТАТУСА ЗАКАЗА
    @PostMapping("/admin/order/{id}")
    public String statusOrder(@ModelAttribute("status") Status status, @PathVariable("id") int id){
        Order order = orderService.getOrderById(id);
        order.setStatus(status);
        orderService.updateOrder(id, order);
        return "redirect:/admin/order/{id}";
    }

    //МЕТОД ДЛЯ ВОЗВРАТА ИНФЫ О ВСЕХ ПОЛЬЗОВАТЕЛЯХ
    @GetMapping("/admin/users")
    public String showAllUsers(Model model) {
        model.addAttribute("users", personService.findAllUsers());
        model.addAttribute("role", Role.values());
        return "/admin/users";
    }

    //МЕТОД ДЛЯ ОТОБРАЖЕНИЯ ПОДРОБНОЙ ИНФЫ О ВЫБРАННОМ ПОЛЬЗОВАТЕЛЕ
    @GetMapping("/admin/user/{id}")
    public String userInfo(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", personService.findById(id));
        model.addAttribute("role", Role.values());
        String a=Role.ROLE_USER.name();
        System.out.println(personService.findById(id).getRole().equals(Role.ROLE_USER.name()));
        return "/admin/userInfo";
    }

    //МЕТОД ДЛЯ ИЗМЕНЕНИЯ РОЛИ ПОЛЬЗОВАТЕЛЯ
    @PostMapping("/admin/user/{id}")
    public String setUserRole (@ModelAttribute("role") String role, @PathVariable("id") int id){
        Person person = personService.findById(id);
        person.setRole(role);
        personService.updatePerson(id, person);
        return "redirect:/admin/user/{id}";
    }
}
