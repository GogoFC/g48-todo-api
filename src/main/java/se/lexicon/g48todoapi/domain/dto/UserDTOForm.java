package se.lexicon.g48todoapi.domain.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTOForm {

    @NotBlank(message = "email field cannot be blank")
    @Email(message = "email field must be a valid email address")
    private String email;

//    @NotBlank(message = "password must not be blank")
//    @Size(min= 8, message = "Password must be at least 8 characters long.")
    @Pattern(regexp = "(?=^.{6,10}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&amp;*()_+}{&quot;:;'?/&gt;.&lt;,])(?!.*\\s).*$",
    message = "Password must have at least 1 small-case letter, 1 Capital letter, 1 digit, 1 special character and the length should be between 6-10 characters.")
    private String password;

    @NotNull(message = "role must not be null")
    @Size(min = 1, message = "User must have at least 1 role")
    private Set<RoleDTOForm> roles;
}
