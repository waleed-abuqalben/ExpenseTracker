package com.waleed.expenseTracker.service.user;

import com.waleed.expenseTracker.exception.AppException;
import com.waleed.expenseTracker.model.dto.UserDto;
import com.waleed.expenseTracker.model.entity.User;
import com.waleed.expenseTracker.model.request.auth.RegisterRequest;
import com.waleed.expenseTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public User createUser(RegisterRequest request) {
        log.info("About to Create user {}", request.email());
        if(userRepository.existsByEmail(request.email()))
            throw new AppException(
                    String.format("User with email %s already exists", request.email()));

        User user = new User();
        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        User savedUser = userRepository.save(user);
        log.info("User {} created successfully", user.getEmail());
        return savedUser;
    }

    @Override
    public User getUserById(Long id) {
      return userRepository.findById(id)
              .orElseThrow(() ->
                new AppException(String.format("User with id %s not found", id)));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(String.format("User with email %s not found", email)));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository :: delete,
                        () -> {throw new AppException("User not found. id: "+userId);});
    }

    @Override
    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
