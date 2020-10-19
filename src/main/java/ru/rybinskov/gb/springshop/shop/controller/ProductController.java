package ru.rybinskov.gb.springshop.shop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.rybinskov.gb.springshop.shop.domain.Bucket;
import ru.rybinskov.gb.springshop.shop.domain.Product;
import ru.rybinskov.gb.springshop.shop.domain.User;
import ru.rybinskov.gb.springshop.shop.dto.BucketDto;
import ru.rybinskov.gb.springshop.shop.service.BucketService;
import ru.rybinskov.gb.springshop.shop.service.ProductServiceImpl;
import ru.rybinskov.gb.springshop.shop.service.SessionObjectHolder;
import ru.rybinskov.gb.springshop.shop.service.UserService;


import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductServiceImpl productService;
    private final UserService userService;
    private final BucketService bucketService;

    public ProductController(ProductServiceImpl productService, UserService userService, BucketService bucketService, SessionObjectHolder sessionObjectHolder) {
        this.productService = productService;
        this.userService = userService;
        this.bucketService = bucketService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model, Principal principal) {
        BucketDto bucketDto = bucketService.getBucketByUser(principal.getName());
        User user = userService.findByName(principal.getName());
        List<Product> products = productService.getAll();
        model.addAttribute("products", products);
        model.addAttribute("bucket", bucketDto);
        model.addAttribute("user_bucket", user.getBucket());
        return "products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(Model model, @PathVariable("id") Long id) {
        productService.deleteById(id);
        return "redirect:/products";
    }

    @GetMapping("/filterByMin")
    public String filterByMin(Model model) {
        List<Product> products = productService.getAllFilteredByMinPrice();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/filterByMax")
    public String filterByMax(Model model) {
        List<Product> products = productService.getAllFilteredByMaxPrice();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/filterById")
    public String filterById(Model model) {
        List<Product> products = productService.getAll();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping(params = {"startPrice", "endPrice"})
    public String productsByPrice(Model model,
                                  @RequestParam(name = "startPrice") Long startPrice,
                                  @RequestParam(name = "endPrice") Long endPrice) {
        List<Product> products = productService.getPriceByRange(startPrice, endPrice);
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/edit/{id}")
    public String getFormEditProduct(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        return "edit-product";
    }

    @PostMapping("/edit")
    public String editProduct(@ModelAttribute Product product) {
        System.out.println(product);
        productService.save(product);
        return "redirect:/products";
    }

    @GetMapping("/new")
    public String getFormNewProduct(Model model) {
        model.addAttribute("product", new Product());
        return "new-product";
    }


    @PostMapping("/new")
    public String addNewProduct(Product product) {
        productService.save(product);
        return "redirect:/products/";
    }

    // @GetMapping("/{id}/bucket")
    @MessageMapping("/{id}/bucket")
//    public String addBucket(@PathVariable Long id, Principal principal) {
//        if (principal == null) {
//            return "redirect:/products";
//        }
//        productService.addToUserBucket(id, principal.getName());
//        return "redirect:/products";
//    }
    public void addToBucket(Product product, Principal principal) {
        productService.addToUserBucket(product.getId(), principal.getName());
    }

    @PostMapping
    public ResponseEntity<Void> addProductToBucket(Product product, Principal principal) {
        productService.addToUserBucket(product.getId(), principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}