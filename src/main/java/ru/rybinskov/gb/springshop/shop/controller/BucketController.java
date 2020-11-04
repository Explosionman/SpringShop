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

    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
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
        bucketService.deleteFromUserBucket(id, principal.getName());
        return "redirect:/bucket";
    }

    @GetMapping("/remove-all/{id}")
    public String deleteAllPiecesFromBucket(Model model, @PathVariable("id") Long id, Principal principal) {
        bucketService.deleteAllFromUserBucket(id, principal.getName());
        return "redirect:/bucket";
    }

    @GetMapping("/plus/{id}")
    public String addOneToBucket(@PathVariable Long id, Principal principal) {
        bucketService.addToUserBucket(id, principal.getName());
        return "redirect:/bucket";
    }
}
