package net.codejava.Application.identityservices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.codejava.Application.identityservices.dto.request.UserCreationRequest;
import net.codejava.Application.identityservices.entity.User;
import net.codejava.Application.identityservices.repository.UserRepository;

@Service
public class UserServices {
    @Autowired
    private UserRepository userRepository ;

    public User createRequest(UserCreationRequest request) {

        User user = new User() ;
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setDob(request.getDob());

        return userRepository.save(user);

    } 
}   
