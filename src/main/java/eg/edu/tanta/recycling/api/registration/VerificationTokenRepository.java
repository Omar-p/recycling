package eg.edu.tanta.recycling.api.registration;

import eg.edu.tanta.recycling.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

  Optional<VerificationToken> findByToken(String token);

  VerificationToken findByUser(User user);
}
