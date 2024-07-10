package com.zion.service;

import com.zion.bean.AuthMessage;
import com.zion.feign.FeignCookBook;
import com.zion.service.impl.JwtServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    @Autowired
    JwtService jwtService;
    @Autowired
    JwtServiceImpl jwtServiceImpl;
    @Autowired
     UserService userService;

    @Autowired
    FeignCookBook feignCookBook;

    @KafkaListener(topics = "securityTopics", groupId = "Campaign", containerFactory = "actionListener")
    public boolean listen(AuthMessage authMessage) {
        boolean isAuthFlag = false;
        try {
            String token = authMessage.getToken().substring(7);
            String role = authMessage.getRole();
            String username = jwtServiceImpl.extractUserName(token);
            UserDetails userDetails = userService.userDetailsService()
                    .loadUserByUsername(username);
            boolean isTokenValid = jwtService.isTokenValid(token, userDetails);
            if (isTokenValid) {
                String userTokenRole = jwtServiceImpl.extractUserRole(token);
                isAuthFlag=role.equals(userTokenRole);
                return isAuthFlag;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        finally {
            feignCookBook.isAuth(isAuthFlag);
        }
    }

}
