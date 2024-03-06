package org.example.controller;

import org.example.service.IBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author huang
 */
@RestController
public class BusinessController {
    private IBusinessService businessService;

    private Map<String, String> map;

    @Autowired
    public BusinessController(IBusinessService businessService,
                              @Qualifier("mockDataBaseMap") Map<String, String> map) {
        this.businessService = businessService;
        this.map = map;
    }

    @GetMapping("/business")
    public String business(@RequestParam String param) {
        return businessService.business(param);
    }

    @GetMapping("/businessInsert")
    public void businessInsert(@RequestParam Integer id) {
        businessService.businessInsert(id);
    }

    @GetMapping("/businessQueryAndDelete")
    public void businessQueryAndDelete(@RequestParam Integer id) {
        businessService.businessQueryAndDelete(id);
    }

    @GetMapping("/businessQueryAllAndDelete")
    public void businessQueryAllAndDelete() {
        businessService.businessQueryAllAndDelete();
    }

    @GetMapping("/businessAsync")
    public void businessAsync(@RequestParam Integer id) {
        businessService.businessAsync(id);
    }

    @GetMapping("/changeTableName")
    public void changeTableName(@RequestParam String busType, @RequestParam String tableName) {
        map.put(busType, tableName);
    }

    @GetMapping("/businessMultiInsert")
    public void businessMultiInsert(@RequestParam Integer startId, @RequestParam Integer number) {
        businessService.businessMultiInsert(startId, number);
    }

    @GetMapping("/businessDelete")
    public void businessDelete(@RequestParam Integer id) {
        businessService.businessDelete(id);
    }

    @GetMapping("/businessMultiDelete")
    public void businessMultiDelete(@RequestParam Integer startId, @RequestParam Integer number) {
        businessService.businessMultiDelete(startId, number);
    }

    @GetMapping("/businessIncreaseNum")
    public void businessIncreaseNum(@RequestParam Integer id) {
        businessService.businessIncreaseNum(id);
    }
}
