package eg.edu.tanta.recycling.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

  private String username;

  @Email
  private String email;

  @Length(min = 6, max = 20)
  private String password;

  @Length(min = 11, max = 12)
  private String phone;
}
