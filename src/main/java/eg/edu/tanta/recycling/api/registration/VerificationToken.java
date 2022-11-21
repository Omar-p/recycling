package eg.edu.tanta.recycling.api.registration;

import eg.edu.tanta.recycling.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class VerificationToken {
  private static final int EXPIRATION = 60 * 24;

  @Id
  @SequenceGenerator(
      name = "verification_token_id_generator",
      allocationSize = 1,
      sequenceName = "verification_token_id_sequence"
  )
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verification_token_id_generator")
  private Long id;

  private String token;

  @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "user_id")
  private User user;

  private Date expiryDate;

  public VerificationToken(String token, User user) {
    this.token = token;
    this.user = user;
    this.expiryDate = calculateExpiryDate(EXPIRATION);
  }

  private Date calculateExpiryDate(int expiryTimeInMinutes) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Timestamp(cal.getTime().getTime()));
    cal.add(Calendar.MINUTE, expiryTimeInMinutes);
    return new Date(cal.getTime().getTime());
  }

  public void updateToken(String newToken) {
    this.token = newToken;
    this.expiryDate = calculateExpiryDate(EXPIRATION);

  }
}
