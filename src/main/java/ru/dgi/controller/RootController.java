package ru.dgi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/")
    public String root() {
        return "redirect:login";
    }

    @GetMapping(value = "/admin/quests")
    public String quests() {
        return "admin/quests";
    }

    @GetMapping(value = "/admin/quest")
    public String edit() {
        return "admin/quest";
    }

    @GetMapping(value = "/my-quests")
    public String myQuests() {
        return "my-quests";
    }

    @GetMapping(value = "/my-quest")
    public String myQuest() {
        return "my-quest";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }



}
