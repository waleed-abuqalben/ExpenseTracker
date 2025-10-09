package com.waleed.expenseTracker.service.authentication;

import com.waleed.expenseTracker.model.request.auth.LoginRequest;
import com.waleed.expenseTracker.model.request.auth.RegisterRequest;
import com.waleed.expenseTracker.model.response.auth.LoginResponse;
import com.waleed.expenseTracker.model.response.auth.RegisterResponse;

public interface AuthenticationService {
     RegisterResponse register(RegisterRequest request);
     LoginResponse login(LoginRequest request);
}
