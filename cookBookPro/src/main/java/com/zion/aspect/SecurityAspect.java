package com.zion.aspect;

import com.zion.bean.AuthMessage;
import com.zion.service.KafkaProducerService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;


@Component
@Aspect
public class SecurityAspect {

    @Autowired
    KafkaProducerService kafkaProducerService;

    @Before("execution(* com.zion.controller.*.*(..))")
    public void validateTokenAspect() throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String method = request.getMethod();
        String url = request.getRequestURI();
        String fullPath = url + "_" + method;
        AuthMessage authMessage;

        if (method.equals("POST") || method.equals("PUT") || method.equals("DELETE")) {
            if (!fullPath.equals("/api/recipes_POST")) {
                authMessage = new AuthMessage("ADMIN", authorizationHeader);
                kafkaProducerService.sendMessage(authMessage);
            }
        }
    }

}

