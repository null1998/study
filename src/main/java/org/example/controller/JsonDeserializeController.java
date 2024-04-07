package org.example.controller;

import org.example.entity.JsonDeserializeDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huang
 */
@RestController
@RequestMapping("/jsonDeserialize")
public class JsonDeserializeController {
    @PostMapping
    public void test(@RequestBody JsonDeserializeDTO jsonDeserializeDTO) {
        System.out.println(jsonDeserializeDTO.getStartDate());
    }
}
