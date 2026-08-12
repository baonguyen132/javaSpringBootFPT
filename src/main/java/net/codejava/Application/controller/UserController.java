package net.codejava.Application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import net.codejava.Application.identityservices.dto.request.UserCreationRequest;
import net.codejava.Application.identityservices.entity.User;
import net.codejava.Application.identityservices.services.UserServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class UserController {
    @Autowired
    private UserServices userServices ;

     @PostMapping("/users")
     User createUser(@RequestBody UserCreationRequest request) {
        return userServices.createRequest(request) ;

     } 
     
}
