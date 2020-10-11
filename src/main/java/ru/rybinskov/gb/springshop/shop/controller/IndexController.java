package ru.rybinskov.gb.springshop.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping(value = {"","/"})
    public String index(Model model) {
        model.addAttribute("msg", "Скоро открытие!!! " +
                "А пока, взгляните на первый ассортимент.");
        return "index";
    }

    @RequestMapping(value = "/login")
    public String loginPage(){
        return "login";
    }

    @RequestMapping("/login-error")
    public String loginError(Model mOdel){
        mOdel.addAttribute("loginError", true);
        return "login";
    }
}
