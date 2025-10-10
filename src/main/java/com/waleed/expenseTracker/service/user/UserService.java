package com.waleed.expenseTracker.service.user;

import com.waleed.expenseTracker.model.dto.UserDto;
import com.waleed.expenseTracker.model.request.auth.RegisterRequest;
import com.waleed.expenseTracker.model.entity.User;

public interface UserService {
    User createUser(RegisterRequest request);
    User getUserById(Long id);
    User getUserByEmail(String email);
    void deleteUser(Long userId);
    UserDto convertToDto(User user);

}
