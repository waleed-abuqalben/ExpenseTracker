package com.waleed.expenseTracker.controller;

import com.waleed.expenseTracker.model.request.auth.LoginRequest;
import com.waleed.expenseTracker.model.request.auth.RegisterRequest;
import com.waleed.expenseTracker.model.response.ApiResponse;
import com.waleed.expenseTracker.model.response.auth.LoginResponse;
import com.waleed.expenseTracker.model.response.auth.RegisterResponse;
import com.waleed.expenseTracker.service.authentication.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("register")
    public ResponseEntity<ApiResponse<RegisterResponse>>
    register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity
                .status(CREATED).body(ApiResponse.of(
                "Success", authenticationService.register(request)));
    }
    @PostMapping("login")
    public ResponseEntity<ApiResponse<LoginResponse>>
    login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(ApiResponse.of("login success", authenticationService.login(request)));
    }
}
