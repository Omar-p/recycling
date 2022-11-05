package eg.edu.tanta.recycling.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationForm {

  private String username;
  @Email
  private String email;
  @Size(min = 6, max = 18)
  private String password;
  @Size(min = 11, max = 12)
  private String phone;
}
