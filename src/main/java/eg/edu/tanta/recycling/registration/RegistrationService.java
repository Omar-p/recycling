package eg.edu.tanta.recycling.registration;

import eg.edu.tanta.recycling.domain.User;
import eg.edu.tanta.recycling.domain.UserRepository;
import eg.edu.tanta.recycling.registration.exceptions.EmailIsAlreadyExist;
import eg.edu.tanta.recycling.registration.exceptions.UsernameIsAlreadyExist;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Transactional
  public long registerUser(RegistrationForm registrationForm) {
    checkIfEmailIsAlreadyExist(userRepository.findByUsername(registrationForm.getUsername()));
    checkIfUsernameIsAlreadyExist(userRepository.findByEmail(registrationForm.getEmail()));

    // TODO send email.
    final User user = toUser(registrationForm, passwordEncoder);
    userRepository.save(user);

    return user.getId();
  }

  private User toUser(RegistrationForm registrationForm, PasswordEncoder passwordEncoder) {
    return new User(
        null,
        registrationForm.getUsername(),
        registrationForm.getEmail(),
        passwordEncoder.encode(registrationForm.getPassword()),
        registrationForm.getPhone(),
        true
    );
  }

  private void checkIfEmailIsAlreadyExist(Optional<User> user) {
    if (user.isPresent())
      throw new EmailIsAlreadyExist("'" + user.get().getEmail() + "' is already exist.");
  }

  private void checkIfUsernameIsAlreadyExist(Optional<User> user) {
    if (user.isPresent())
      throw new UsernameIsAlreadyExist("'" + user.get().getUsername() + "' is already exist.");
  }

}
