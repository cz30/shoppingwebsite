package com.bjf;

import com.bjf.service.BjfUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderMapperTests {

    @Autowired
    private BjfUserService userService;

    @Test
    public void addIntegral() {
        userService.addIntegral(10, 111);
        System.err.println(userService.getIntegral(10));
    }

}
