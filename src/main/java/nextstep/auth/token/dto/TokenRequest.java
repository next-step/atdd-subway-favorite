package nextstep.auth.token.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequest {

    @NotBlank(message = "이메일은 반드시 입력해야 합니다.")
    @Email(message = "이메일 형식이어야 합니다.")
    private String email;

    private String password;

}
