package com.waleed.expenseTracker.data;
import com.waleed.expenseTracker.enums.CategoryType;
import com.waleed.expenseTracker.model.entity.Category;
import com.waleed.expenseTracker.model.entity.Role;
import com.waleed.expenseTracker.model.entity.User;
import com.waleed.expenseTracker.repository.CategoryRepository;
import com.waleed.expenseTracker.repository.RoleRepository;
import com.waleed.expenseTracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.waleed.expenseTracker.enums.CategoryType.EXPENSE;
import static com.waleed.expenseTracker.enums.CategoryType.INCOME;

@Slf4j
@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final CategoryRepository categoryRepository;
	private final PasswordEncoder passwordEncoder;

	private static final String USER_CREATED_MSG = "User {} created!";

    private static final Set<String> ROLES = Set.of("ADMIN", "USER");
	private static final String PASSWORD = "P@ssw0rd";
	private static final List<String> INCOMES = List.of(
			"Salary", "Rent", "Investment", "Overtime"
	);
	private static final List<String> EXPENSES = List.of(
			"Rent", "Debt", "Entertainment", "Home", "Personal"
	);
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		createDefaultRoleIfNotExists();
	    createDefaultUserIfNotExists();
		createDefaultAdminIfNotExists();
		createCategoriesIfNotExists();
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

	@Transactional
	protected void createCategoriesIfNotExists() {
		List<User> users = userRepository.findAll();
		for (User user : users) {
			createMissingCategoriesForUser(user, INCOME, INCOMES);
			createMissingCategoriesForUser(user, EXPENSE, EXPENSES);
		}
	}

	private void createMissingCategoriesForUser(User user, CategoryType type, List<String> defaultNames) {
		List<String> existingNames = categoryRepository
				.findByTypeAndUserId(type, user.getId())
				.stream()
				.map(Category::getName)
				.toList();

		List<String> missingNames = defaultNames.stream()
				.filter(name -> !existingNames.contains(name))
				.toList();

		if (missingNames.isEmpty())
			return;

		log.info("User {} Missing {} categories: {}", user.getId(), type, missingNames);

		List<Category> newCategories = missingNames.stream()
				.map(name -> Category.builder()
						.name(name)
						.type(type)
						.user(user)
						.build())
				.toList();
		categoryRepository.saveAll(newCategories);
	}

	private void createUser(String name, int userCount, String email, Role userRole) {
		User user = new User();
		user.setUsername(name + userCount);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(PASSWORD));
		user.setRoles(Set.of(userRole));
		user.setVerified(false);
		userRepository.save(user);
		log.info(USER_CREATED_MSG, user.getEmail());
	}
}
