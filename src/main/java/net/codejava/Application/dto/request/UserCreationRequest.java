package net.codejava.Application.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

public class UserCreationRequest {
    private String name;

    @Size(min = 3,max = 50, message = "Username must be at least 3 characters long and at most 50 characters long")
    private String username ;

    @Size(min = 8,max = 100, message = "Password must be at least 8 characters long and at most 100 characters long")
    private String password ;
    private LocalDate dob ;

    public LocalDate getDob() {
        return dob;
    }
    public String getName() {
        return name;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
