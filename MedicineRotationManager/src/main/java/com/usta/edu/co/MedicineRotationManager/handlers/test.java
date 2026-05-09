package com.usta.edu.co.MedicineRotationManager.handlers;

import org.springframework.web.bind.annotation.RestController;
import org.aspectj.weaver.ast.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/test")
public class test {
 @GetMapping("test1/{word}")
 public String testGet(@PathVariable String x, @RequestParam(required = false, defaultValue = "gurruple") String y){
    return "test"+x+y;
 }
 @PostMapping("test2/post")
 public String test(@RequestBody Test test){
    return test.toString();

 }
}
