package eg.edu.tanta.recycling.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(name = "users_username_unique", columnNames = "username"),
        @UniqueConstraint(name = "users_email_unique", columnNames = "email")
    }
)
public class User implements UserDetails {

  @Id
  @SequenceGenerator(
      name = "user_id_generator",
      allocationSize = 1,
      sequenceName = "user_id_sequence"
  )
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
  private Long id;

  @Column(name = "username")
  private String username;

  @Email
  @Column(name = "email")
  private String email;


  private String password;

  @Length(min = 11, max = 12)
  private String phone;

  private boolean enabled;


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
