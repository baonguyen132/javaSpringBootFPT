package net.codejava.Application.dto.request;

import java.time.LocalDate;

public class UserCreationRequest {
    private String name;
    private String username ;
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
