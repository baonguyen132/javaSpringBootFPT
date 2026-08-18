package net.codejava.Application.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import net.codejava.Application.dto.request.UserCreationRequest;
import net.codejava.Application.dto.request.UserUpdateRequest;
import net.codejava.Application.entity.User;
import net.codejava.Application.services.UserServices;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/users")
public class UserController {
    private UserServices userServices;

    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

     @PostMapping
     User createUser(@RequestBody @Valid UserCreationRequest request) {
        return userServices.createRequest(request) ;
     } 

     @GetMapping
     List<User> getUsers() {
      return userServices.getUsers() ;
     }
     
     @GetMapping("/{userId}")
     User getUser(@PathVariable String userId) {
         return userServices.getUser(userId);
     }

     @PutMapping("/{userId}")
     User updateUser(@RequestBody UserUpdateRequest request , @PathVariable String userId) {
         return userServices.updateUser(request, userId) ;  
     }
     
     @DeleteMapping("/{userId}")
     void deleteUser(@PathVariable String userId) {
         userServices.deleteUser(userId) ;
     }
     
     
}
