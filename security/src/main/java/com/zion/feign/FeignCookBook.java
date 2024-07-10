package com.zion.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "FeignCookBook", url = "http://localhost:8080/api/recipes")
public interface FeignCookBook {

    @GetMapping("/valid_token")
    void isAuth(@RequestParam("res") Boolean res);

}