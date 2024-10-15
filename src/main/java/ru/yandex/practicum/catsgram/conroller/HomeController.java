package ru.yandex.practicum.catsgram.conroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/home")
public class HomeController {

    @GetMapping
    public String homePage() {
        int counter = 0;
        return "<h1>Приветствуем вас, в приложении Котограм<h1>";
    }
}
