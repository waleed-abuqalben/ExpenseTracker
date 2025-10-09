package com.waleed.expenseTracker.service.authentication;

import com.waleed.expenseTracker.enums.AuthenticationStatus;
import com.waleed.expenseTracker.model.entity.User;
import com.waleed.expenseTracker.model.request.auth.LoginRequest;
import com.waleed.expenseTracker.model.request.auth.RegisterRequest;
import com.waleed.expenseTracker.model.response.auth.LoginResponse;
import com.waleed.expenseTracker.model.response.auth.RegisterResponse;
import com.waleed.expenseTracker.security.jwt.JwtUtils;
import com.waleed.expenseTracker.security.user.SystemUserDetails;
import com.waleed.expenseTracker.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    @Override
    public RegisterResponse register(RegisterRequest request) {
        User user = userService.createUser(request);
        /*
            TODO: Generate Verification Token
            TODO: Send Verification OTP
        */

        String token = "Verification Token";
        return new RegisterResponse(user.getId(), user.getUsername(),
                            user.getEmail(), token);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateTokenForUser(authentication);
        SystemUserDetails userDetails = (SystemUserDetails) authentication.getPrincipal();
        return new LoginResponse(AuthenticationStatus.SUCCESS, "Success", jwtToken);
    }
}
