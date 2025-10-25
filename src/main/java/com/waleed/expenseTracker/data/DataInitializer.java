package com.waleed.expenseTracker.data;
import com.waleed.expenseTracker.model.entity.Role;
import com.waleed.expenseTracker.model.entity.User;
import com.waleed.expenseTracker.repository.RoleRepository;
import com.waleed.expenseTracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	private static final String USER_CREATED_MSG = "User {} created!";

    private static final Set<String> ROLES = Set.of("ADMIN", "USER");
	private static final String PASSWORD = "P@ssw0rd";
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		createDefaultRoleIfNotExists();
	    createDefaultUserIfNotExists();
		createDefaultAdminIfNotExists();
	}

	private void createDefaultRoleIfNotExists() {
		ROLES.stream()
				.filter(role -> roleRepository.findByName(role).isEmpty())
				.map(Role :: new).forEach(roleRepository :: save);
	}
	
	private void createDefaultUserIfNotExists() {
		Role userRole = roleRepository.findByName("USER").get();
		for(int i = 1 ; i<= 5; i++) {
			String email = "user"+i+"@gmail.com";
			if(!userRepository.existsByEmail(email))
				createUser("User", i, email, userRole);
		}
	}

	private void createDefaultAdminIfNotExists() {
		Role adminRole = roleRepository.findByName("ADMIN").get();
		for(int i = 1 ; i<= 2; i++) {
			String email = "admin"+i+"@gmail.com";
			if(userRepository.existsByEmail(email))
				continue;
			createUser("Admin", i, email, adminRole);
		}
	}

	private void createUser(String name, int i, String email, Role userRole) {
		User user = new User();
		user.setUsername(name + i);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(PASSWORD));
		user.setRoles(Set.of(userRole));
		user.setVerified(false);
		userRepository.save(user);
		log.info(USER_CREATED_MSG, user.getEmail());
	}
}
