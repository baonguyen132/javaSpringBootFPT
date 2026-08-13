package net.codejava.Application.identityservices.dto.request;

import java.time.LocalDate;

public class UserUpdateRequest {
    private String name;
    private String password ;
    private LocalDate dob ;

    public LocalDate getDob() {
        return dob;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
