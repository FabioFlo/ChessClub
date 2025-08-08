package org.csc.chessclub.config;

import java.util.logging.Logger;
import org.csc.chessclub.enums.Role;
import org.csc.chessclub.model.user.UserEntity;
import org.csc.chessclub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@Order(1)
public class AdminRunner implements CommandLineRunner {

  private static final Logger log = Logger.getLogger(AdminRunner.class.getName());
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${fake.admin.username}")
  private String fakeAdminUsername;
  @Value("${fake.admin.password}")
  private String fakeAdminPassword;
  @Value("${fake.admin.email}")
  private String fakeAdminEmail;
  @Value("${fake.admin.create.flag}")
  private Boolean fakeAdminCreateFlag;

  public AdminRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  private void createFakeAdmin() {
    UserEntity fakeAdmin = userRepository.findUserEntityByUsernameOrEmail(fakeAdminUsername,
            fakeAdminEmail)
        .orElse(null);

    if (fakeAdmin == null) {
      fakeAdmin = new UserEntity();
      fakeAdmin.setUsername(fakeAdminUsername);
      fakeAdmin.setPassword(passwordEncoder.encode(fakeAdminPassword));
      fakeAdmin.setEmail(fakeAdminEmail);
      fakeAdmin.setAvailable(true);
      fakeAdmin.setRole(Role.ADMIN);

      userRepository.save(fakeAdmin);
      log.info("Created fake Admin with username: " + fakeAdminUsername + " and password: "
          + fakeAdminPassword);
    } else {
      log.info("Fake admin already exists with username: " + fakeAdmin.getUsername());
    }
  }

  @Override
  public void run(String... args) {
    if (fakeAdminCreateFlag) {
      createFakeAdmin();
    } else {
      log.info(
          "Skipping fake admin creation - fake admin create flag set to " + fakeAdminCreateFlag);
    }
  }
}
