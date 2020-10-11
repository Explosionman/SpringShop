package ru.rybinskov.gb.springshop.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.rybinskov.gb.springshop.shop.dto.BucketDto;
import ru.rybinskov.gb.springshop.shop.service.BucketService;
import ru.rybinskov.gb.springshop.shop.service.ProductServiceImpl;

import java.security.Principal;

@Controller
@RequestMapping("/bucket")
public class BucketController {

    private final BucketService bucketService;
    private final ProductServiceImpl productService;

    public BucketController(BucketService bucketService, ProductServiceImpl productService) {
        this.bucketService = bucketService;
        this.productService = productService;
    }


    @RequestMapping(method = RequestMethod.GET)
    public String aboutBucket(Model model, Principal principal) {
        if (principal == null) {
            model.addAttribute("bucket", new BucketDto());
        } else {
            BucketDto bucketDto = bucketService.getBucketByUser(principal.getName());
            model.addAttribute("bucket", bucketDto);
        }
        return "bucket";
    }

    @GetMapping("/remove/{id}")
    public String deleteOnePieceFromBucket(Model model, @PathVariable("id") Long id, Principal principal) {
        System.out.println("Попали в метод ремув");
        bucketService.deleteFromUserBucket(id, principal.getName());
        return "redirect:/bucket";
    }
}
