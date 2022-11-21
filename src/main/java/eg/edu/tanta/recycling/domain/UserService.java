package eg.edu.tanta.recycling.domain;

import eg.edu.tanta.recycling.api.ApiResponse;
import eg.edu.tanta.recycling.api.dto.UserDto;
import eg.edu.tanta.recycling.api.registration.VerificationToken;
import eg.edu.tanta.recycling.api.registration.VerificationTokenRepository;
import eg.edu.tanta.recycling.exceptions.EmailIsAlreadyExist;
import eg.edu.tanta.recycling.exceptions.UsernameIsAlreadyExist;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final VerificationTokenRepository tokenRepository;

  public static final String TOKEN_INVALID = "invalidToken";
  public static final String TOKEN_EXPIRED = "expired";
  public static final String TOKEN_VALID = "valid";


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username).orElseThrow(
        () -> new UsernameNotFoundException("There is no user with " + username + " username.")
    );
  }

  public Optional<User> findUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public User registerNewUserAccount(UserDto userDto) {
    checkIfEmailIsAlreadyExist(userDto.getEmail());
    checkIfUsernameIsAlreadyExist(userDto.getUsername());
    final User newUser = toUser(userDto);

    final User user = userRepository.save(newUser);
    createVerificationTokenForUser(user, UUID.randomUUID().toString());
    return user;

  }

  public User getUser(final String verificationToken) {
    final Optional<VerificationToken> token = tokenRepository.findByToken(verificationToken);
    return token.map(VerificationToken::getUser).orElse(null);
  }

  public VerificationToken getVerificationToken(final String VerificationToken) {
    return tokenRepository.findByToken(VerificationToken).orElse(null);
  }

  public void saveRegisteredUser(final User user) {
    userRepository.save(user);
  }

  public void deleteUser(final User user) {
    final VerificationToken verificationToken = tokenRepository.findByUser(user);

    userRepository.delete(user);
  }

  public void createVerificationTokenForUser(final User user, final String token) {
    final VerificationToken myToken = new VerificationToken(token, user);
    tokenRepository.save(myToken);
  }

  public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
    VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken).get();
    vToken.updateToken(UUID.randomUUID()
        .toString());
    vToken = tokenRepository.save(vToken);
    return vToken;
  }

  private void checkIfEmailIsAlreadyExist(String email) {
    Optional<User> user = findUserByEmail(email);
    if (user.isPresent())
      throw new EmailIsAlreadyExist("'" + user.get().getEmail() + "' is already exist.");
  }

  private void checkIfUsernameIsAlreadyExist(String username) {
    Optional<User> user = findByUsername(username);
    if (user.isPresent())
      throw new UsernameIsAlreadyExist("'" + user.get().getUsername() + "' is already exist.");
  }

  private User toUser(UserDto userDto) {
    return new User(
        null,
        userDto.getUsername(),
        userDto.getEmail(),
        passwordEncoder.encode(userDto.getPassword()),
        userDto.getPhone(),
        false
    );
  }

  public String findVerificationTokenByUser(User user) {
    return tokenRepository.findByUser(user).getToken();
  }

  public ApiResponse<?> validateVerificationToken(String token) {
    final Optional<VerificationToken> verificationToken = tokenRepository.findByToken(token);
    if (verificationToken.isEmpty()) {
      return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), TOKEN_INVALID, null);
    }

    final User user = verificationToken.get().getUser();
    final Calendar cal = Calendar.getInstance();
    if ((verificationToken.get().getExpiryDate()
        .getTime() - cal.getTime()
        .getTime()) <= 0) {
      tokenRepository.delete(verificationToken.get());
      return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), TOKEN_EXPIRED, null);
    }

    user.setEnabled(true);
    tokenRepository.delete(verificationToken.get());
    userRepository.save(user);
    return new ApiResponse<>(HttpStatus.OK.value(), TOKEN_VALID, null);
  }
}
