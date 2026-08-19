package net.codejava.Application.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequest {
    String name;

    @Size(min = 3,max = 50, message = "USER_VALIDATION")
    String username ;

    @Size(min = 8,max = 100, message = "PASWORD_VALIDATION")
    String password ;
    LocalDate dob ;
}
