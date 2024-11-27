package com.app.sales.controller;

import com.github.javafaker.Faker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ChuckNorrisController {

    private final Faker faker = new Faker();

    @GetMapping("/api/chuck-norris")
    public Map<String, String> getRandomChuckNorrisFact() {
        Map<String, String> response = new HashMap<>();
        response.put("fact", faker.chuckNorris().fact());
        return response;
    }
}