package net.codejava.Application.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.codejava.Application.dto.request.APIResponse;
import net.codejava.Application.dto.request.AuthenticationRequest;
import net.codejava.Application.dto.response.AuthenticationResponse;
import net.codejava.Application.services.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService ;

    @PostMapping("/login")
    APIResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse result = authenticationService.authenticate(request) ;


        return APIResponse.<AuthenticationResponse>builder()
                .code(200)
                .result(result)
                .build();
    }
}
