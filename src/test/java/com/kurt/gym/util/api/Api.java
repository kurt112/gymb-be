package com.kurt.gym.util.api;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Component
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations="classpath:test.properties")
public class Api {

    @Value("${server.servlet.context-path}")
    private String basePath;

    @Value("${server.port}")
    private String port;

    public String getUrl(){
        return basePath + port;
    }






}
