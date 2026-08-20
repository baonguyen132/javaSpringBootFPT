package net.codejava.Application.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.codejava.Application.dto.request.UserCreationRequest;
import net.codejava.Application.dto.request.UserUpdateRequest;
import net.codejava.Application.entity.User;
import net.codejava.Application.exception.AppException;
import net.codejava.Application.exception.ErrorCode;
import net.codejava.Application.repository.UserRepository;

import net.codejava.Application.mapper.UserMapper ;

@Service
public class UserServices {
    @Autowired
    private UserRepository userRepository ;
    @Autowired
    private UserMapper userMapper ; 

    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createRequest(UserCreationRequest request) {

        System.out.println(request.getName());
        if (userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("Username already exists");
        
        User user = userMapper.toUser(request) ;
        System.out.println(user.toString());

        // User user = new User() ;
        // user.setName(request.getName());
        // user.setUsername(request.getUsername());
        // user.setPassword(request.getPassword());
        // user.setDob(request.getDob());

        return userRepository.save(user);

    }
    
    public List<User> getUsers() {
        return userRepository.findAll() ;
    }

    public User getUser(String userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

    }
    public User updateUser(UserUpdateRequest request, String userId) {
        User user = getUser(userId) ;

        userMapper.userUpdate(request, user);

        // user.setName(request.getName());
        // user.setPassword(request.getPassword());
        // user.setDob(request.getDob());

        return userRepository.save(user);
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}   
